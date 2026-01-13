package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "matriculaciones")
public class Matriculaciones implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "curso")
    private Byte curso;


    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "alum_id")
    private Users alumno;

    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclos ciclo;

    // Constructor vacío (requerido por JPA)
    public Matriculaciones() {
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getCurso() {
        return curso;
    }

    public void setCurso(Byte curso) {
        this.curso = curso;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    @Override
    public String toString() {
        return "Matriculaciones{" +
                "id=" + id +
                '}';
    }
}