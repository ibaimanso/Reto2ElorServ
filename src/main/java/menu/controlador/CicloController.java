package menu.controlador;

 

 
import   menu.modelo.Ciclo;
import   menu.servicios.CicloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciclo")
public class CicloController {

    @Autowired
    private CicloService cicloService;

    // GET - SELECT all ciclos
    @GetMapping
    public List<Ciclo> getAll() {
        return cicloService.findAll();
    }

    // GET - SELECT ciclo by id
    @GetMapping("/{id}")
    public Ciclo getById(@PathVariable Integer id) {
        return cicloService.findById(id).orElse(null);
    }

    // POST - INSERT new ciclo
    @PostMapping
    public Ciclo create(@RequestBody Ciclo ciclo) {
        return cicloService.save(ciclo);
    }

    // PUT - UPDATE ciclo
    @PutMapping("/{id}")
    public Ciclo update(@PathVariable Integer id, @RequestBody Ciclo ciclo) {
        ciclo.setId(id);
        return cicloService.save(ciclo);
    }

    // DELETE - DELETE ciclo
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cicloService.delete(id);
    }
}