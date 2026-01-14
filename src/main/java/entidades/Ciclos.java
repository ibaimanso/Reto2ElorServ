package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ciclos")
public class Ciclos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "nombre", length = 200)
    private String nombre;

    @OneToMany(mappedBy = "ciclo")
    private List<Modulos> modulosList;

    // Constructor vacío (requerido por JPA)
    public Ciclos() {
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
    
    public List<Modulos> getModulosList() {
        return modulosList;
    }

    public void setModulosList(List<Modulos> modulosList) {
        this.modulosList = modulosList;
    }
    
    @Override
    public String toString() {
        return "Ciclos{" +
                "id=" + id +
                '}';
    }
}