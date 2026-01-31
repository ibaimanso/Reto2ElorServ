package menu.controlador;

import menu.modelo.User;
import menu.servicios.CryptoService;
import menu.servicios.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        String key = Base64.getEncoder()
                .encodeToString(cryptoService.getPublicKey().getEncoded());
        return Map.of("publicKey", key);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            User user = userService.login(
                    body.get("username"),
                    body.get("passwordCifrada")
            );
            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if ("USUARIO_NO_EXISTE".equals(msg)) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no existe"));
            }
            // Cualquier otro IllegalArgumentException → contraseña incorrecta
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }
    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            userService.forgotPassword(email);
            return Map.of("message", "Se ha enviado la nueva contraseña al email");
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

}
