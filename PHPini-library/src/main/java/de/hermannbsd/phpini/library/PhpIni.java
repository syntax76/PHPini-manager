package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIni;
import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import de.hermannbsd.phpini.library.interfaces.IPhpIniSection;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.*;

/**
 * Class representing a PHP INI file.
 * This class implements the IPhpIni interface and provides methods to manipulate the PHP INI file.
 */
public class PhpIni implements IPhpIni {

    /**
     * Default file attributes for the PHP INI file.
     * This is used to set the permissions of the temporary file created for the PHP INI.
     */
    protected static final FileAttribute<?>[] FILE_ATTRIBUTES = {
            new FileAttribute<>() {
                @Override
                public String name() {
                    return "posix:permissions";
                }

                @Override
                public Object value() {
                    return "rw-r--r--";
                }
            }
    };

    /**
     * Logger for the PhpIni class.
     */
    private static final Logger logger = LoggerFactory.getLogger(PhpIni.class);
    static final String DIRECTIVE_NOT_FOUND_IN_PHP_INI_FILE = "Directive {} not found in PHP INI file";
    static final String DIRECTIVE_FOUND_IN_PHP_INI_FILE = "Directive {} found in PHP INI file";
    static final String SECTION_NOT_FOUND_IN_PHP_INI_FILE = "Section {} not found in PHP INI file";
    static final String SECTION_FOUND_IN_PHP_INI_FILE = "Section {} found in PHP INI file";
    static final String SECTION_REMOVED_FROM_PHP_INI_FILE = "Section {} removed from PHP INI file";
    static final String SECTION_IS_NULL_OR_EMPTY = "Section is null or empty";

    /**
     * The path of the PHP INI file.
     */
    private final Path innerPath;

    /**
     * The file path of the PHP INI file.
     */
    private String filePath;
    /**
     * The file name of the PHP INI file.
     */
    private String fileName;
    /**
     * The file name without extension of the PHP INI file.
     */
    private String fileNameWithoutExtension;
    /**
     * The file extension of the PHP INI file.
     */
    private String fileExtension;
    /**
     * The file content of the PHP INI file.
     */
    private String fileContent;

    /**
     * Is the PHP INI file created?
     */
    private final boolean isCreated;

    /**
     * The current section of the PHP INI file.
     */
    private String currentSectionName;

    /**
     * The list of directives in the PHP INI file.
     */
    private List<IPhpIniSection> ini;

    /**
     * Constructor for the PhpIni class.
     *
     * @param filePath the file path to the PHP INI file
     * @exception IOException if the file path is invalid or cannot be created
     */
    public PhpIni(String filePath) throws IOException {
        this.filePath = filePath;

        if (filePath != null && tryGetFilePath(filePath)) {
            innerPath = Paths.get(filePath);
            isCreated = false;
            init();
        } else {
            try {
                innerPath = Files.createTempFile("php", ".ini", FILE_ATTRIBUTES);
                logger.trace("Temporary file created: {}", innerPath);
                isCreated = true;
                initVariables();
            } catch (IOException e) {
                String message = "Error creating temporary file: " + filePath;
                // logger error message, e
                throw new IOException(message, e);
            }
        }
    }

    private boolean tryGetFilePath(String filePath) {
        boolean result = false;
        try {
            result = Files.exists(Paths.get(filePath));
        } catch (Exception e) {
            logger.error("Error checking file path: {}", filePath, e);
        }

        return result;
    }

    /**
     * Initialize the file name, file name without extension, and file extension
     */
    private void init() {
        if (innerPath.toFile().canRead()) {
            try {
                this.fileContent = new String(Files.readAllBytes(innerPath));
            } catch (IOException e) {
                String message = "Error reading file: " + filePath;
                logger.error(message, e);
            }

            initVariables();

            if (!fileContent.isEmpty()) {
                logger.trace("File content of {} is not empty", filePath);
                initDirectives();
            } else {
                logger.trace("File content of {} is empty", filePath);
            }
        }
    }

    /**
     * Initialize the file name, file name without extension, and file extension
     */
    private void initVariables() {
        this.fileName = innerPath.getFileName().toString();
        this.fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        this.fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        this.ini = new ArrayList<>();
    }

    /**
     * Initialize the directives in the PHP INI file.
     * This method reads the file content and populates the ini HashMap with directives.
     */
    private void initDirectives() {
        if (!isCreated && fileContent != null && !fileContent.isEmpty()) {
            try {
                List<String> lines = Files.readAllLines(innerPath);
                for (String line : lines) {
                    IPhpIniDirective directive = new PhpIniDirective(line, currentSectionName);

                    if (directive.getName() != null && !directive.getName().isEmpty()
                            && !containsDirective(directive.getName())) {
                        addDirective(directive);
                        logger.debug("Directive {} added to section {}", directive.getName(), directive.getSection());
                    } else if (directive.getSection() != null && !directive.getSection().isEmpty()
                            && !directive.getSection().equalsIgnoreCase(currentSectionName)) {
                        currentSectionName = directive.getSection();
                        ini.add(new PhpIniSection(currentSectionName));
                        logger.debug("Section {} added to PHP INI file", currentSectionName);
                    } else if (directive.getSection().equalsIgnoreCase(currentSectionName)) {
                        logger.debug("Directive {} already exists in section {}", directive.getName(), directive.getSection());
                    } else {
                        logger.warn("Invalid directive: {}", line);
                    }
                }
            } catch (IOException e) {
                logger.error("Error reading file: {}", filePath, e);
            }
        }
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    @Override
    public void setFileNameWithoutExtension(String fileNameWithoutExtension) {
        this.fileNameWithoutExtension = fileNameWithoutExtension;
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public String getFileContent() {
        return fileContent;
    }

    /**
     * Get the directives of the PHP INI file as a dictionary.
     *
     * @return a dictionary where the key is the section name and the value is a list of IPhpIniDirective objects
     */
    @Override
    public List<IPhpIniSection> getIni() {
        return ini;
    }

    /**
     * Gets whether the PHP INI file contains a section with the given name.
     *
     * @param sectionName the given name
     * @return true if the section exists, false otherwise
     */
    @Override
    public boolean containsSection(String sectionName) {
        boolean result = false;

        for (IPhpIniSection section : ini) {
            if (section.getName().equalsIgnoreCase(sectionName)) {
                result = true;
                break;
            }
        }

        if (!result) {
            logger.info(SECTION_NOT_FOUND_IN_PHP_INI_FILE, sectionName);
        } else {
            logger.info(SECTION_FOUND_IN_PHP_INI_FILE, sectionName);
        }

        return result;
    }

    /**
     * Gets whether the PHP INI file contains a directive with the given name.
     *
     * @param directiveName the given name
     * @return true if the directive exists, false otherwise
     */
    @Override
    public boolean containsDirective(String directiveName) {
        boolean result = false;

        for (IPhpIniSection section : ini) {
            if (section.getDirectiveByName(directiveName) != null) {
                result = true;
                break;
            }
        }

        if (!result) {
            logger.info(DIRECTIVE_NOT_FOUND_IN_PHP_INI_FILE, directiveName);
        } else {
            logger.info(DIRECTIVE_FOUND_IN_PHP_INI_FILE, directiveName);
        }

        return result;
    }

    /**
     * Adds a given section to the PHP INI file.
     * @param section the section to add
     * @return true if the section was added successfully, false otherwise
     */
    @Override
    public boolean addSection(IPhpIniSection section) {
        boolean result = false;

        if (section != null && !section.getName().isEmpty()) {
            if (!containsSection(section.getName())) {
                ini.add(section);
                result = true;
                logger.info("Section {} added to PHP INI file", section.getName());
            } else {
                logger.warn("Section {} already exists in PHP INI file", section.getName());
            }
        } else {
            logger.error(SECTION_IS_NULL_OR_EMPTY);
        }

        return result;
    }

    /**
     * Removes a given section from the PHP INI file.
     *
     * @param section the section to remove
     * @return true if the section was removed successfully, false otherwise
     */
    @Override
    public boolean removeSection(IPhpIniSection section) {
        boolean result = false;

        if (section != null && containsSection(section.getName())) {
            ini.remove(section);
            result = true;
            logger.info(SECTION_REMOVED_FROM_PHP_INI_FILE, section.getName());
        } else if (section != null) {
            logger.warn(SECTION_NOT_FOUND_IN_PHP_INI_FILE, section.getName());
        } else {
            logger.error(SECTION_IS_NULL_OR_EMPTY);
        }

        return result;
    }

    /**
     * Removes a section from the PHP INI file.
     *
     * @param sectionName the name of the section to remove
     * @return true if the section was removed successfully, false otherwise
     */
    @Override
    public boolean removeSection(String sectionName) {
        boolean result = false;

        if (containsSection(sectionName)) {
            for (IPhpIniSection section : ini) {
                if (section.getName().equalsIgnoreCase(sectionName)) {
                    ini.remove(section);
                    result = true;
                    logger.info(SECTION_REMOVED_FROM_PHP_INI_FILE, sectionName);
                    break;
                }
            }
        } else {
            logger.warn(SECTION_NOT_FOUND_IN_PHP_INI_FILE, sectionName);
        }

        return result;
    }

    /**
     * Adds a given directive to the PHP INI file.
     *
     * @param directive the directive to add
     * @return true if the directive was added successfully, false otherwise
     */
    @Override
    public boolean addDirective(IPhpIniDirective directive) {
        boolean result = false;

        if (directive != null && !directive.getName().isEmpty()) {
            if (!containsDirective(directive.getName())) {
                for (IPhpIniSection section : ini) {
                    if (section.getName().equalsIgnoreCase(directive.getSection())) {
                        section.getDirectives().add(directive);
                        result = true;
                        logger.info("Directive {} added to section {} in PHP INI file", directive.getName(), directive.getSection());
                        break;
                    }
                }
            } else {
                logger.warn("Directive {} already exists in PHP INI file", directive.getName());
            }
        } else {
            logger.error("Directive is null or empty");
        }

        return result;
    }

    /**
     * Updates a directive in the PHP INI file.
     *
     * @param name  the name of the directive
     * @param value the new value of the directive to update
     * @return true if the directive was updated successfully, false otherwise
     */
    @Override
    public boolean updateDirective(String name, String value) {
        boolean result = false;

        if (name != null && !name.isEmpty()) {
            for (IPhpIniSection section : ini) {
                IPhpIniDirective directive = section.getDirectiveByName(name);
                if (directive != null) {
                    directive.setValue(value);
                    result = true;
                    logger.info("Directive {} updated in section {} in PHP INI file", name, section.getName());
                    break;
                }
            }
        } else {
            logger.error("Directive name is null or empty");
        }

        return result;
    }

    /**
     * Remove a directive from the PHP INI file.
     *
     * @param directive the directive to update
     * @return true if the directive was removed successfully, false otherwise
     */
    @Override
    public boolean removeDirective(@NotNull IPhpIniDirective directive) {
        return removeDirective(directive.getName());
    }

    /**
     * Remove a directive from the PHP INI file.
     *
     * @param name the name of the directive
     * @return true if the directive was removed successfully, false otherwise
     */
    @Override
    public boolean removeDirective(String name) {
        boolean result = false;

        if (containsDirective(name)) {
            for (IPhpIniSection section : ini) {
                for (IPhpIniDirective directive : section.getDirectives()) {
                    if (directive.getName().equalsIgnoreCase(name)) {
                        section.getDirectives().remove(directive);
                        result = true;
                        logger.info("Directive {} removed from section {} in PHP INI file", name, section.getName());
                        break;
                    }
                }

                if (result) {
                    break;
                }
            }
        } else {
            logger.warn(DIRECTIVE_NOT_FOUND_IN_PHP_INI_FILE, name);
        }

        return result;
    }

    /**
     * Get a directive by its name.
     *
     * @param name the name of the directive
     * @return the directive with the given name, or null if not found
     */
    @Override
    public IPhpIniDirective getDirective(String name) {
        IPhpIniDirective directive = null;
        boolean found = false;
        String sectionName = null;

        if (name != null && !name.isEmpty()) {
            for (IPhpIniSection section : ini) {
                directive = section.getDirectiveByName(name);
                if (directive != null) {
                    found = true;
                    sectionName = section.getName();
                    break;
                }
            }
        } else {
            logger.error("Directive name is null or empty");
        }

        if (!found) {
            logger.warn(DIRECTIVE_NOT_FOUND_IN_PHP_INI_FILE, name);
        } else {
            logger.info("Directive {} found in section {} in PHP INI file", name, sectionName);
        }

        return directive;
    }

    /**
     * Tries to save the PHP INI file.
     *
     * @return true if the file was saved successfully, false otherwise
     */
    @Override
    public boolean save() {
        boolean result = false;

        StringBuilder sb = new StringBuilder();

        for (IPhpIniSection section : ini) {
            sb.append("[").append(section.getName()).append("]").append(System.lineSeparator());

            List<IPhpIniDirective> directives = section.getDirectives();
            for (IPhpIniDirective directive : directives) {
                sb.append(directive.getContent());
                sb.append(System.lineSeparator());
            }
        }

        fileContent = sb.toString();

        try {
            if (innerPath != null && innerPath.toFile().canWrite()) {
                Files.writeString(innerPath, fileContent);
                result = true;
                logger.info("File saved successfully: {}", filePath);
            } else {
                logger.error("File is not writable: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error saving file: {}", filePath, e);
        }

        return result;
    }
}
