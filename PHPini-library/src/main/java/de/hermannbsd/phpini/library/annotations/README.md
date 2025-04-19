# Annotations
Annotations are used to mark classes, methods, and fields with metadata that can be 
used by the PHP INI library to provide additional functionality or behavior. 
Annotations are defined using the `@interface` keyword and can be applied to various 
elements in the code.

## Current Annotations
### EnumDescription
The `EnumDescription` annotation is used to provide a description for an enumeration
value. It can be applied to enumeration constants to provide additional information
about the value.

```java
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

```

### PhpIniDirectiveAnnotation
The `PhpIniDirectiveAnnotation` annotation is used to mark a class as a PHP INI 
directive.
It can be applied to classes that represent PHP INI directives and provides metadata
about the directive, such as its name, default value, and whether it is changeable.

```java
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
```

