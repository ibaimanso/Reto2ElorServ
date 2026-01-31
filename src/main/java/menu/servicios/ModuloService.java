package  menu.servicios;

 
import   menu.modelo.Modulo;
import   menu.repositorios.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    public List<Modulo> findAll() {
        return moduloRepository.findAll();
    }

    public Optional<Modulo> findById(Integer id) {
        return moduloRepository.findById(id);
    }

    public Modulo save(Modulo modulo) {
        return moduloRepository.save(modulo);
    }

    public void delete(Integer id) {
        moduloRepository.deleteById(id);
    }
}
