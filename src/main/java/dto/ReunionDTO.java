package dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de reuniones.
 */
public class ReunionDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer idReunion;
    private String estado;  // PENDIENTE, ACEPTADA, CANCELADA, CONFLICTO
    private String estadoEus;
    private String idCentro;
    private String titulo;
    private String asunto;
    private String aula;
    private LocalDateTime fecha;
    
    // Información del profesor
    private Integer profesorId;
    private String profesorNombre;
    private String profesorApellidos;
    private String profesorEmail;
    
    // Información del alumno
    private Integer alumnoId;
    private String alumnoNombre;
    private String alumnoApellidos;
    private String alumnoEmail;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructores
    public ReunionDTO() {
    }
    
    // Getters y Setters
    public Integer getIdReunion() {
        return idReunion;
    }
    
    public void setIdReunion(Integer idReunion) {
        this.idReunion = idReunion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getEstadoEus() {
        return estadoEus;
    }
    
    public void setEstadoEus(String estadoEus) {
        this.estadoEus = estadoEus;
    }
    
    public String getIdCentro() {
        return idCentro;
    }
    
    public void setIdCentro(String idCentro) {
        this.idCentro = idCentro;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getAula() {
        return aula;
    }
    
    public void setAula(String aula) {
        this.aula = aula;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public Integer getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }
    
    public String getProfesorNombre() {
        return profesorNombre;
    }
    
    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }
    
    public String getProfesorApellidos() {
        return profesorApellidos;
    }
    
    public void setProfesorApellidos(String profesorApellidos) {
        this.profesorApellidos = profesorApellidos;
    }
    
    public String getProfesorEmail() {
        return profesorEmail;
    }
    
    public void setProfesorEmail(String profesorEmail) {
        this.profesorEmail = profesorEmail;
    }
    
    public Integer getAlumnoId() {
        return alumnoId;
    }
    
    public void setAlumnoId(Integer alumnoId) {
        this.alumnoId = alumnoId;
    }
    
    public String getAlumnoNombre() {
        return alumnoNombre;
    }
    
    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }
    
    public String getAlumnoApellidos() {
        return alumnoApellidos;
    }
    
    public void setAlumnoApellidos(String alumnoApellidos) {
        this.alumnoApellidos = alumnoApellidos;
    }
    
    public String getAlumnoEmail() {
        return alumnoEmail;
    }
    
    public void setAlumnoEmail(String alumnoEmail) {
        this.alumnoEmail = alumnoEmail;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "ReunionDTO{" +
                "idReunion=" + idReunion +
                ", estado='" + estado + '\'' +
                ", titulo='" + titulo + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}