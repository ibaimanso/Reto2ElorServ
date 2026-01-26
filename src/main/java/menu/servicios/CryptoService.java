package menu.servicios;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";

    private final KeyPair keyPair;

    public CryptoService() throws Exception {
        File pub = new File(PUBLIC_KEY_FILE);
        File priv = new File(PRIVATE_KEY_FILE);

        if (pub.exists() && priv.exists()) {
            PublicKey publicKey = loadPublicKey(pub);
            PrivateKey privateKey = loadPrivateKey(priv);
            keyPair = new KeyPair(publicKey, privateKey);
        } else {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            keyPair = gen.generateKeyPair();
            saveKey(pub, keyPair.getPublic().getEncoded());
            saveKey(priv, keyPair.getPrivate().getEncoded());
        }
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public String decrypt(String encryptedBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted);
    }

    private void saveKey(File file, byte[] bytes) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    private PublicKey loadPublicKey(File file) throws Exception {
        byte[] bytes = fileBytes(file);
        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(bytes));
    }

    private PrivateKey loadPrivateKey(File file) throws Exception {
        byte[] bytes = fileBytes(file);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    private byte[] fileBytes(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        return bytes;
    }
}
