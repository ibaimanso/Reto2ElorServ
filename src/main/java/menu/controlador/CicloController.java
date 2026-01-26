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

    @GetMapping
    public List<Ciclo> getAll() {
        return cicloService.findAll();
    }

    @GetMapping("/{id}")
    public Ciclo getById(@PathVariable Integer id) {
        return cicloService.findById(id).orElse(null);
    }

    @PostMapping
    public Ciclo create(@RequestBody Ciclo ciclo) {
        return cicloService.save(ciclo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cicloService.delete(id);
    }
}
