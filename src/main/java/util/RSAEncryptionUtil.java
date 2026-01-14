package util;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utilidad para cifrado/descifrado RSA.
 * El servidor genera un par de claves (pública/privada).
 * La clave pública se envía al cliente para que cifre la contraseña.
 * El servidor usa la clave privada para descifrar.
 */
public class RSAEncryptionUtil {
    
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String PRIVATE_KEY_FILE = "keys/private_key.key";
    private static final String PUBLIC_KEY_FILE = "keys/public_key.key";
    
    private static KeyPair keyPair;
    
    /**
     * Genera un par de claves RSA (pública/privada).
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        keyPair = keyGen.generateKeyPair();
        return keyPair;
    }
    
    /**
     * Guarda las claves en archivos.
     */
    public static void saveKeys(KeyPair keyPair) throws IOException {
        File keysDir = new File("keys");
        if (!keysDir.exists()) {
            keysDir.mkdirs();
        }
        
        // Guardar clave privada
        try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE)) {
            fos.write(keyPair.getPrivate().getEncoded());
        }
        
        // Guardar clave pública
        try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE)) {
            fos.write(keyPair.getPublic().getEncoded());
        }
        
        System.out.println("✓ Claves RSA guardadas en: keys/");
    }
    
    /**
     * Carga la clave privada desde archivo.
     */
    public static PrivateKey loadPrivateKey() throws Exception {
        File keyFile = new File(PRIVATE_KEY_FILE);
        if (!keyFile.exists()) {
            throw new FileNotFoundException("Archivo de clave privada no encontrado: " + PRIVATE_KEY_FILE);
        }
        
        byte[] keyBytes = new byte[(int) keyFile.length()];
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            fis.read(keyBytes);
        }
        
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }
    
    /**
     * Carga la clave pública desde archivo.
     */
    public static PublicKey loadPublicKey() throws Exception {
        File keyFile = new File(PUBLIC_KEY_FILE);
        if (!keyFile.exists()) {
            throw new FileNotFoundException("Archivo de clave pública no encontrado: " + PUBLIC_KEY_FILE);
        }
        
        byte[] keyBytes = new byte[(int) keyFile.length()];
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            fis.read(keyBytes);
        }
        
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }
    
    /**
     * Obtiene la clave pública en formato Base64 (para enviar al cliente).
     */
    public static String getPublicKeyBase64() throws Exception {
        PublicKey publicKey = loadPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    
    /**
     * Cifra un texto con la clave pública.
     * (Usado principalmente en el cliente, pero se incluye para testing)
     */
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    /**
     * Descifra un texto cifrado con la clave privada.
     * ESTE ES EL MÉTODO QUE USA EL SERVIDOR para descifrar contraseñas.
     */
    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, "UTF-8");
    }
    
    /**
     * Inicializa las claves RSA.
     * Si no existen, las genera y guarda.
     */
    public static void initializeKeys() {
        try {
            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            
            if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
                System.out.println("⚠ No se encontraron claves RSA. Generando nuevas claves...");
                KeyPair kp = generateKeyPair();
                saveKeys(kp);
                System.out.println("✓ Claves RSA generadas y guardadas exitosamente.");
            } else {
                System.out.println("✓ Claves RSA existentes cargadas.");
            }
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar claves RSA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
