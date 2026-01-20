package  menu.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reuniones")
public class Reunion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reunion")
    private Integer id;

    @Column(name = "estado")
    private String estado;

    @Column(name = "estado_eus")
    private String estadoEus;

    @Column(name = "profesor_id")
    private Integer profesorId;

    @Column(name = "alumno_id")
    private Integer alumnoId;

    @Column(name = "id_centro")
    private String idCentro;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "asunto")
    private String asunto;

    @Column(name = "aula")
    private String aula;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstadoEus() { return estadoEus; }
    public void setEstadoEus(String estadoEus) { this.estadoEus = estadoEus; }

    public Integer getProfesorId() { return profesorId; }
    public void setProfesorId(Integer profesorId) { this.profesorId = profesorId; }

    public Integer getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Integer alumnoId) { this.alumnoId = alumnoId; }

    public String getIdCentro() { return idCentro; }
    public void setIdCentro(String idCentro) { this.idCentro = idCentro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

}
