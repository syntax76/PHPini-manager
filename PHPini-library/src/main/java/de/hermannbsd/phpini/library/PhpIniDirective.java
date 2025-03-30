package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Class representing a PHP INI directive.
 */
public class PhpIniDirective implements IPhpIniDirective {

    /**
     * Logger for the PhpIniDirective class.
     */
    private static final Logger logger = LoggerFactory.getLogger(PhpIniDirective.class);
    /**
     * The name of the CSV file containing the directives.
     */
    private static final String DIRECTIVES_CSV = "phpini_directives.csv";
    /**
     * The list of directives loaded from the CSV file.
     */
    private static final List<IPhpIniDirective> directives = new ArrayList<>();

    /**
     * The name of the directive.
     */
    private String name;
    /**
     * The value of the directive.
     */
    private String value;
    /**
     * The section of the directive.
     */
    private String section;
    /**
     * The description of the directive.
     */
    private String description;
    /**
     * The changelog of the directive.
     */
    private String changelog;
    /**
     * The default value of the directive.
     */
    private String defaultValue;
    /**
     * The changeable type of the directive.
     */
    private DirectiveChangeable changeable;

    /**
     * The default directive for this directive.
     */
    private IPhpIniDirective defaultDirective;

    /**
     * Constructor for the PhpIniDirective class.
     * This constructor takes a row from the INI file and initializes the directive.
     *
     * @param row the row from the INI file
     */
    public PhpIniDirective(String row) {
        if (row == null || row.trim().isEmpty() || row.startsWith("#") || row.startsWith(";")) {
            logger.info("Skipping empty or comment line: {}", row);
        } else if (row.startsWith("[")) {
            section = handleSection(row);
        } else {
            if (!tryLoadDirectives()) {
                String errorLoadingDirectives = "Error loading directives";
                logger.error(errorLoadingDirectives);
                throw new IllegalStateException(errorLoadingDirectives);
            }

            if (row.contains(";")) { // Remove comments
                row = row.substring(0, row.indexOf(';') - 1).trim();
            } else if (row.contains("#")) { // Remove comments
                row = row.substring(0, row.indexOf('#') - 1).trim();
            } else {
                row = row.trim();
            }

            String[] parts = row.split("=", 2);
            if (parts.length < 2) {
                logger.error("Invalid directive format: {}", row);
                throw new IllegalArgumentException("Invalid directive format: " + row);
            }
            this.name = parts[0].trim();
            this.value = parts[1].trim();
            defaultDirective = getDirectiveByName(this.name);
            if (defaultDirective != null) {
                this.defaultValue = defaultDirective.getDefaultValue();
                this.section = defaultDirective.getSection();
                this.description = defaultDirective.getDescription();
                this.changelog = defaultDirective.getChangelog();
                this.changeable = defaultDirective.getChangeable();
            } else {
                this.defaultValue = "";
                this.section = "";
                this.description = "";
                this.changelog = "";
                this.changeable = DirectiveChangeable.INI_SYSTEM;
                logger.warn("Directive not found in CSV: {}", this.name);
            }
        }
    }

    /**
     * Handle the section line in the INI file.
     *
     * @param row the section line
     * @return the section name
     */
    private @NotNull String handleSection(String row) {
                logger.info("Section line: {}", row);
        int endSection = row.indexOf("]");
        if (endSection > 0) {
            this.section = row.substring(1, endSection - 1).trim();
            logger.info("Section: {}", section);
        } else {
            logger.error("Invalid section line: {}", row);
            throw new IllegalArgumentException("Invalid section line: " + row);
        }
        return section;
    }

    /**
     * Constructor for the PhpIniDirective class.
     * This constructor is used to create a new directive with the given parameters.
     *
     * @param name         the name of the directive
     * @param defaultValue the default value of the directive
     * @param description  the description of the directive
     * @param changelog    the changelog of the directive
     * @param changeable   the changeable type of the directive
     */
    private PhpIniDirective(String name, String defaultValue, String description,
                           String changelog, DirectiveChangeable changeable) {
        this.name = name;
        this.value = null;
        this.section = null;
        this.description = description;
        this.changelog = changelog;
        this.defaultValue = defaultValue;
        this.changeable = changeable;

        defaultDirective = null;
    }

    /**
     * Get the default directive for this directive.
     *
     * @param name the name of the directive
     * @return the default directive
     */
    private static IPhpIniDirective getDirectiveByName(String name) {
        IPhpIniDirective result = null;
        for (IPhpIniDirective directive : directives) {
            if (directive.getName().equals(name)) {
                result = directive;
                break;
            }
        }
        return result;
    }

    /**
     * Try to load the directives from the CSV file.
     *
     * @return true if the directives were loaded successfully, false otherwise
     */
    private boolean tryLoadDirectives() {
        if (directives.isEmpty()) {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(DIRECTIVES_CSV)))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    List<String> parts = List.of(line.split(";", 6));
                    if (parts.size() >= 4 && lineNumber > 0) {
                        String tmpName = parts.get(0).trim();
                        String tmpDefaultValue = parts.get(1).replace("\"", "").trim();
                        DirectiveChangeable tmpChangeable = DirectiveChangeable.valueOf(parts.get(2).trim());
                        String tmpChangeLog = parts.get(3).replace("\"", "").trim();
                        String tmpDescription = parts.size() >= 5 ? parts.get(4).replace("\"", "").trim() : "";
                        directives.add(new PhpIniDirective(tmpName, tmpDefaultValue, tmpDescription, tmpChangeLog, tmpChangeable));
                    } else if (lineNumber > 0) {
                        logger.error("Invalid directive format: {}", line);
                    } else {
                        logger.info("Skipping header line: {}", line);
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                logger.error("Error loading directives", e);
            }
        }
        return !directives.isEmpty();
    }

    /**
     * Get the name of the directive.
     *
     * @return the name of the directive as a String
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the value of the directive.
     *
     * @param value the value of the directive
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the value of the directive.
     *
     * @return the value of the directive as a String
     */
    @Override
    public String getValue() {
        return getNotEmptyString(value);
    }

    /**
     * Get the default value of the directive.
     *
     * @return the default value of the directive as a String
     */
    @Override
    public String getDefaultValue() {
        return getNotEmptyString(defaultValue);
    }

    /**
     * Gets where the directive can be changed.
     *
     * @return The changeable type of the directive.
     */
    @Override
    public DirectiveChangeable getChangeable() {
        return changeable;
    }

    /**
     * Get the description of the directive.
     *
     * @return the description of the directive as a String
     */
    @Override
    public String getDescription() {
        return getNotEmptyString(description);
    }

    /**
     * Get the changelog of the directive.
     *
     * @return the changelog of the directive as a String
     */
    @Override
    public String getChangelog() {
        return getNotEmptyString(changelog);
    }

    /**
     * Get the section of the directive.
     *
     * @return the section of the directive as a String
     */
    @Override
    public String getSection() {
        return getNotEmptyString(section);
    }

    private static String getNotEmptyString(String value) {
        return value != null && !value.trim().isEmpty() ? value : "";
    }

    @Override
    public String toString() {
        return name + " = " + value;
    }
}

