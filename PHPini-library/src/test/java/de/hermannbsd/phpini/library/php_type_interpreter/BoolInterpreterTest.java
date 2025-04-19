package de.hermannbsd.phpini.library.php_type_interpreter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolInterpreterTest {

    private BoolInterpreter boolInterpreter;

    @BeforeEach
    void setUp() {
        boolInterpreter = new BoolInterpreter();
        boolInterpreter.setStringValue("On");
    }

    @AfterEach
    void tearDown() {
        boolInterpreter = null;
    }

    @Test
    void getStringValue() {
        boolInterpreter = new BoolInterpreter("On");
        assertEquals("On", boolInterpreter.getStringValue(), "String value should be 'On'");
        boolInterpreter = new BoolInterpreter("Off");
        assertEquals("Off", boolInterpreter.getStringValue(), "String value should be 'Off'");
        boolInterpreter = new BoolInterpreter("1");
        assertEquals("1", boolInterpreter.getStringValue(), "String value should be '1'");
        boolInterpreter = new BoolInterpreter("0");
        assertEquals("0", boolInterpreter.getStringValue(), "String value should be '0'");
        boolInterpreter = new BoolInterpreter("true");
        assertEquals("true", boolInterpreter.getStringValue(), "String value should be 'true'");
        boolInterpreter = new BoolInterpreter("false");
        assertEquals("false", boolInterpreter.getStringValue(), "String value should be 'false'");
    }

    @Test
    void getBoolValue() {
        boolInterpreter = new BoolInterpreter(false);
        assertFalse(boolInterpreter.getBoolValue(), "Boolean value should be false");
        boolInterpreter = new BoolInterpreter(true);
        assertTrue(boolInterpreter.getBoolValue(), "Boolean value should be true");
    }

    @Test
    void testGetStringValue() {
        String stringValue = BoolInterpreter.getStringValue(false);
        assertEquals("Off", stringValue, "String value should be 'Off'");
        stringValue = BoolInterpreter.getStringValue(true);
        assertEquals("On", stringValue, "String value should be 'On'");
    }

    @Test
    void setStringValue() {
        boolInterpreter.setStringValue("Off");
        assertEquals("Off", boolInterpreter.getStringValue(), "String value should be 'Off'");
        boolInterpreter.setStringValue("On");
        assertEquals("On", boolInterpreter.getStringValue(), "String value should be 'On'");
        boolInterpreter.setStringValue("1");
        assertEquals("1", boolInterpreter.getStringValue(), "String value should be '1'");
        boolInterpreter.setStringValue("0");
        assertEquals("0", boolInterpreter.getStringValue(), "String value should be '0'");
        boolInterpreter.setStringValue("true");
        assertEquals("true", boolInterpreter.getStringValue(), "String value should be 'true'");
        boolInterpreter.setStringValue("false");
        assertEquals("false", boolInterpreter.getStringValue(), "String value should be 'false'");
    }

    @Test
    void isBoolValue() {
        boolean boolValue = BoolInterpreter.getBoolValue("On");
        assertTrue(boolValue, "Bool value should be true");
        boolValue = BoolInterpreter.getBoolValue("Off");
        assertFalse(boolValue, "Bool value should be false");
    }

    @Test
    void setBoolValue() {
        boolInterpreter.setBoolValue(false);
        assertFalse(boolInterpreter.getBoolValue(), "Bool value should be false");
        boolInterpreter.setBoolValue(true);
        assertTrue(boolInterpreter.getBoolValue(), "Bool value should be true");
    }
}