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

    @GetMapping
    public List<Tipo> getAll() {
        return tipoService.findAll();
    }

    @GetMapping("/{id}")
    public Tipo getById(@PathVariable Integer id) {
        return tipoService.findById(id).orElse(null);
    }

    @PostMapping
    public Tipo create(@RequestBody Tipo tipo) {
        return tipoService.save(tipo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tipoService.delete(id);
    }
}
