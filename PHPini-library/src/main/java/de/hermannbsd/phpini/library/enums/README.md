# Enums

The enumerations in this package.

## Current Enums

### DirectiveChangeable
The areas in which a directive can be changed.

These modes determine when and where a PHP directive may or may not be set, and each
directive within the manual refers to one of these modes. For example, some settings may
be set within a PHP script using `ini_set()`, whereas others may require `php.ini` or
`httpd.conf`.

For example, the output_buffering setting is `INI_PERDIR` therefore it may not be set
using `ini_set()`. However, the `display_errors` directive is `INI_ALL` therefore it may
be set anywhere, including with `ini_set()`.

- `INI_ALL` - Entry can be set anywhere.
- `INI_PERDIR` - Entry can be set in `php.ini`, `.htaccess`, `httpd.conf` or `.user.ini`
- `INI_SYSTEM` - Entry can be set in `php.ini` or `httpd.conf`
- `INI_USER` - Entry can be set in user scripts (like with ini_set()) or in the Windows registry. Entry can be set in `.user.ini`
- `PHP_INI_ONLY` - Entry can only be set in `php.ini`