package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.hermannbsd.phpini.library.interfaces.IPhpIniSection;

import static org.junit.jupiter.api.Assertions.*;
class PhpIniSectionTest {

    public static final String TEST_SECTION = "TestSection";
    private IPhpIniSection phpIniSection;

    @BeforeEach
    void setUp() {
        phpIniSection = new PhpIniSection(TEST_SECTION);
    }

    @AfterEach
    void tearDown() {
        phpIniSection = null;
    }

    @Test
    void getName() {
        String actualName = phpIniSection.getName();
        assertEquals(TEST_SECTION, actualName, "Section name should match the expected value");
    }

    @Test
    void getDirectives() {
        assertNotNull(phpIniSection.getDirectives(), "Directives list should not be null");
        assertTrue(phpIniSection.getDirectives().isEmpty(), "Directives list should be empty initially");
    }

    @Test
    void getDirectiveByName() {
        String directiveName = "nonexistent_directive";
        assertNull(phpIniSection.getDirectiveByName(directiveName), "Should return null for nonexistent directive");

        // Add a directive and test again
        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue(directiveName, "value");
        phpIniSection.tryAddDirective(directive);
        assertNotNull(phpIniSection.getDirectiveByName(directiveName), "Should return the directive after adding it");
    }

    @Test
    void tryAddDirective() {
        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue("test_directive", "value");
        assertTrue(phpIniSection.tryAddDirective(directive), "Should return true when adding a new directive");
        assertEquals(1, phpIniSection.getDirectives().size(), "Directives list should contain one directive after adding");
    }

    @Test
    void tryUpdateDirective() {
        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue("test_directive", "value");
        phpIniSection.tryAddDirective(directive);

        // Update the directive
        directive.setValue("new_value");
        assertTrue(phpIniSection.tryUpdateDirective(directive), "Should return true when updating an existing directive");

        // Check if the value is updated
        assertEquals("new_value", phpIniSection.getDirectiveByName("test_directive").getValue(), "Directive value should be updated");
    }

    @Test
    void tryRemoveDirective() {
        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue("test_directive", "value");
        phpIniSection.tryAddDirective(directive);

        assertTrue(phpIniSection.tryRemoveDirective(directive), "Should return true when removing an existing directive");
        assertEquals(0, phpIniSection.getDirectives().size(), "Directives list should be empty after removing the directive");
    }

    @Test
    void testTryRemoveDirective() {
        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue("test_directive", "value");
        phpIniSection.tryAddDirective(directive);

        assertTrue(phpIniSection.tryRemoveDirective("test_directive"), "Should return true when removing an existing directive by name");
        assertEquals(0, phpIniSection.getDirectives().size(), "Directives list should be empty after removing the directive");
    }

    @Test
    void containsDirectiveByName() {
        String directiveName = "test_directive";
        assertFalse(phpIniSection.containsDirectiveByName(directiveName), "Should return false for nonexistent directive");

        IPhpIniDirective directive = PhpIniDirective.getDirectiveByNameAndSetValue(directiveName, "value");
        phpIniSection.tryAddDirective(directive);
        assertTrue(phpIniSection.containsDirectiveByName(directiveName), "Should return true for existing directive");
    }

    @Test
    void testToString() {
        String expected = "Section: " + TEST_SECTION + "\n";
        String actualString = phpIniSection.toString();
        assertEquals(expected, actualString, "toString() should return the expected string representation");
    }
}