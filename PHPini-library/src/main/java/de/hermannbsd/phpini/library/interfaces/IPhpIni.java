package de.hermannbsd.phpini.library.interfaces;

import java.util.List;

/**
 * Interface for PHP INI file.
 * This interface represents a PHP INI file and provides methods to manipulate it.
 */
public interface IPhpIni {

    /**
     * Get the file path of the PHP INI file.
     *
     * @return the file path as a String
     */
    String getFilePath();
    /**
     * Set the file path of the PHP INI file.
     *
     * @param filePath the file path to set
     */
    void setFilePath(String filePath);
    /**
     * Get the file name of the PHP INI file.
     *
     * @return the file name as a String
     */
    String getFileName();
    /**
     * Set the file name of the PHP INI file.
     *
     * @param fileName the file name to set
     */
    void setFileName(String fileName);
    /**
     * Get the file name without extension of the PHP INI file.
     *
     * @return the file name without extension as a String
     */
    String getFileNameWithoutExtension();
    /**
     * Set the file name without extension of the PHP INI file.
     *
     * @param fileNameWithoutExtension the file name without extension to set
     */
    void setFileNameWithoutExtension(String fileNameWithoutExtension);
    /**
     * Get the file extension of the PHP INI file.
     *
     * @return the file extension as a String
     */
    String getFileExtension();
    /**
     * Get the file content of the PHP INI file.
     *
     * @return the file content as a String
     */
    String getFileContent();

    /**
     * Get the directives of the PHP INI file.
     *
     * @return a list of IPhpIniDirective objects representing the directives
     */
    List<IPhpIniDirective> getDirectives();

    /**
     * Adds a given directive to the PHP INI file.
     * @param directive the directive to add
     * @return true if the directive was added successfully, false otherwise
     */
    boolean addDirective(IPhpIniDirective directive);

    /**
     * Adds a directive to the PHP INI file.
     * @param name the name of the directive
     * @param value the value of the directive
     * @return true if the directive was added successfully, false otherwise
     */
    boolean addDirective(String name, String value);

    /**
     * Adds a directive to the PHP INI file.
     * @param name the name of the directive
     * @param value the value of the directive
     * @param section the section of the directive
     * @return true if the directive was added successfully, false otherwise
     */
    boolean addDirective(String name, String value, String section);

    /**
     * Updates a directive in the PHP INI file.
     * @param name the name of the directive
     * @param value the new value of the directive to update
     * @return true if the directive was updated successfully, false otherwise
     */
    boolean updateDirective(String name, String value);

    /**
     * Remove a directive from the PHP INI file.
     * @param directive the directive to update
     * @return true if the directive was removed successfully, false otherwise
     */
    boolean removeDirective(IPhpIniDirective directive);

    /**
     * Remove a directive from the PHP INI file.
     * @param name the name of the directive
     * @return true if the directive was removed successfully, false otherwise
     */
    boolean removeDirective(String name);

    /**
     * Get a directive by its name.
     * @param name the name of the directive
     * @return the directive with the given name, or null if not found
     */
    IPhpIniDirective getDirective(String name);

    /**
     * Tries to save the PHP INI file.
     * @return true if the file was saved successfully, false otherwise
     */
    boolean save();
}
