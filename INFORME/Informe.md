# Informe del Proyecto de Cifrado/Descifrado de Archivos

## Introducción
El proyecto consiste en un cifrador/descifrador de archivos que utiliza el algoritmo de cifrado AES y el algoritmo de derivación de clave PBKDF2 para garantizar la seguridad de los archivos. El programa permite cifrar un archivo con una contraseña proporcionada y luego descifrarlo usando la misma contraseña, verificando la integridad del proceso mediante el uso de hash SHA-256.

## Desarrollo del Programa
El programa está desarrollado en Java y utiliza varias clases para realizar las operaciones de cifrado, descifrado y manejo de interfaz gráfica. A continuación, se describen las clases principales:

### Clase CipherManager
La clase `CipherManager` gestiona las operaciones de cifrado y descifrado de archivos. Utiliza las clases `AESManager`, `PBKDF2Manager`, y `SHA256Manager` para realizar las operaciones criptográficas necesarias.

#### Cifrado de Archivo
- Lee el contenido del archivo.
- Genera un salt y una clave utilizando PBKDF2.
- Calcula el hash SHA-256 del archivo sin cifrar.
- Genera un IV para AES.
- Cifra el archivo utilizando AES.
- Combina el IV, el archivo cifrado, el salt y el hash.
- Escribe el archivo combinado.

#### Descifrado de Archivo
- Lee el archivo combinado.
- Separa el IV, el archivo cifrado, el salt y el hash.
- Genera la clave utilizando PBKDF2 con el salt almacenado.
- Descifra el archivo utilizando AES.
- Calcula el hash SHA-256 del contenido descifrado.
- Verifica la integridad comparando el hash almacenado con el hash calculado.

### Clase AESManager
La clase `AESManager` proporciona métodos para cifrar y descifrar utilizando el algoritmo AES en modo CBC.

### Clase PBKDF2Manager
La clase `PBKDF2Manager` maneja la generación de claves utilizando el algoritmo PBKDF2 con HMAC-SHA256.

### Clase SHA256Manager
La clase `SHA256Manager` calcula el hash SHA-256 de una cadena de datos.

### Clase MainUI
La clase `MainUI` implementa la interfaz gráfica del programa utilizando la biblioteca Swing de Java. Permite al usuario seleccionar un archivo, ingresar una contraseña y elegir entre las opciones de cifrado y descifrado.

## Conclusiones
El proyecto ha logrado desarrollar un cifrador/descifrador de archivos que utiliza algoritmos de cifrado y derivación de clave robustos para garantizar la seguridad. La implementación de la interfaz gráfica proporciona una experiencia de usuario amigable. Se recomienda mejorar la gestión de excepciones y la validación de entrada para hacer el programa más robusto y seguro.

## Repositorio en GitHub
El código fuente se encuentra disponible en el siguiente repositorio de GitHub: [Enlace al Repositorio](url_del_repositorio)
