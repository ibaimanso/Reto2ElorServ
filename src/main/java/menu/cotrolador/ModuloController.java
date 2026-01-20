package  menu.cotrolador;

 
import   menu.modelo.Ciclo;
import   menu.servicios.CicloService;

import   menu.modelo.Modulo;
import  menu.servicios.ModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modulo")
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    @GetMapping
    public List<Modulo> getAll() {
        return moduloService.findAll();
    }

    @GetMapping("/{id}")
    public Modulo getById(@PathVariable Integer id) {
        return moduloService.findById(id).orElse(null);
    }

    @PostMapping
    public Modulo create(@RequestBody Modulo modulo) {
        return moduloService.save(modulo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moduloService.delete(id);
    }
}
