package  menu.servicios;

import  menu.modelo.Matriculacion;
import  menu.repositorios.MatriculacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculacionService {

    @Autowired
    private MatriculacionRepository matriculacionRepository;

    public List<Matriculacion> findAll() {
        return matriculacionRepository.findAll();
    }

    public Optional<Matriculacion> findById(Integer id) {
        return matriculacionRepository.findById(id);
    }

    public Matriculacion save(Matriculacion matriculacion) {
        return matriculacionRepository.save(matriculacion);
    }

    public void delete(Integer id) {
        matriculacionRepository.deleteById(id);
    }

    public List<Matriculacion> findByAlumnoId(Integer alumnoId) {
        return matriculacionRepository.findByAlumnoId(alumnoId);
    }

    public List<Matriculacion> findByCicloId(Integer cicloId) {
        return matriculacionRepository.findByCicloId(cicloId);
    }
}
