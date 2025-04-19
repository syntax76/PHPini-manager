package de.hermannbsd.phpini.library.annotations;
/**
 * Annotation to describe an enum value.
 * <p>This annotation can be used to provide a description for enum values in PHP INI directives.
 * It can be used to provide additional information about the enum value.</p>
 *
 * @author Aleandra Hermann
 * @version 1.0
 * @since 1.0
 */
public @interface EnumDescription {
    /**
     * The description of the enum value.
     *
     * @return the description of the enum value
     */
    String value();

    /**
     * The name of the enum value.
     *
     * @return the name of the enum value
     */
    String name() default "";
}
