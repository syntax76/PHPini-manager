package de.hermannbsd.phpini.library.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for a PHP INI section.
 */
public interface IPhpIniSection {

    /**
     * Get the name of the section.
     *
     * @return the name of the section as a String
     */
    String getName();

    /**
     * Get the directives in the section.
     *
     * @return a list of directives in the section
     */
    List<IPhpIniDirective> getDirectives();

    /**
     * Tries to get a directive by its name.
     * @param directiveName the name of the directive to search for
     * @return the directive if found, null otherwise
     */
    @Nullable IPhpIniDirective getDirectiveByName(String directiveName);

    /**
     * Tries to add a directive to the section.
     * @param directive the directive to add
     * @return true if the directive was added, false otherwise
     */
    boolean tryAddDirective(@NotNull IPhpIniDirective directive);

    /**
     * Tries to update a directive from the section.
     * If the directive is not found, it will be added.
     *
     * @param directive the directive to update
     * @return true if the directive was updated, false otherwise
     */
    boolean tryUpdateDirective(@NotNull IPhpIniDirective directive);

    /**
     * Tries to remove a directive from the section.
     * @param directive the directive to remove
     * @return true if the directive was removed, false otherwise
     */
    boolean tryRemoveDirective(@NotNull IPhpIniDirective directive);

    /**
     * Tries to remove a directive from the section.
     * @param directiveName the name of the directive to remove
     * @return true if the directive was removed, false otherwise
     */
    boolean tryRemoveDirective(@NotNull String directiveName);

    /**
     * Gets whether the section contains a directive with the given name.
     * @param directiveName the given name
     * @return is there a directive with the given name?
     */
    boolean containsDirectiveByName(@NotNull String directiveName);
}
