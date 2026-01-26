package menu.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import menu.modelo.User;
import menu.servicios.StorageService;
import menu.servicios.UserService;

import menu.repositorios.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        // opcional: limpiar campos sensibles
        user.setPassword(null);
        return user;
    }
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

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
