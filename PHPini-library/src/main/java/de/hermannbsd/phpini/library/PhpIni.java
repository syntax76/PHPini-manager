package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIni;
import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

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

    private final boolean isCreated;

    /**
     * The list of directives in the PHP INI file.
     */
    private List<IPhpIniDirective> directives;

    /**
     * Constructor for the PhpIni class.
     *
     * @param filePath the file path to the PHP INI file
     * @exception IOException if the file path is invalid or cannot be created
     */
    public PhpIni(String filePath) throws IOException {
        this.filePath = filePath;

        if (filePath != null && Paths.get(filePath).toFile().exists()) {
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

        this.directives = new ArrayList<>();
    }

    private void initDirectives() {
        if (!isCreated && fileContent != null && !fileContent.isEmpty()) {
            try {
                List<String> lines = Files.readAllLines(innerPath);
                for (String line : lines) {
                    IPhpIniDirective directive = new PhpIniDirective(line);

                    if (directive.getName() != null && !directive.getName().isEmpty()) {
                        directives.add(directive);
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
     * Get the directives of the PHP INI file.
     *
     * @return a list of IPhpIniDirective objects representing the directives
     */
    @Override
    public List<IPhpIniDirective> getDirectives() {
        return List.of();
    }

    /**
     * Adds a given directive to the PHP INI file.
     *
     * @param directive the directive to add
     * @return true if the directive was added successfully, false otherwise
     */
    @Override
    public boolean addDirective(IPhpIniDirective directive) {
        return false;
    }

    /**
     * Adds a directive to the PHP INI file.
     *
     * @param name  the name of the directive
     * @param value the value of the directive
     * @return true if the directive was added successfully, false otherwise
     */
    @Override
    public boolean addDirective(String name, String value) {
        return false;
    }

    /**
     * Adds a directive to the PHP INI file.
     *
     * @param name    the name of the directive
     * @param value   the value of the directive
     * @param section the section of the directive
     * @return true if the directive was added successfully, false otherwise
     */
    @Override
    public boolean addDirective(String name, String value, String section) {
        return false;
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
        return false;
    }

    /**
     * Remove a directive from the PHP INI file.
     *
     * @param directive the directive to update
     * @return true if the directive was removed successfully, false otherwise
     */
    @Override
    public boolean removeDirective(IPhpIniDirective directive) {
        return false;
    }

    /**
     * Remove a directive from the PHP INI file.
     *
     * @param name the name of the directive
     * @return true if the directive was removed successfully, false otherwise
     */
    @Override
    public boolean removeDirective(String name) {
        return false;
    }

    /**
     * Get a directive by its name.
     *
     * @param name the name of the directive
     * @return the directive with the given name, or null if not found
     */
    @Override
    public IPhpIniDirective getDirective(String name) {
        return null;
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

        for (IPhpIniDirective directive : directives) {
            sb.append(directive.toString());
            sb.append(System.lineSeparator());
        }

        fileContent = sb.toString();

        try {
            if (innerPath != null && innerPath.toFile().canWrite()) {
                Files.writeString(innerPath, fileContent);
                result = true;
            } else {
                logger.error("File is not writable: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error saving file: {}", filePath, e);
        }
        return result;
    }
}
