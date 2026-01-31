package  menu.servicios;

 
import   menu.modelo.Ciclo;
import   menu.repositorios.CicloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CicloService {

    @Autowired
    private CicloRepository cicloRepository;

    public List<Ciclo> findAll() {
        return cicloRepository.findAll();
    }

    public Optional<Ciclo> findById(Integer id) {
        return cicloRepository.findById(id);
    }

    public Ciclo save(Ciclo ciclo) {
        return cicloRepository.save(ciclo);
    }

    public void delete(Integer id) {
        cicloRepository.deleteById(id);
    }
}
