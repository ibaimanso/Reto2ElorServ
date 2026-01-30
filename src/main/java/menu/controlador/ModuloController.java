package menu.controlador;

 
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

    // GET - SELECT all modulos
    @GetMapping
    public List<Modulo> getAll() {
        return moduloService.findAll();
    }

    // GET - SELECT modulo by id
    @GetMapping("/{id}")
    public Modulo getById(@PathVariable Integer id) {
        return moduloService.findById(id).orElse(null);
    }

    // POST - INSERT new modulo
    @PostMapping
    public Modulo create(@RequestBody Modulo modulo) {
        return moduloService.save(modulo);
    }

    // PUT - UPDATE modulo
    @PutMapping("/{id}")
    public Modulo update(@PathVariable Integer id, @RequestBody Modulo modulo) {
        modulo.setId(id);
        return moduloService.save(modulo);
    }

    // DELETE - DELETE modulo
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        moduloService.delete(id);
    }
}