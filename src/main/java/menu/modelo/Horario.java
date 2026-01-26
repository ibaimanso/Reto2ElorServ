package menu.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "horarios",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ciclo_id", "curso", "dia", "hora"})
    }
)
public class Horario {

    @Id
    private Integer id;

    private String dia;
    private Integer hora;
    private String aula;
    private String observaciones;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // ===== PROFESOR =====
    @ManyToOne
    @JoinColumn(name = "profe_id")
    private User profesor;

    // ===== MODULO =====
    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    // ===== CICLO =====
    @ManyToOne
    @JoinColumn(name = "ciclo_id")
    private Ciclo ciclo;

    // ===== CURSO =====
    private Integer curso;

    // ================= GETTERS / SETTERS =================

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

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
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

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }
}
