
package model;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JOptionPane;

public class AESManager {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(byte[] data, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            return cipher.doFinal(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el cifrado AES", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static byte[] decrypt(byte[] encryptedData, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el descifrado AES", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[16];  // Tamaño del IV para AES en modo CBC
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}