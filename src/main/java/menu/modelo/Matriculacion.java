package  menu.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "matriculacion")
public class Matriculacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alumno_id", nullable = false)
    private Integer alumnoId;

    @Column(name = "Ciclo_Id", nullable = false)
    private Integer cicloId;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Integer alumnoId) { this.alumnoId = alumnoId; }

    public Integer getModuloId() { return cicloId; }
    public void setModuloId(Integer moduloId) { this.cicloId = moduloId; }
}
