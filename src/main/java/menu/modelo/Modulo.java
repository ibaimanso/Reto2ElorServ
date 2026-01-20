package  menu.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "modulo")
public class Modulo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ciclo_id", nullable = false)
    private Integer cicloId;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCicloId() { return cicloId; }
    public void setCicloId(Integer cicloId) { this.cicloId = cicloId; }
}
