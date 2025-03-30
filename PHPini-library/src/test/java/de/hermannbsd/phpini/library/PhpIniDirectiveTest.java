package de.hermannbsd.phpini.library;

import de.hermannbsd.phpini.library.interfaces.IPhpIniDirective;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhpIniDirectiveTest {

    private static IPhpIniDirective directive;

    @BeforeEach
    void setUp() {
        directive = new PhpIniDirective("allow_url_fopen = 0");
    }

    @AfterEach
    void tearDown() {
        directive = null;
    }

    @Test
    void getName() {
        assertEquals("allow_url_fopen", directive.getName());
    }

    @Test
    void setValue() {
        directive.setValue("1");
        assertEquals("1", directive.getValue());
    }

    @Test
    void getValue() {
        assertEquals("0", directive.getValue());
    }

    @Test
    void getDefaultValue() {
        assertEquals("1", directive.getDefaultValue());
    }

    @Test
    void getChangeable() {
        assertEquals(DirectiveChangeable.INI_SYSTEM, directive.getChangeable());
    }

    @Test
    void getDescription() {
        String description = "This option enables the URL-aware fopen wrappers that enable accessing URL object like " +
                "files. Default wrappers are provided for the access of remote files using the ftp or http protocol, " +
                "some extensions like zlib may register additional wrappers.";
        assertEquals(description, directive.getDescription());
    }

    @Test
    void getChangelog() {
        assertEquals("", directive.getChangelog());
    }

    @Test
    void getSection() {
        assertEquals("", directive.getSection());
    }

    @Test
    void testToString() {
        assertEquals("allow_url_fopen = 0", directive.toString());
    }
}