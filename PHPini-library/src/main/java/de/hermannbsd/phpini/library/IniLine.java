package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IIniLine;

/**
 * Class representing a line in an INI file.
 * This class implements the IIniLine interface and provides methods to manipulate the line.
 */
public class IniLine implements IIniLine {

    /**
     * The line of the INI file.
     */
    private final String line;
    /**
     * The name of the directive.
     */
    private String directiveName;
    /**
     * The value of the directive.
     */
    private String value;
    /**
     * The comment of the directive.
     */
    private String comment;

    /**
     * Constructor with a given line.
     *
     * @param line the given line
     */
    public IniLine(String line) {
        this.line = line;
        this.directiveName = "";
        this.value = "";
        this.comment = "";
    }

    /**
     * Get the line of the INI file.
     *
     * @return the line as a String
     */
    @Override
    public String getLine() {
        return line;
    }

    /**
     * Get the name of the directive.
     *
     * @return the name of the directive as a String
     */
    @Override
    public String getDirectiveName() {
        return directiveName;
    }

    /**
     * Set the name of the directive.
     *
     * @param directiveName the name of the directive
     */
    public void setDirectiveName(String directiveName) {
        this.directiveName = directiveName;
    }

    /**
     * Get the value of the directive.
     *
     * @return the value of the directive as a String
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Set the value of the directive.
     *
     * @param value the value of the directive
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the comment of the directive.
     *
     * @return the comment of the directive as a String
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment of the directive.
     *
     * @param comment the comment of the directive
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets whether the line is commented.
     *
     * @return is the line commented?
     */
    @Override
    public boolean isCommented() {
        return comment != null && !comment.isEmpty();
    }

    /**
     * Gets whether the line is a comment only.
     *
     * @return is the line a comment only?
     */
    @Override
    public boolean isCommentOnly() {
        return isCommented() && directiveName.isEmpty() && value.isEmpty();
    }

    /**
     * Gets whether the line has a value.
     *
     * @return does the line have a value?
     */
    @Override
    public boolean hasValue() {
        return !value.isEmpty();
    }

    /**
     * Gets whether the line is empty.
     *
     * @return is the line empty?
     */
    @Override
    public boolean isEmpty() {
        return line == null || line.isEmpty();
    }

    @Override
    public String toString() {
        return line;
    }
}
