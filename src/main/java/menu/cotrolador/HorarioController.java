package  menu.cotrolador;




/*
 * 
 * 
import modelo.Horario;
import servicios.HorarioService;
 * 
 * */
import  menu.modelo.Horario;
import  menu.servicios.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/profesor/{id}")
    public List<Horario> horarioProfesor(@PathVariable Integer id) {
        return horarioService.findByProfesorId(id);
    }

 
}
