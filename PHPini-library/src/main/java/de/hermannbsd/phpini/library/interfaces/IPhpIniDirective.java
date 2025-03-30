package de.hermannbsd.phpini.library.interfaces;

import de.hermannbsd.phpini.library.DirectiveChangeable;

/**
 * Interface for a PHP INI directive.
 */
public interface IPhpIniDirective {
    /**
     * Get the name of the directive.
     *
     * @return the name of the directive as a String
     */
    String getName();

    /**
     * Set the value of the directive.
     *
     * @param value the value of the directive
     */
    void setValue(String value);

    /**
     * Get the value of the directive.
     *
     * @return the value of the directive as a String
     */
    String getValue();

    /**
     * Get the default value of the directive.
     *
     * @return the default value of the directive as a String
     */
    String getDefaultValue();

    /**
     * Gets where the directive can be changed.
     *
     * @return The changeable type of the directive.
     */
    DirectiveChangeable getChangeable();

    /**
     * Get the description of the directive.
     *
     * @return the description of the directive as a String
     */
    String getDescription();

    /**
     * Get the changelog of the directive.
     *
     * @return the changelog of the directive as a String
     */
    String getChangelog();

    /**
     * Get the section of the directive.
     *
     * @return the section of the directive as a String
     */
    String getSection();
}
