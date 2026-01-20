package menu.servicios;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class CryptoService {

    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";

    private KeyPair keyPair;

    public CryptoService() throws Exception {
        File pubFile = new File(PUBLIC_KEY_FILE);
        File privFile = new File(PRIVATE_KEY_FILE);

        if (pubFile.exists() && privFile.exists()) {
            // Si los archivos existen, cargamos las claves
            this.keyPair = new KeyPair(loadPublicKey(pubFile), loadPrivateKey(privFile));
        } else {
            // Si no existen, generamos nuevas claves y las guardamos
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            this.keyPair = generator.generateKeyPair();
            saveKey(pubFile, keyPair.getPublic().getEncoded());
            saveKey(privFile, keyPair.getPrivate().getEncoded());
        }
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    private void saveKey(File file, byte[] keyBytes) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(keyBytes);
        }
    }

    private PublicKey loadPublicKey(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private PrivateKey loadPrivateKey(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
