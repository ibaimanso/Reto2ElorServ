package menu.controlador;

 

import  menu.modelo.Reunion;
import  menu.servicios.ReunionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reunion")
public class ReunionController {

    @Autowired
    private ReunionService reunionService;

    // GET - SELECT all reuniones
    @GetMapping
    public List<Reunion> getAll() {
        return reunionService.findAll();
    }

    // GET - SELECT reunion by id
    @GetMapping("/{id}")
    public Reunion getById(@PathVariable Integer id) {
        return reunionService.findById(id).orElse(null);
    }

    // POST - INSERT new reunion
    @PostMapping
    public Reunion crearReunion(@RequestBody Reunion reunion) {
        return reunionService.crearReunion(reunion);
    }

    // PUT - UPDATE reunion
    @PutMapping("/{id}")
    public Reunion updateReunion(@PathVariable Integer id, @RequestBody Reunion reunion) {
        reunion.setId_reunion(id);
        return reunionService.updateReunion(reunion);
    }

    // PUT - UPDATE estado de reunion
    @PutMapping("/{id}/{estado}")
    public Reunion cambiarEstado(@PathVariable Integer id, @PathVariable String estado) {
        return reunionService.cambiarEstado(id, estado);
    }

    // DELETE - DELETE reunion
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reunionService.delete(id);
    }
}