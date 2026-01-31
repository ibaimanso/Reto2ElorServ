package  menu.repositorios;

 
import   menu.modelo.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Integer> {
    // Puedes añadir métodos personalizados si luego necesitas buscar por fecha, estado, etc.
}
