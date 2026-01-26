package menu.modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="ciclos")
public class Ciclo {

    @Id
    private Integer id;

    private String nombre;

    @OneToMany(mappedBy="ciclo")
    private List<Modulo> modulos;

    @OneToMany(mappedBy="ciclo")
    private List<Matriculacion> matriculaciones;

    @OneToMany(mappedBy="ciclo")
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

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public List<Matriculacion> getMatriculaciones() {
		return matriculaciones;
	}

	public void setMatriculaciones(List<Matriculacion> matriculaciones) {
		this.matriculaciones = matriculaciones;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

    // getters setters
}
