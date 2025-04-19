package de.hermannbsd.phpini.library.helper;

import de.hermannbsd.phpini.library.IniLine;
import de.hermannbsd.phpini.library.interfaces.IIniLine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for string operations.
 * This class provides utility methods for string manipulation.
 */
public class StringHelper {

    private StringHelper() {
        // Prevent instantiation
    }

    /**
     * Check if a given string has an even number of quotation marks. If not, append a quotation mark.
     *
     * @param value the string to check
     * @return the modified string with an even number of quotation marks
     */
    public static @NotNull String secureEqualQuotationMarks(String value) {
        StringBuilder sb = new StringBuilder(value);
        if (value.contains("\"") && counts(value, '"') % 2 == 1) {
            sb.append("\"");
        }

        return sb.toString();
    }

    /**
     * Count the occurrences of a character in a string.
     *
     * @param value the string to search in
     * @param search the character to count
     * @return the number of occurrences of the character in the string
     */
    @Contract(pure = true)
    public static int counts(@NotNull String value, char search) {
        int count = 0;

        char[] chars = value.toCharArray();

        for (char c : chars) {
            if (c == search) {
                count++;
            }
        }

        return count;
    }

    public static @NotNull IIniLine splitPhpIniLine(String line) {
        IniLine result = new IniLine(line);

        if (line != null && !line.isEmpty()) {
            String[] parts = line.split("=");
            if (parts.length > 0) {
                if (parts[0].contains(";")) {
                    String[] subParts = parts[0].split(";");
                    result.setComment(subParts[1].trim());
                } else {
                    result.setDirectiveName(parts[0].trim());
                }
            }
            if (parts.length > 1) {
                String secondPart = parts[1].trim();
                handleSecondPart(secondPart, result);
            }
        }

        return result;
    }

    private static void handleSecondPart(String secondPart, IniLine result) {
        int count = counts(secondPart, '"');
        int idx1;
        int idx3;
        String value;
        String comment = null;

        switch (count) {
            case 0 -> {
                idx3 = secondPart.indexOf(';');
                if (idx3 > -1) {
                    value = secondPart.substring(0, idx3).trim();
                    comment = searchComment(secondPart.substring(idx3));
                } else {
                    value = secondPart.trim();
                }
            }
            case 1 -> {
                idx1 = secondPart.indexOf('"');
                idx3 = secondPart.indexOf(';');

                if (idx3 > -1) {
                    value = secondPart.substring(0, idx3).trim();
                    comment = searchComment(secondPart.substring(idx3));
                } else {
                    value = secondPart.substring(0, idx1 + 1).trim();
                }
            }
            case 2 -> {
                int idx2;

                idx1 = secondPart.indexOf('"');
                idx2 = secondPart.indexOf('"', idx1 + 1);
                idx3 = secondPart.indexOf(';');

                if (idx3 < idx2 || idx3 > idx2) {
                    value = secondPart.substring(idx1, idx2 + 1).trim();
                    comment = searchComment(secondPart.substring(idx2 + 1));
                } else {
                    value = secondPart.substring(0, idx3).trim();
                    comment = searchComment(secondPart.substring(idx3));
                }
            }
            default -> {
                value = secondPart.substring(0, secondPart.lastIndexOf('"') + 1).trim();
                comment = searchComment(secondPart);
            }
        }

        result.setValue(value);

        if (comment != null) {
            result.setComment(comment);
        }
    }

    private static String searchComment(@NotNull String secondPart) {
        int idx = secondPart.indexOf(';');
        String comment = null;
        if (idx > -1) {
            comment = secondPart.substring(idx+1).trim();
        }

        return comment;
    }
}
