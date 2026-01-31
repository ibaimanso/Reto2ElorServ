package menu.controlador;

 

 

import   menu.modelo.Tipo;
import   menu.servicios.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo")
public class TipoController {

    @Autowired
    private TipoService tipoService;

    // GET - SELECT all tipos
    @GetMapping
    public List<Tipo> getAll() {
        return tipoService.findAll();
    }

    // GET - SELECT tipo by id
    @GetMapping("/{id}")
    public Tipo getById(@PathVariable Integer id) {
        return tipoService.findById(id).orElse(null);
    }

    // POST - INSERT new tipo
    @PostMapping
    public Tipo create(@RequestBody Tipo tipo) {
        return tipoService.save(tipo);
    }

    // PUT - UPDATE tipo
    @PutMapping("/{id}")
    public Tipo update(@PathVariable Integer id, @RequestBody Tipo tipo) {
        tipo.setId(id);
        return tipoService.save(tipo);
    }

    // DELETE - DELETE tipo
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tipoService.delete(id);
    }
}