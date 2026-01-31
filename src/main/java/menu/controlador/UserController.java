package menu.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import menu.modelo.User;
import menu.servicios.StorageService;
import menu.servicios.UserService;

import menu.repositorios.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    // GET - SELECT all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET - SELECT user by id
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        // opcional: limpiar campos sensibles
        user.setPassword(null);
        return user;
    }

    // POST - INSERT new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT - UPDATE user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    // DELETE - DELETE user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<User> uploadPhoto(
            @PathVariable Integer id,
            @RequestParam("photo") MultipartFile photo
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String url = storageService.save(photo);

        user.setArgazkia_url(url);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}