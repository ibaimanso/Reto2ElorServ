package menu.controlador;

import menu.dto.HorarioDTO;
import menu.modelo.Horario;
import menu.servicios.HorarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
@CrossOrigin
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    // GET - SELECT all horarios
    @GetMapping
    public List<Horario> getAllHorarios() {
        return horarioService.findAll();
    }

    // GET - SELECT horario by id
    @GetMapping("/{id}")
    public Horario getById(@PathVariable Integer id) {
        return horarioService.findById(id).orElse(null);
    }

    // GET - SELECT horario profesor
    @GetMapping("/profesor/{id}")
    public List<HorarioDTO> getHorarioProfesor(@PathVariable Integer id) {
        return horarioService.horarioProfesor(id);
    }

    // GET - SELECT horario alumno
    @GetMapping("/alumno/{id}")
    public List<HorarioDTO> horarioAlumno(@PathVariable Integer id) {
        return horarioService.horarioAlumno(id);
    }

    // POST - INSERT new horario
    @PostMapping
    public Horario create(@RequestBody Horario horario) {
        return horarioService.save(horario);
    }

    // PUT - UPDATE horario
    @PutMapping("/{id}")
    public Horario update(@PathVariable Integer id, @RequestBody Horario horario) {
        horario.setId(id);
        return horarioService.save(horario);
    }

    // DELETE - DELETE horario
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        horarioService.delete(id);
    }
}