package de.hermannbsd.phpini.library.php_type_interpreter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Class to interpret boolean values in PHP INI files.
 */
public class BoolInterpreter {

    /**
     * The string value, which should be interpreted as a boolean.
     */
    private String stringValue;

    /**
     * The boolean value, which should be interpreted as a string.
     */
    private boolean boolValue;

    /**
     * Constructor to create a BoolInterpreter instance.
     */
    public BoolInterpreter() {
        this.stringValue = "";
        this.boolValue = false;
    }

    /**
     * Constructor to create a BoolInterpreter instance with a string value.
     *
     * @param stringValue the string value to be interpreted as a boolean
     */
    public BoolInterpreter(String stringValue) {
        this.stringValue = stringValue;
        this.boolValue = getBoolValue(stringValue);
    }

    /**
     * Constructor to create a BoolInterpreter instance with a boolean value.
     *
     * @param boolValue the boolean value to be interpreted as a string
     */
    public BoolInterpreter(boolean boolValue) {
        this.stringValue = getStringValue(boolValue);
        this.boolValue = boolValue;
    }

    /**
     * Interprets the given boolean value as a string.
     *
     * @param boolValue the given boolean value
     * @return the interpreted result
     */
    @Contract(pure = true)
    public static @NotNull String getStringValue(boolean boolValue) {
        return boolValue ? "On" : "Off";
    }

    /**
     * Interprets the given string value as a boolean.
     *
     * @param stringValue the given string value
     * @return the interpreted result
     */
    @Contract(pure = true)
    public static boolean getBoolValue(@NotNull String stringValue) {

        return switch (stringValue.toLowerCase()) {
            case "on", "1", "true" -> true;
            default -> false;
        };
    }

    /**
     * Get the string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Set the string value.
     *
     * @param stringValue the string value to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
        this.boolValue = getBoolValue(stringValue);
    }

    /**
     * Get the boolean value.
     *
     * @return the boolean value
     */
    public boolean getBoolValue() {
        return boolValue;
    }

    /**
     * Set the boolean value.
     *
     * @param boolValue the boolean value to set
     */
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
        this.stringValue = getStringValue(boolValue);
    }
}
