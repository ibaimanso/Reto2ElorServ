package  menu.servicios;

 
import   menu.modelo.Horario;
import   menu.repositorios.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Horario> findByProfesorId(Integer profeId) {
        return horarioRepository.findByProfesorId(profeId);
    }



 
}
