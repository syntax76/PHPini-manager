package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.enums.DirectiveChangeable;
import de.hermannbsd.phpini.library.helper.StringHelper;
import de.hermannbsd.phpini.library.interfaces.IIniLine;
import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import de.hermannbsd.phpini.library.php_type_interpreter.BoolInterpreter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Objects;

import static de.hermannbsd.phpini.library.helper.StringHelper.secureEqualQuotationMarks;

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
     * The PHP type name for float.
     */
    static final String PHP_FLOAT = "float";
    /**
     * The PHP type name for string.
     */
    static final String PHP_STRING = "string";

    /**
     * The format for invalid directive messages.
     */
    static final String INVALID_DIRECTIVE_FORMAT = "Invalid directive format: {}";

    /**
     * The message for when the directive name is null or empty.
     */
    static final String DIRECTIVE_NAME_CANNOT_BE_NULL_OR_EMPTY = "Directive name cannot be null or empty";

    /**
     * The name of the directive.
     */
    private String name;
    /**
     * The value of the directive.
     */
    private String value;

    /**
     * The real value of the directive.
     */
    private Object realValue;
    
    /**
     * The type of the directive.
     */
    private Type type;
    
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
    private DirectiveChangeable directiveChangeable;

    /**
     * The default directive for this directive.
     */
    private IPhpIniDirective defaultDirective;

    /**
     * The INI line associated with this directive.
     */
    private IIniLine iniLine;

    /**
     * Constructor for the PhpIniDirective class.
     * This constructor takes a row from the INI file and initializes the directive.
     *
     * @param row the row from the INI file
     * @param sectionString the section name of the directive
     */
    public PhpIniDirective(String row, String sectionString) {
        if (row == null || row.trim().isEmpty() || row.startsWith(";")) {
            logger.info("Skipping empty or comment line: {}", row);
        } else if (row.startsWith("[")) {
            section = handleSection(row);
        } else {
            iniLine = StringHelper.splitPhpIniLine(row);
            defaultInit(sectionString);
        }
    }

    /**
     * Default initialization method for the PhpIniDirective class.
     * This method initializes the directive with the given row and section string.
     *
     * @param sectionString the section name of the directive
     */
    private void defaultInit(String sectionString) {
        if (sectionString != null && !sectionString.trim().isEmpty()) {
            section = sectionString;
        } else {
            logger.error("Section string cannot be null or empty");
            throw new IllegalArgumentException("Section string cannot be null or empty");
        }
        if (!tryLoadDirectives()) {
            String errorLoadingDirectives = "Error loading directives";
            logger.error(errorLoadingDirectives);
            throw new IllegalStateException(errorLoadingDirectives);
        }

        this.name = iniLine.getDirectiveName();
        this.value = iniLine.getValue();

        defaultDirective = getDirectiveByName(this.name);
        if (defaultDirective != null) {
            this.type = getTypeByName(defaultDirective.getType());
            this.defaultValue = defaultDirective.getDefaultValue();
            this.section = defaultDirective.getSection();
            this.description = defaultDirective.getDescription();
            this.changelog = defaultDirective.getChangelog();
            this.directiveChangeable = defaultDirective.getDirectiveChangeable();
        } else {
            this.type = String.class;
            this.defaultValue = "";
            this.description = "";
            this.changelog = "";
            this.directiveChangeable = DirectiveChangeable.INI_SYSTEM;
            getDirectiveNotFoundInCsvWarn(this.name);
        }

        trySetPhpStyleValue(iniLine.getValue());

        if (this.section == null || this.section.isEmpty()) {
            this.section = sectionString;
        }
    }

    /**
     * Sets the name of the directive.
     *
     * @param name the name of the directive
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the real value of the directive.
     *
     * @param realValue the real value of the directive
     */
    protected void setRealValue(Object realValue) {
        this.realValue = realValue;
    }

    /**
     * Sets the type of the directive.
     *
     * @param type the type of the directive
     */
    protected void setType(Type type) {
        this.type = type;
    }

    /**
     * Sets the section of the directive.
     *
     * @param section the section of the directive
     */
    protected void setSection(String section) {
        this.section = section;
    }

    /**
     * Sets the description of the directive.
     *
     * @param description the description of the directive
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the changelog of the directive.
     *
     * @param changelog the changelog of the directive
     */
    void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    /**
     * Set the changeable type of the directive.
     *
     * @param changeable the changeable type of the directive
     */
    protected void setDirectiveChangeable(DirectiveChangeable changeable) {
        this.directiveChangeable = changeable;
    }

    /**
     * Get the default directive for this directive.
     *
     * @return the default directive
     */
    public IPhpIniDirective getDefaultDirective() {
        return defaultDirective;
    }

    /**
     * Set the default directive for this directive.
     *
     * @param defaultDirective the default directive
     */
    protected void setDefaultDirective(IPhpIniDirective defaultDirective) {
        this.defaultDirective = defaultDirective;
    }

    /**
     * Handle the section line in the INI file.
     *
     * @param row the section line
     * @return the section name
     */
    private @NotNull String handleSection(String row) {
        if (logger.isInfoEnabled()) {
            logger.info("Section line: {}", row);
        }
        int endSection = row.indexOf("]");
        if (endSection > 0) {
            this.section = row.substring(1, endSection).trim();
            if (logger.isInfoEnabled()) {
                logger.info("Section: {}", section);
            }
        } else {
            if (logger.isErrorEnabled()) {
                logger.error("Invalid section line: {}", row);
            }
            throw new IllegalArgumentException("Invalid section line: " + row);
        }
        return section;
    }

    /**
     * Constructor for the PhpIniDirective class.
     * This constructor is used to create a new directive based on an existing one.
     *
     * @param directive the existing directive
     */
    public PhpIniDirective(@NotNull IPhpIniDirective directive) {
        this.name = directive.getName();
        this.value = directive.getValue();
        this.type = getTypeByName(directive.getType());
        this.section = directive.getSection();
        this.description = directive.getDescription();
        this.changelog = directive.getChangelog();
        this.defaultValue = directive.getDefaultValue();
        this.directiveChangeable = directive.getDirectiveChangeable();
        this.realValue = directive.getRealValue();
    }

    /**
     * Constructor for the PhpIniDirective class.
     * This constructor is used to create a new directive with the given parameters.
     *
     * @param name         the name of the directive
     * @param defaultValue the default value of the directive
     * @param type         the type of the value of the directive
     * @param description  the description of the directive
     * @param changelog    the changelog of the directive
     * @param changeable   the changeable type of the directive
     */
    private PhpIniDirective(String name, String defaultValue, String type, String description,
                           String changelog, DirectiveChangeable changeable) {
        this.name = name;
        this.type = getTypeByName(type);
        this.setValue(defaultValue);
        this.section = null;
        this.description = description.replace("\"", "").trim();
        this.changelog = changelog.replace("\"", "").trim();
        this.defaultValue = defaultValue.replace("\"", "");
        this.directiveChangeable = changeable;

        defaultDirective = null;
    }

    /**
     * Tries to get the directive by the given name
     *
     * @param name the given directive name
     * @return the found directive or null if not found
     */
    protected static IPhpIniDirective getDirectiveByName(String name) {
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
     * Get the Java type by the given PHP type name.
     *
     * @param typeName the given PHP type name
     * @return the found Java type or String.class if not found
     */
    protected static Type getTypeByName(String typeName) {
        Type result = String.class;

        if (typeName == null || typeName.trim().isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Type shouldn't be null or empty ('{}')", typeName);
            }
            result = Object.class;
        } else if (typeName.equalsIgnoreCase("int")) {
            result = Integer.class;
        } else if (typeName.equalsIgnoreCase("bool")) {
            result = Boolean.class;
        } else if (typeName.equalsIgnoreCase(PHP_FLOAT)) {
            result = Float.class;
        } else if (typeName.equalsIgnoreCase(PHP_STRING)) {
            result = String.class;
        } else if (typeName.equalsIgnoreCase("array")) {
            result = List.class;
        } else if (typeName.equalsIgnoreCase("object")) {
            result = Object.class;
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("Type not implemented: {}", typeName);
            }
        }

        return result;
    }

    /**
     * Get the PHP type name of the given Java type.
     *
     * @param type the given Java type
     * @return the found PHP type name or the Java type name if not found
     */
    @NotNull
    protected static String getTypeName(@NotNull Type type) {
        String result;

        if (type.equals(Integer.class)) {
            result = "int";
        } else if (type.equals(Boolean.class)) {
            result = "bool";
        } else if (type.equals(Float.class)) {
            result = PHP_FLOAT;
        } else if (type.equals(String.class)) {
            result = PHP_STRING;
        } else if (type.equals(List.class)) {
            result = "array";
        } else if (type.equals(Object.class)) {
            result = "object";
        } else {
            result = type.getTypeName();
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
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(DIRECTIVES_CSV))))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    List<String> parts = List.of(line.split(";", 8));
                    loadDirectivesLine(parts, lineNumber, line);
                    lineNumber++;
                }
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error loading directives", e);
                }
            }
        }
        return !directives.isEmpty();
    }

    /**
     * Load the given line from the CSV file.
     *
     * @param parts the parts of the line
     * @param lineNumber the line number
     * @param line the line itself
     */
    private static void loadDirectivesLine(@NotNull List<String> parts, int lineNumber, String line) {
        if (parts.size() >= 5 && lineNumber > 0) {
            String tmpName = parts.get(0).trim();
            String tmpDefaultValue = parts.get(1).replace("\"", "");
            String tmpType = parts.get(2).replace("\"", "").trim();
            DirectiveChangeable tmpChangeable = DirectiveChangeable.valueOf(parts.get(3).trim());
            String tmpChangeLog = parts.get(4).replace("\"", "").trim();
            String tmpDescription = parts.size() >= 6 ? parts.get(5).replace("\"", "").trim() : "";
            PhpIniDirective phpIniDirective = new PhpIniDirective(tmpName, tmpDefaultValue, tmpType, tmpDescription, tmpChangeLog, tmpChangeable);
            // min and max versions should be added by setters
            directives.add(phpIniDirective);
        } else if (lineNumber > 0) {
            if (logger.isErrorEnabled()) {
                logger.error(INVALID_DIRECTIVE_FORMAT, line);
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Skipping header line: {}", line);
            }
        }
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
        this.value = value.replace("\"", "");
        if (trySetPhpStyleValue(value)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Value set to: {}", realValue);
            }
        } else if (logger.isErrorEnabled()) {
            logger.error("Failed to set value: {}", value);
        }
    }

    /**
     * Try to set the value of the directive in PHP or Java style.
     *
     * @param value the value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    protected boolean trySetPhpStyleValue(String value) {
        boolean result = false;
        if (value != null) {
            String typeName = type.getTypeName();
            switch (typeName) {
                case "java.lang.Integer", "int" -> result = setPhpStyleValueByTypeInt(value);
                case "java.lang.Boolean", "bool" -> result = trySetRealValue(BoolInterpreter.getBoolValue(value));
                case PHP_FLOAT -> result = setPhpStyleValueByTypeFloat(value);
                default -> result = setPhpStyleValueByValueTypeDefault(value);
            }
        }
        return result;
    }

    protected String getPhpStyleValue() {
        String result;

        switch (type.getTypeName()) {
            case "java.lang.Integer", "int" -> result = String.valueOf(realValue);
            case "java.lang.Boolean", "bool" -> result = BoolInterpreter.getStringValue((Boolean) realValue);
            case PHP_FLOAT -> result = String.valueOf(realValue);
            default -> result = String.valueOf(realValue);
        }

        return result;
    }

    /**
     * Set the Integer value of the directive as string.
     *
     * @param value the Integer value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    private boolean setPhpStyleValueByTypeInt(@NotNull String value) {
        boolean result = false;
        try {
            this.realValue = Integer.parseInt(value.replace("\"", ""));
            result = true;
        } catch (NumberFormatException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Failed to parse int value: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * Set the Float value of the directive as string..
     *
     * @param value the Float value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    private boolean setPhpStyleValueByTypeFloat(String value) {
        boolean result = false;
        try {
            this.realValue = Float.parseFloat(value);
            result = true;
        } catch (NumberFormatException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Failed to parse float value: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * Set the value of the directive as string.
     *
     * @param value the value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    private boolean setPhpStyleValueByValueTypeDefault(String value) {
        if (value.equalsIgnoreCase("null")) {
            this.realValue = null;
        } else {
            this.realValue = value;
        }
        return true;
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
     * Tries to set the real value of the directive.
     *
     * @param value the real value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    @Override
    public boolean trySetRealValue(Object value) {
        boolean result = false;

        if (value != null && value.getClass().equals(type)) {
            this.realValue = value;
            this.value = value.toString();
            result = true;
        } else if (value != null) {
            if (logger.isErrorEnabled()) {
                logger.error("Value type mismatch: expected {}, got {}", type, value.getClass());
            }
        } else {
            if (logger.isErrorEnabled()) {
                logger.error("Value cannot be null");
            }
        }

        return result;
    }

    /**
     * Get the real value of the directive.
     *
     * @return the real value of the directive as an Object
     */
    @Override
    public Object getRealValue() {
        return realValue;
    }

    /**
     * Set the type of the directive.
     *
     * @param type the type of the directive
     */
    @Override
    public void setType(String type) {
        this.type = getTypeByName(type);
    }

    /**
     * Get the type of the directive.
     *
     * @return the type of the directive as a String
     */
    @Override
    public String getType() {
        return getTypeName(type);
    }

    /**
     * Get the default value of the directive.
     *
     * @return the default value of the directive as a String
     */
    @Override
    public @NotNull String getDefaultValue() {
        return getNotEmptyString(defaultValue);
    }

    /**
     * Gets where the directive can be changed.
     *
     * @return The changeable type of the directive.
     */
    @Override
    public DirectiveChangeable getDirectiveChangeable() {
        return directiveChangeable;
    }

    /**
     * Get the description of the directive.
     *
     * @return the description of the directive as a String
     */
    @Override
    public @NotNull String getDescription() {
        return getNotEmptyString(description);
    }

    /**
     * Get the changelog of the directive.
     *
     * @return the changelog of the directive as a String
     */
    @Override
    public @NotNull String getChangelog() {
        return getNotEmptyString(changelog);
    }

    /**
     * Get the section of the directive.
     *
     * @return the section of the directive as a String
     */
    @Override
    public @NotNull String getSection() {
        return getNotEmptyString(section);
    }

    /**
     * Get a non-empty string.
     *
     * @param value the given string
     * @return the non-empty string or an empty string if null or empty
     */
    private static @NotNull String getNotEmptyString(String value) {
        return value != null && !value.trim().isEmpty() ? value : "";
    }

    /**
     * Get a directive by the given name and sets the given value.
     *
     * @param name the given directive name
     * @param value the given directive value
     *
     * @return the found directive or a new directive with the given name and value
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static @NotNull IPhpIniDirective getDirectiveByNameAndSetValue(String name, String value) throws IllegalArgumentException {
        IPhpIniDirective directive;
        if (name == null || name.trim().isEmpty()) {
            if (logger.isErrorEnabled()) {
                logger.error(DIRECTIVE_NAME_CANNOT_BE_NULL_OR_EMPTY);
            }
            throw new IllegalArgumentException(DIRECTIVE_NAME_CANNOT_BE_NULL_OR_EMPTY);
        } else {
            directive = getDirectiveByName(name);
            if (directive == null) {
                DirectiveChangeable changeable = DirectiveChangeable.INI_SYSTEM;
                getDirectiveNotFoundInCsvWarn(name);
                directive = new PhpIniDirective(name,
                        value,
                        PHP_STRING,
                        "",
                        "",
                        changeable);
            } else {
                directive.setValue(value);
            }

        }

        return directive;
    }

    /**
     * Log a warning if the directive is not found in the CSV file.
     *
     * @param name the name of the directive
     */
    private static void getDirectiveNotFoundInCsvWarn(String name) {
        if (logger.isWarnEnabled()) {
            logger.warn("Directive not found in CSV: {}", name);
        }
    }

    /**
     * Get the string representation of the directive.
     *
     * @return the string representation of the directive
     */
    @Override
    public String toString() {
        return "[" + section + "]: " + name + " = " + value + " (" + getTypeName(type) + ")";
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder();
        String outValue = secureEqualQuotationMarks(getPhpStyleValue());

        sb.append(name).append(" = ").append(outValue);
        if (description != null && !description.isEmpty()) {
            sb.append("\t; ").append(description);
        }

        return sb.toString();
    }




}

