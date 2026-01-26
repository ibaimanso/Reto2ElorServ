package menu.modelo;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "matriculaciones")
public class Matriculacion {

    @Id
    private Integer id;

    private Integer curso;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "alum_id")
    private User alumno;

    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclo ciclo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurso() {
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public User getAlumno() {
		return alumno;
	}

	public void setAlumno(User alumno) {
		this.alumno = alumno;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}
}

