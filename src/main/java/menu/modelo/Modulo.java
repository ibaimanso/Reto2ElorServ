package menu.modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="modulos")
public class Modulo {

    @Id
    private Integer id;

    private String nombre;
    private String nombre_eus;
    private Integer horas;
    private Integer curso;

    @ManyToOne
    @JoinColumn(name="ciclo_id")
    private Ciclo ciclo;

    @OneToMany(mappedBy="modulo")
    private List<Horario> horarios;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre_eus() {
		return nombre_eus;
	}

	public void setNombre_eus(String nombre_eus) {
		this.nombre_eus = nombre_eus;
	}

	public Integer getHoras() {
		return horas;
	}

	public void setHoras(Integer horas) {
		this.horas = horas;
	}

	public Integer getCurso() {
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

    // getters setters
}
