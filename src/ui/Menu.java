package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.KeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Arrays;

public class Menu{

    public void showMenu() {
        System.out.println("\nIngrese la opción que desee realizar: ");
        System.out.println("[1] Cifrar archivo.\n"+
                           "[2] Descifrar archivo\n"+
                           "[3] Salir del programa.\n");
    }
    
    public int readOption(BufferedReader br) throws NumberFormatException, IOException {
        int option = Integer.parseInt(br.readLine());
        return option;
    }
    
    public void doOption(int option, BufferedReader br) throws NumberFormatException, IOException {
        switch(option) {
        
        case 1:
            System.out.println("-----------------------------------------------------");
            System.out.println("                 CIFRAR ARCHIVO");
            System.out.println("-----------------------------------------------------");
            System.out.println("Ingrese la ruta del archivo a cifrar: ");
            String inputFile = br.readLine();
            System.out.println("Ingrese la contraseña: ");
            String password = br.readLine();
            System.out.println("Ingrese la ruta del archivo cifrado de salida: ");
            String outputFile = br.readLine();
            
            try {
                byte[] salt = new byte[16];
                SecureRandom random = new SecureRandom();
                random.nextBytes(salt);
                
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
                SecretKey tmp = factory.generateSecret(spec);
                SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
                
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                
                byte[] iv = cipher.getIV();
                byte[] encryptedFile = cipher.doFinal(Files.readAllBytes(Paths.get(inputFile)));
                
                // Calcular el hash SHA-256 del archivo sin cifrar
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedInputFile = md.digest(Files.readAllBytes(Paths.get(inputFile)));
                
                // Escribir el archivo cifrado y el hash en el archivo de salida
                Files.write(Paths.get(outputFile), salt, StandardOpenOption.CREATE);
                Files.write(Paths.get(outputFile), iv, StandardOpenOption.APPEND);
                Files.write(Paths.get(outputFile), encryptedFile, StandardOpenOption.APPEND);
                Files.write(Paths.get(outputFile), hashedInputFile, StandardOpenOption.APPEND);
                
                System.out.println("Archivo cifrado exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al cifrar el archivo: " + e.getMessage());
            }
            
            break;
                
        case 2:
            System.out.println("-----------------------------------------------------");
            System.out.println("               DESCIFRAR ARCHIVO");
            System.out.println("-----------------------------------------------------");
            System.out.println("Ingrese la ruta del archivo cifrado: ");
            String encryptedFile = br.readLine();
            System.out.println("Ingrese la contraseña: ");
            String passwordd = br.readLine();
            System.out.println("Ingrese la ruta del archivo descifrado de salida: ");
            String outputFilex = br.readLine();

            try {
                byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedFile));

                // Extraer la sal y el IV del archivo cifrado
                byte[] salt = Arrays.copyOfRange(encryptedData, 0, 16);
                byte[] iv = Arrays.copyOfRange(encryptedData, 16, 32);
                byte[] ciphertext = Arrays.copyOfRange(encryptedData, 32, encryptedData.length - 32);
                byte[] originalHash = Arrays.copyOfRange(encryptedData, encryptedData.length - 32, encryptedData.length);

                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                KeySpec spec = new PBEKeySpec(passwordd.toCharArray(), salt, 65536, 256);
                SecretKey tmp = factory.generateSecret(spec);
                SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

                byte[] decryptedData = cipher.doFinal(ciphertext);

                // Calcular el hash SHA-256 del archivo descifrado
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] computedHash = md.digest(decryptedData);

                // Verificar la integridad del archivo descifrado
                if (Arrays.equals(originalHash, computedHash)) {
                    Files.write(Paths.get(outputFilex), decryptedData);
                    System.out.println("Archivo descifrado exitosamente.");
                } else {
                    System.out.println("Error: El archivo ha sido modificado o la contraseña es incorrecta.");
                }
            } catch (Exception e) {
                System.out.println("Error al descifrar el archivo: " + e.getMessage());
            }
            break;
        case 3:
            System.out.println("Bye!");
           
            break;
        }
    }
    
    public void startProgram() throws NumberFormatException, IOException {
        int option = 0;
        int exit = 3;
        do {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            showMenu();
            option = readOption(br);
            doOption(option, br);
        } while(option != exit);
    }
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        Menu menu = new Menu();
        menu.startProgram();
    }
}