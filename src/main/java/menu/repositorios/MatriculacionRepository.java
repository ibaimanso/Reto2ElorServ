package  menu.repositorios;

 
import   menu.modelo.Matriculacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculacionRepository extends JpaRepository<Matriculacion, Integer> {
    List<Matriculacion> findByAlumnoId(Integer alumId);
    List<Matriculacion> findByCicloId(Integer cicloId);
    

}

