package  menu.servicios;

import  menu.modelo.Reunion;

import   menu.repositorios.ReunionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReunionService {

    @Autowired
    private ReunionRepository reunionRepository;

    // GET - SELECT all reuniones
    public List<Reunion> findAll() {
        return reunionRepository.findAll();
    }

    // GET - SELECT reunion by id
    public Optional<Reunion> findById(Integer id) {
        return reunionRepository.findById(id);
    }

    // POST - INSERT new reunion
    public Reunion crearReunion(Reunion reunion) {
        return reunionRepository.save(reunion);
    }

    // PUT - UPDATE reunion
    public Reunion updateReunion(Reunion reunion) {
        return reunionRepository.save(reunion);
    }

    // PUT - UPDATE estado
    public Reunion cambiarEstado(Integer id, String estado) {
        Optional<Reunion> opt = reunionRepository.findById(id);
        if (opt.isPresent()) {
            Reunion reunion = opt.get();
            reunion.setEstado(estado);
            return reunionRepository.save(reunion);
        }
        return null; 
    }

    // DELETE - DELETE reunion
    public void delete(Integer id) {
        reunionRepository.deleteById(id);
    }
}