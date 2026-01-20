package  menu.servicios;

import  menu.modelo.Reunion;

import   menu.repositorios.ReunionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReunionService {

    @Autowired
    private ReunionRepository reunionRepository;

    public Reunion crearReunion(Reunion reunion) {
        return reunionRepository.save(reunion);
    }

    public Reunion cambiarEstado(Integer id, String estado) {
        Optional<Reunion> opt = reunionRepository.findById(id);
        if (opt.isPresent()) {
            Reunion reunion = opt.get();
            reunion.setEstado(estado);
            return reunionRepository.save(reunion);
        }
        return null; 
    }
}
