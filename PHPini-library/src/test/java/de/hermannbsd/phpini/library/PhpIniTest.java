package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIni;
import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.io.IOException;

import static de.hermannbsd.phpini.library.PhpIniDirective.getDirectiveByNameAndSetValue;
import static org.junit.jupiter.api.Assertions.*;

class PhpIniTest {

    private IPhpIni phpIni;
    private static final String PHP_INI_FILE = "php.ini";
    private static final Logger logger = LoggerFactory.getLogger(PhpIniTest.class);

    @BeforeEach
    void setUp() {
        logger.info("Setting up PhpIniTest...");
        URL phpIniFile = getClass().getClassLoader().getResource(PHP_INI_FILE);

        if (phpIniFile != null) {
            String filePath = phpIniFile.getPath();
            try {
                phpIni = new PhpIni(filePath);
            } catch (IOException e) {
                fail("Failed to create PhpIni instance: " + e.getMessage());
            }
        } else {
            fail("PHP INI file not found in classpath");
        }
    }

    @AfterEach
    void tearDown() {
        phpIni = null;
        logger.info("Tearing down PhpIniTest...");
    }

    @Test
    void getFilePath() {
        String expectedFilePath = phpIni.getFilePath();
        assertNotNull(expectedFilePath, "File path should not be null");
        assertTrue(expectedFilePath.endsWith(PHP_INI_FILE), "File path should end with " + PHP_INI_FILE);
    }

    @Test
    void getFileName() {
        String expectedFileName = phpIni.getFileName();
        assertNotNull(expectedFileName, "File name should not be null");
        assertEquals(PHP_INI_FILE, expectedFileName, "File name should be " + PHP_INI_FILE);
    }

    @Test
    void getFileNameWithoutExtension() {
        assertEquals("php", phpIni.getFileNameWithoutExtension(), "File name without extension should be 'php'");
    }

    @Test
    void getFileExtension() {
        assertEquals("ini", phpIni.getFileExtension(), "File extension should be 'ini'");
    }

    @Test
    void getFileContent() {
        String fileContent = phpIni.getFileContent();
        assertNotNull(fileContent, "File content should not be null");
        assertFalse(fileContent.isEmpty(), "File content should not be empty");
    }

    @Test
    void updateDirective() {
        String directiveName = "auto_globals_jit";
        String directiveValue = "Off";

        phpIni.addDirective(getDirectiveByNameAndSetValue(directiveName, directiveValue));

        String newValue = "On";
        phpIni.updateDirective(directiveName, newValue);

        assertEquals("true", phpIni.getDirective(directiveName).getValue(), "Updated directive value should match");
    }

    @Test
    void removeDirective() {
        String directiveName = "test_directive";
        String directiveValue = "test_value";

        IPhpIniDirective directive = getDirectiveByNameAndSetValue(directiveName, directiveValue);

        if (phpIni.addDirective(directive)) {
            assertTrue(phpIni.removeDirective(directive), "Directive should be removed");
        }

        assertNull(phpIni.getDirective(directiveName), "Directive should be removed");
    }

    @Test
    void testRemoveDirective() {
        String directiveName = "test_directive";
        String directiveValue = "test_value";

        IPhpIniDirective directive = getDirectiveByNameAndSetValue(directiveName, directiveValue);

        phpIni.addDirective(directive);

        phpIni.removeDirective(directive);

        assertNull(phpIni.getDirective(directiveName), "Directive should be removed");
    }

    @Test
    void getDirective() {
        String directiveName = "short_open_tag";
        String directiveValue = "Off";

        if (phpIni.containsDirective(directiveName)) {
            IPhpIniDirective directive = phpIni.getDirective(directiveName);

            assertNotNull(directive, "Directive should be found");
            assertEquals(directiveValue, directive.getValue(), "Directive value should match");
        } else {
            fail("Failed to add directive");
        }
    }

    @Test
    void save() {
        try {
            phpIni.save();
        } catch (Exception e) {
            fail("Failed to save PHP INI file: " + e.getMessage());
        }

        // Verify that the file was saved correctly
        String fileContent = phpIni.getFileContent();
        assertNotNull(fileContent, "File content should not be null after saving");
        assertFalse(fileContent.isEmpty(), "File content should not be empty after saving");
    }
}