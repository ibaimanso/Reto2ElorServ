package menu.controlador;

import menu.dto.HorarioDTO;
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

    @GetMapping("/profesor/{id}")
    public List<HorarioDTO> getHorarioProfesor(@PathVariable Integer id) {
        return horarioService.horarioProfesor(id);
    }

    @GetMapping("/alumno/{id}")
    public List<HorarioDTO> horarioAlumno(@PathVariable Integer id) {
        return horarioService.horarioAlumno(id);
    }
}
