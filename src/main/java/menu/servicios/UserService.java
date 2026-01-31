package menu.servicios;

import menu.SecurityConfig.PasswordUtil;
import menu.modelo.User;
import menu.repositorios.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Login descifrando contraseña RSA y comparando con hash en DB
    public User login(String username, String passwordCifrada) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("USUARIO_NO_EXISTE");
        }

        String passwordPlano = cryptoService.decrypt(passwordCifrada);

        if (!encoder.matches(passwordPlano, user.getPassword())) {
            throw new IllegalArgumentException("PASSWORD_INCORRECTA");
        }

        return user;
    }

    // Recuperar contraseña y enviar email
    public void forgotPassword(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("USUARIO_NO_EXISTE");
        }

        // Generar nueva contraseña aleatoria
        String tempPassword = PasswordUtil.generateRandomPassword(10);

        // Guardar contraseña encriptada en DB
        String hashed = encoder.encode(tempPassword);
        user.setPassword(hashed);
        userRepository.save(user);

        // Enviar correo con la nueva contraseña
        emailService.sendPasswordReset(email, tempPassword);
    }
    
    // GET - SELECT all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // GET - SELECT user by id
    public User getUserById(int id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    // POST - INSERT new user
    public User createUser(User user) {
        // Encriptar contraseña antes de guardar
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
    
    // PUT - UPDATE user
    public User updateUser(User user) {
        // Si hay nueva contraseña, encriptarla
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
    
    // DELETE - DELETE user
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}