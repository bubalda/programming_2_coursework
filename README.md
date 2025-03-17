# Java Programming
Coursework 2 for Programming II (COMP-1322)\
Online Shopping Platform Program by Mah Han Cheng

## Features
+ [User Login](#logging-in)
+ [Register](#register)

## Logging In
1. Requires username and password already in database
2. Uses salt and PBKDF2WithHmacSHA1 to encrypt user password to prevent rainbow table attack

## Register
1. All users should not have the same username
2. All fields should be field
3. Will add some more protective measures (cloudflare, email auth, etc. afterwards if I have time, otherwise this will be in unused for future work)

## Code Language and Version
- Java version: 23
- JavaFX version: 17.0.6
- Maven version: 4.0.0
- SQLite JDBC version: 3.49.1.0 (maven - org.xerial:sqlite-jdbc:3.49.1.0)
- Ikonli JavaFX version: 3.49.1.0 (maven - org.kordamp.ikonli:ikonli-javafx:12.3.1)
- Ikonli Core version: 3.49.1.0 (maven - org.kordamp.ikonli:ikonli-core:12.3.1)

### Ikonli icons
- Bootstrap Icons Pack (maven - org.kordamp.ikonli:ikonli-bootstrapicons-pack:12.3.1)

## Setup
### To run this project:
1. 

## Author
- [Mah Han Cheng (36271632)](mailto:hcm1e24@soton.ac.uk)

## References
[Maven Repository](https://mvnrepository.com/)