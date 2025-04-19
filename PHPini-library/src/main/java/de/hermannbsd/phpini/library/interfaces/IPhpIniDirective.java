package de.hermannbsd.phpini.library.interfaces;

import de.hermannbsd.phpini.library.enums.DirectiveChangeable;

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
     * Tries to set the real value of the directive.
     *
     * @param value the real value of the directive
     * @return true if the value was set successfully, false otherwise
     */
    boolean trySetRealValue(Object value);

    /**
     * Get the real value of the directive.
     *
     * @return the real value of the directive as an Object
     */
    Object getRealValue();

    /**
     * Set the type of the directive.
     *
     * @param type the type of the directive
     */
    void setType(String type);

    /**
     * Get the type of the directive.
     *
     * @return the type of the directive as a String
     */
    String getType();

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
    DirectiveChangeable getDirectiveChangeable();

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

    /**
     * Get the content of the directive.
     *
     * @return the content of the directive as a String
     */
    String getContent();
}
