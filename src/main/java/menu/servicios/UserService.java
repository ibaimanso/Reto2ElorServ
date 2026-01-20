package menu.servicios;

import menu.modelo.User;
import menu.repositorios.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoService cryptoService;

    // Login usando RSA-OAEP/SHA-256
    public User login(String username, String passwordCifradaBase64) throws Exception {
        User usuario = userRepository.findByUsername(username);

        if (usuario == null) return null;

        // Descifra la contraseña enviada desde el navegador
        String passwordPlano = rsaDecrypt(passwordCifradaBase64, cryptoService.getPrivateKey());

        // Compara con la contraseña guardada en la DB (texto plano)
        if (passwordPlano.equals(usuario.getPassword())) {
            return usuario;
        } else {
            return null; // contraseña incorrecta
        }
    }

    private String rsaDecrypt(String textoCifradoBase64, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytesCifrados = Base64.getDecoder().decode(textoCifradoBase64);
        byte[] bytesDescifrados = cipher.doFinal(bytesCifrados);

        return new String(bytesDescifrados); // UTF-8
    }



}
