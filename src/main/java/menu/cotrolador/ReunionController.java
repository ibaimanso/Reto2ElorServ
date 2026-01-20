package  menu.cotrolador;

 

import  menu.modelo.Reunion;
import  menu.servicios.ReunionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reunion")
public class ReunionController {

    @Autowired
    private ReunionService reunionService;

    @PostMapping
    public Reunion crearReunion(@RequestBody Reunion reunion) {
        return reunionService.crearReunion(reunion);
    }

    @PutMapping("/{id}/{estado}")
    public Reunion cambiarEstado(@PathVariable Integer id, @PathVariable String estado) {
        return reunionService.cambiarEstado(id, estado);
    }
}
