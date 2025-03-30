package de.hermannbsd.phpini.library;

/**
 * Enum representing in which kind directive in PHP INI files are changeable.
 * This enum is used to define how a directive can be changed in the PHP INI files.
 */
public enum DirectiveChangeable {

    /**
     * Entry can be set in user scripts (like with ini_set()) or in the Windows registry.
     * Entry can be set in .user.ini
     */
    INI_USER,
    /**
     * Entry can be set in php.ini or httpd.conf
     */
    INI_SYSTEM,
    /**
     * Entry can be set in php.ini, .htaccess, httpd.conf or .user.ini
     */
    INI_PERDIR,
    /**
     * Entry can be set anywhere
     */
    INI_ALL,
    /**
     * Entry can only be set in php.ini
     */
    PHP_INI_ONLY,
}
