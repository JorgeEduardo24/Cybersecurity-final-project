
package model;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class CipherManager {
    private static final int SALT_ADN_HASH_LENGTH = 64;

    public void encryptFile(String filePath, String password) {
        try {
            byte[] fileData = Files.readAllBytes(Path.of(filePath));

            // Generar salt y clave con PBKDF2
            String salt = PBKDF2Manager.generateSalt();
            SecretKey key = PBKDF2Manager.generateKey(password, salt.getBytes());
            String hash = SHA256Manager.calculateHash(new String(fileData));
            byte[] iv = AESManager.generateIV();
            byte[] encryptedData = AESManager.encrypt(fileData, key, iv);
            
            // Combina el IV, el archivo encriptado, el salt y el hash
            byte[] combinedData = new byte[iv.length + encryptedData.length + salt.getBytes().length + hash.getBytes().length];
            System.arraycopy(iv, 0, combinedData, 0, iv.length);
            System.arraycopy(encryptedData, 0, combinedData, iv.length, encryptedData.length);
            System.arraycopy(salt.getBytes(), 0, combinedData, iv.length + encryptedData.length, salt.getBytes().length);
            System.arraycopy(hash.getBytes(), 0, combinedData, iv.length + encryptedData.length + salt.getBytes().length, hash.getBytes().length);

            // Escribe el archivo combinado
            Files.write(Path.of(filePath), combinedData, StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptFile(String filePath, String password) {
        try {
            byte[] fileData = Files.readAllBytes(Path.of(filePath));
            int hashLength = SALT_ADN_HASH_LENGTH;
            int ivLength = 16;  // Tamaño del IV para AES en modo CBC

            // Separar el archivo cifrado, el IV, el salt y el hash
            byte[] iv = Arrays.copyOfRange(fileData, 0, ivLength);
            byte[] encryptedData = Arrays.copyOfRange(fileData, ivLength, fileData.length - SALT_ADN_HASH_LENGTH - hashLength);
            byte[] saltBytes = Arrays.copyOfRange(fileData, fileData.length - SALT_ADN_HASH_LENGTH - hashLength, fileData.length - hashLength);
            byte[] storedHash = Arrays.copyOfRange(fileData, fileData.length - hashLength, fileData.length);
            
            // Generar clave con PBKDF2 utilizando la sal almacenada
            SecretKey key = PBKDF2Manager.generateKey(password, saltBytes);            
            // Calcular el hash del contenido desencriptado
            byte[] decryptedData = AESManager.decrypt(encryptedData, key, iv);
            String calculatedHash = SHA256Manager.calculateHash(new String(decryptedData));
            
            Path pathEncriptedFile = Paths.get(filePath);
            if (Files.exists(pathEncriptedFile)) {
                Files.delete(pathEncriptedFile);
            }else{
                JOptionPane.showMessageDialog(null, "No se encuentra el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            // Verificar si el hash almacenado coincide con el hash calculado
            if (Arrays.equals(storedHash, calculatedHash.getBytes())) {
                Files.write(Path.of(filePath), decryptedData, StandardOpenOption.CREATE);
                JOptionPane.showMessageDialog(null, "Descifrado completado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error: El hash almacenado no coincide.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
