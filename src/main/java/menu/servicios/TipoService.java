package  menu.servicios;

 
import   menu.modelo.Tipo;
import   menu.repositorios.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    public List<Tipo> findAll() {
        return tipoRepository.findAll();
    }

    public Optional<Tipo> findById(Integer id) {
        return tipoRepository.findById(id);
    }

    public Tipo save(Tipo tipo) {
        return tipoRepository.save(tipo);
    }

    public void delete(Integer id) {
        tipoRepository.deleteById(id);
    }
}
