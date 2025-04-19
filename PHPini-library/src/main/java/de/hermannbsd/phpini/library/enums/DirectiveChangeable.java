package de.hermannbsd.phpini.library.enums;

import de.hermannbsd.phpini.library.annotations.PhpIniDirectiveAnnotation;

/// Enum representing in which kind directive in PHP INI files are changeable.
///
/// This enum is used to define how a directive can be changed in the PHP INI files.
///
/// @author Aleandra Hermann
/// @version 1.0
/// @since 1.0
public enum DirectiveChangeable {

    /// Entry can be set anywhere
    @PhpIniDirectiveAnnotation(name = "INI_ALL", allowedFileNames = "*")
    INI_ALL,
    /// Entry can be set in php.ini, .htaccess, httpd.conf or .user.ini
    @PhpIniDirectiveAnnotation(name = "INI_PERDIR", allowedFileNames = "php.ini,.htaccess,httpd.conf,.user.ini")
    INI_PERDIR,
    /// Entry can be set in php.ini or httpd.conf
    @PhpIniDirectiveAnnotation(name = "INI_SYSTEM", allowedFileNames = "php.ini,httpd.conf")
    INI_SYSTEM,
    /// Entry can be set in user scripts (like with ini_set()) or in the Windows registry.
    ///
    /// Entry can be set in .user.ini
    @PhpIniDirectiveAnnotation(name = "INI_USER", allowedFileNames = ".user.ini")
    INI_USER,
    /// Entry can only be set in php.ini
    @PhpIniDirectiveAnnotation(name = "PHP_INI_ONLY", allowedFileNames = "php.ini")
    PHP_INI_ONLY,
}
