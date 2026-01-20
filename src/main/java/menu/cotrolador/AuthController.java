package menu.cotrolador;

import menu.modelo.User;
import menu.servicios.UserService;
import menu.servicios.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoService cryptoService;

    // LOGIN con JSON
    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> body) throws Exception {
        String username = body.get("username");
        String passwordCifrada = body.get("passwordCifrada");
        return userService.login(username, passwordCifrada);
    }

    // DEVOLVER CLAVE PÚBLICA
    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        String key = Base64.getEncoder().encodeToString(cryptoService.getPublicKey().getEncoded());
        return Collections.singletonMap("publicKey", key);
    }

}
