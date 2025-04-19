package de.hermannbsd.phpini.library.interfaces;

/**
 * Interface for a PHP INI line.
 * This interface provides methods to get the line, name, value, and comment of the INI line.
 */
public interface IIniLine {
    /**
     * Get the line of the INI file.
     *
     * @return the line as a String
     */
    String getLine();

    /**
     * Get the name of the directive.
     *
     * @return the name of the directive as a String
     */
    String getDirectiveName();

    /**
     * Get the value of the directive.
     *
     * @return the value of the directive as a String
     */
    String getValue();

    /**
     * Get the comment of the directive.
     *
     * @return the comment of the directive as a String
     */
    String getComment();

    /**
     * Gets whether the line is commented.
     *
     * @return is the line commented?
     */
    boolean isCommented();

    /**
     * Gets whether the line is a comment only.
     *
     * @return is the line a comment only?
     */
    boolean isCommentOnly();

    /**
     * Gets whether the line has a value.
     *
     * @return does the line have a value?
     */
    boolean hasValue();

    /**
     * Gets whether the line is empty.
     *
     * @return is the line empty?
     */
    boolean isEmpty();
}
