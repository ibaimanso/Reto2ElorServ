package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncryptionUtil {
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void initializeKeys() {
        try {
            byte[] pub = Files.readAllBytes(Path.of("public.key"));
            byte[] priv = Files.readAllBytes(Path.of("private.key"));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(new X509EncodedKeySpec(pub));
            privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(priv));
        } catch (Exception e) {
            SocketLogger.error("No se pudieron cargar las claves RSA", e);
        }
    }

    public static PublicKey getPublicKey() { return publicKey; }
    public static PrivateKey getPrivateKey() { return privateKey; }
}
