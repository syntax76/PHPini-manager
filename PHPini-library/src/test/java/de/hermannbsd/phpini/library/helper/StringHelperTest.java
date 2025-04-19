package de.hermannbsd.phpini.library.helper;

import de.hermannbsd.phpini.library.interfaces.IIniLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTest {

    @Test
    void secureEqualQuotationMarks() {
        String s1 = "\".;/etc/php/;/usr/local/etc/php/\"";
        String s2 = "\".";

        String s3 = StringHelper.secureEqualQuotationMarks(s1);

        assertEquals(s1, s3, "String should be unchanged");

        s3 = StringHelper.secureEqualQuotationMarks(s2);
        assertNotEquals(s2, s3, "String should be changed");
    }

    @Test
    void counts() {
        String s1 = ".1.2.3,4";
        int count = StringHelper.counts(s1, '.');
        int m = count % 2;

        assertEquals(3, count, "Count of '.' should be 3");
        assertEquals(1, m, "Count of '.' should be odd");

        count = StringHelper.counts(s1, ',');
        m = count % 2;

        assertEquals(1, count, "Count of ',' should be 1");
        assertEquals(1, m, "Count of ',' should be odd");

        s1 = "1.2,3.4";
        count = StringHelper.counts(s1, '.');
        m = count % 2;
        assertEquals(2, count, "Count of '.' should be 2");
        assertEquals(0, m, "Count of '.' should be even");

        count = StringHelper.counts(s1, ',');
        m = count % 2;
        assertEquals(1, count, "Count of ',' should be 1");
        assertEquals(1, m, "Count of ',' should be odd");
    }

    @Test
    void splitPhpIniLine() {
        String s1 = "path = \".;/etc/php/;/usr/local/etc/php/\" ; declaration of path";
        String s2 = "allow_url_fopen = 0 ; should it be allowed to open URL";
        String s3 = "; only comment";
        IIniLine r1 = StringHelper.splitPhpIniLine(s1);
        IIniLine r2 = StringHelper.splitPhpIniLine(s2);
        IIniLine r3 = StringHelper.splitPhpIniLine(s3);

        assertNotNull(r1, "Result should not be null");
        assertEquals("path", r1.getDirectiveName(), "Name should be 'path'");
        assertEquals("\".;/etc/php/;/usr/local/etc/php/\"", r1.getValue(), "Value should be \".;/etc/php/;/usr/local/etc/php/\"");
        assertEquals("declaration of path", r1.getComment(), "Comment should be 'declaration of path'");
        assertTrue(r1.isCommented(), "Line should be commented");
        assertFalse(r1.isCommentOnly(), "Line should not be comment only");

        assertNotNull(r2, "Result should not be null");
        assertEquals("allow_url_fopen", r2.getDirectiveName(), "Name should be 'allow_url_fopen'");
        assertEquals("0", r2.getValue(), "Value should be '0'");
        assertEquals("should it be allowed to open URL", r2.getComment(), "Comment should be 'should it be allowed to open URL'");
        assertTrue(r2.isCommented(), "Line should be commented");
        assertFalse(r2.isCommentOnly(), "Line should not be comment only");

        assertNotNull(r3, "Result should not be null");
        assertEquals("", r3.getDirectiveName(), "Name should be empty");
        assertEquals("", r3.getValue(), "Value should be empty");
        assertEquals("only comment", r3.getComment(), "Comment should be 'only comment'");
        assertTrue(r3.isCommented(), "Line should be commented");
        assertTrue(r3.isCommentOnly(), "Line should be comment only");

    }
}