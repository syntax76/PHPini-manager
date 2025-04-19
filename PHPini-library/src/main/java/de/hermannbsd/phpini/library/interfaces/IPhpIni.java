package de.hermannbsd.phpini.library.interfaces;

import org.jetbrains.annotations.NotNull;

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
     * Get the directives of the PHP INI file as a dictionary.
     *
     * @return a dictionary where the key is the section name and the value is a list of IPhpIniDirective objects
     */
    List<IPhpIniSection> getIni();

    /**
     * Gets whether the PHP INI file contains a section with the given name.
     * @param sectionName the given name
     * @return true if the section exists, false otherwise
     */
    boolean containsSection(String sectionName);

    /**
     * Gets whether the PHP INI file contains a directive with the given name.
     * @param directiveName the given name
     * @return true if the directive exists, false otherwise
     */
    boolean containsDirective(String directiveName);

    /**
     * Adds a given section to the PHP INI file.
     * @param section the section to add
     * @return true if the section was added successfully, false otherwise
     */
    boolean addSection(IPhpIniSection section);

    /**
     * Removes a given section from the PHP INI file.
     * @param section the section to remove
     * @return true if the section was removed successfully, false otherwise
     */
    boolean removeSection(IPhpIniSection section);

    /**
     * Removes a section from the PHP INI file.
     * @param sectionName the name of the section to remove
     * @return true if the section was removed successfully, false otherwise
     */
    boolean removeSection(String sectionName);

    /**
     * Adds a given directive to the PHP INI file.
     * @param directive the directive to add
     * @return true if the directive was added successfully, false otherwise
     */
    boolean addDirective(IPhpIniDirective directive);

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
    boolean removeDirective(@NotNull IPhpIniDirective directive);

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
