package  menu.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reuniones")
public class Reunion {

    @Id
    private Integer id_reunion;

    private String estado;
    private String estado_eus;
    private String id_centro;
    private String titulo;
    public Integer getId_reunion() {
		return id_reunion;
	}

	public void setId_reunion(Integer id_reunion) {
		this.id_reunion = id_reunion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado_eus() {
		return estado_eus;
	}

	public void setEstado_eus(String estado_eus) {
		this.estado_eus = estado_eus;
	}

	public String getId_centro() {
		return id_centro;
	}

	public void setId_centro(String id_centro) {
		this.id_centro = id_centro;
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

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

	public User getProfesor() {
		return profesor;
	}

	public void setProfesor(User profesor) {
		this.profesor = profesor;
	}

	public User getAlumno() {
		return alumno;
	}

	public void setAlumno(User alumno) {
		this.alumno = alumno;
	}

	private String asunto;
    private String aula;

    private LocalDateTime fecha;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private User profesor;

    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private User alumno;
}
