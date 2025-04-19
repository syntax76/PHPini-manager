package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import de.hermannbsd.phpini.library.interfaces.IPhpIniSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a PHP INI section.
 * This class implements the IPhpIniSection interface and provides methods to manipulate the section.
 */
public class PhpIniSection implements IPhpIniSection {

    private static final Logger logger = LoggerFactory.getLogger(PhpIniSection.class);
    /**
     * The name of the section.
     */
    private final String sectionName;

    /**
     * The directives in the section.
     */
    private final List<IPhpIniDirective> directives;

    /**
     * Constructor with a given section name.
     * @param sectionName the given section name
     */
    public PhpIniSection(String sectionName) {
        this.sectionName = sectionName;
        this.directives = new ArrayList<>();
    }

    /**
     * Constructor with a given section name and directives.
     * @param sectionName the given section name
     * @param directives the given directives
     */
    public PhpIniSection(String sectionName, List<IPhpIniDirective> directives) {
        this.sectionName = sectionName;
        this.directives = directives;
    }

    /**
     * Get the name of the section.
     *
     * @return the name of the section as a String
     */
    @Override
    public String getName() {
        return sectionName;
    }

    /**
     * Get the directives in the section.
     *
     * @return a list of directives in the section
     */
    @Override
    public List<IPhpIniDirective> getDirectives() {
        return directives;
    }

    /**
     * Tries to get a directive by its name.
     *
     * @param directiveName the name of the directive to search for
     * @return the directive if found, null otherwise
     */
    @Override
    public @Nullable IPhpIniDirective getDirectiveByName(String directiveName) {
        IPhpIniDirective result = null;

        for (IPhpIniDirective directive : directives) {
            if (directive.getName().equalsIgnoreCase(directiveName)) {
                result = directive;
                break;
            }
        }

        if (result == null) {
            logger.info("Directive {} not found in section {}", directiveName, sectionName);
        } else {
            logger.info("Directive {} found in section {}", directiveName, sectionName);
        }

        return result;
    }

    /**
     * Tries to add a directive to the section.
     *
     * @param directive the directive to add
     * @return true if the directive was added, false otherwise
     */
    @Override
    public boolean tryAddDirective(@NotNull IPhpIniDirective directive) {
        boolean result;

        if (containsDirectiveByName(directive.getName())) {
            result = false;
            logger.info("Directive {} already exists in section {}", directive.getName(), sectionName);
        } else {
            directives.add(directive);
            result = true;
            logger.info("Directive {} added to section {}", directive.getName(), sectionName);
        }

        return result;
    }

    /**
     * Tries to update a directive from the section.
     * If the directive is not found, it will be added.
     *
     * @param directive the directive to update
     * @return true if the directive was updated, false otherwise
     */
    @Override
    public boolean tryUpdateDirective(@NotNull IPhpIniDirective directive) {
        boolean result = false;

        try {
            if (containsDirectiveByName(directive.getName())) {
                for (IPhpIniDirective dir : directives) {
                    if (dir.getName().equalsIgnoreCase(directive.getName())) {
                        dir.setValue(directive.getValue());
                        result = true;
                        break;
                    }
                }

                logger.info("Directive {} updated in section {}", directive.getName(), sectionName);
            } else {
                directives.add(directive);
                logger.info("Directive {} added to section {}", directive.getName(), sectionName);
                result = true;
            }
        } catch (Exception e) {
            result = false;
            logger.error("Error updating directive: {}", e.getMessage());
        }

        return result;
    }

    /**
     * Tries to remove a directive from the section.
     *
     * @param directive the directive to remove
     * @return true if the directive was removed, false otherwise
     */
    @Override
    public boolean tryRemoveDirective(@NotNull IPhpIniDirective directive) {
        return tryRemoveDirective(directive.getName());
    }

    /**
     * Tries to remove a directive from the section.
     *
     * @param directiveName the name of the directive to remove
     * @return true if the directive was removed, false otherwise
     */
    @Override
    public boolean tryRemoveDirective(@NotNull String directiveName) {
        boolean result = false;

        if (containsDirectiveByName(directiveName)) {
            for (IPhpIniDirective dir : directives) {
                if (dir.getName().equalsIgnoreCase(directiveName)) {
                    directives.remove(dir);
                    result = true;
                    break;
                }
            }
        }

        if (!result) {
            logger.info("Directive {} not found in section {}", directiveName, sectionName);
        } else {
            logger.info("Directive {} removed from section {}", directiveName, sectionName);
        }

        return result;
    }

    /**
     * Gets whether the section contains a directive with the given name.
     *
     * @param directiveName the given name
     * @return is there a directive with the given name?
     */
    @Override
    public boolean containsDirectiveByName(@NotNull String directiveName) {
        boolean result = false;

        for (IPhpIniDirective directive : directives) {
            if (directive.getName().equalsIgnoreCase(directiveName)) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Section: ").append(sectionName).append("\n");
        for (IPhpIniDirective directive : directives) {
            sb.append(directive.getName()).append(" = ").append(directive.getValue()).append("\n");
        }
        return sb.toString();
    }
}
