package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "horarios")
public class Horarios implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "dia")
    private String dia;


    @Column(name = "hora")
    private Byte hora;


    @Column(name = "aula", length = 50)
    private String aula;


    @Column(name = "observaciones", length = 255)
    private String observaciones;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "profe_id")
    private Users profesor;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulos modulo;

    // Constructor vacío (requerido por JPA)
    public Horarios() {
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Byte getHora() {
        return hora;
    }

    public void setHora(Byte hora) {
        this.hora = hora;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Users getProfesor() {
        return profesor;
    }

    public void setProfesor(Users profesor) {
        this.profesor = profesor;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }
    
    @Override
    public String toString() {
        return "Horarios{" +
                "id=" + id +
                '}';
    }
}