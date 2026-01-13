package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "modulos")
public class Modulos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "nombre", length = 200)
    private String nombre;


    @Column(name = "nombre_eus", length = 200)
    private String nombreEus;


    @Column(name = "horas")
    private Integer horas;


    @Column(name = "curso")
    private Byte curso;

    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclos ciclo;

    @OneToMany(mappedBy = "modulo")
    private List<Horarios> horariosList;

    // Constructor vacío (requerido por JPA)
    public Modulos() {
    }
    
    // Getters y Setters
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

    public String getNombreEus() {
        return nombreEus;
    }

    public void setNombreEus(String nombreEus) {
        this.nombreEus = nombreEus;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Byte getCurso() {
        return curso;
    }

    public void setCurso(Byte curso) {
        this.curso = curso;
    }
    
    @Override
    public String toString() {
        return "Modulos{" +
                "id=" + id +
                '}';
    }
}