package menu.repositorios;

import menu.modelo.Matriculacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculacionRepository extends JpaRepository<Matriculacion, Integer> {

    List<Matriculacion> findByAlumno_Id(Integer alumnoId);
}
