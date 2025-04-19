package de.hermannbsd.phpini.library.annotations;

/**
 * Annotation to describe a PHP INI directive.
 * <p>This annotation can be used to provide additional information about the directive.</p>
 * <p>Additionally it sets the range where the Directive can be used</p>
 *
 * @author Aleandra Hermann
 * @version 1.0
 * @since 1.0
 */
public @interface PhpIniDirectiveAnnotation {
    /**
     * The name of the directive.
     *
     * @return the name of the directive
     */
    String name();

    /**
     * Gets the list of allowed filenames, where the directive can be used.
     * <p>The list is comma seperated</p>
     *
     * @return The list of allowed filenames, where the directive can be used
     */
    String allowedFileNames();
}
