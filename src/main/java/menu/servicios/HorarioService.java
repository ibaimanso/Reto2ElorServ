package menu.servicios;

import menu.dto.HorarioDTO;
import menu.modelo.Horario;
import menu.repositorios.HorarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // GET - SELECT all horarios
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    // GET - SELECT horario by id
    public Optional<Horario> findById(Integer id) {
        return horarioRepository.findById(id);
    }

    // GET - SELECT horario profesor
    public List<HorarioDTO> horarioProfesor(Integer id) {
        return horarioRepository.horarioProfesor(id);
    }

    // GET - SELECT horario alumno
    public List<HorarioDTO> horarioAlumno(Integer id) {
        return horarioRepository.horarioAlumno(id);
    }

    // POST - INSERT / PUT - UPDATE
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    // DELETE - DELETE horario
    public void delete(Integer id) {
        horarioRepository.deleteById(id);
    }
}