package menu.servicios;

import menu.dto.HorarioDTO;
import menu.repositorios.HorarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<HorarioDTO> horarioProfesor(Integer id) {
        return horarioRepository.horarioProfesor(id);
    }

    public List<HorarioDTO> horarioAlumno(Integer id) {
        return horarioRepository.horarioAlumno(id);
    }
}
