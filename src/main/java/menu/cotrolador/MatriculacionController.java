package  menu.cotrolador;
 
import   menu.modelo.Matriculacion;
import   menu.servicios.MatriculacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculacion")
public class MatriculacionController {

    @Autowired
    private MatriculacionService matriculacionService;

     @GetMapping
    public List<Matriculacion> getAll() {
        return matriculacionService.findAll();
    }

    // Obtener matriculaciones por ID
    @GetMapping("/{id}")
    public Matriculacion getById(@PathVariable Integer id) {
        return matriculacionService.findById(id).orElse(null);
    }

    // Crear una nueva matriculacion
    @PostMapping
    public Matriculacion create(@RequestBody Matriculacion matriculacion) {
        return matriculacionService.save(matriculacion);
    }

    // Eliminar matriculacion
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        matriculacionService.delete(id);
    }

    // Obtener matriculaciones de un alumno por su ID
    @GetMapping("/alumno/{alumnoId}") 
    public List<Matriculacion> getByAlumno(@PathVariable Integer alumnoId) {
        return matriculacionService.findByAlumnoId(alumnoId);
    }

    // Obtener matriculaciones de un ciclo por su ID
    @GetMapping("/ciclo/{cicloId}")
    public List<Matriculacion> getByCiclo(@PathVariable Integer cicloId) {
        return matriculacionService.findByCicloId(cicloId);
    }
}
