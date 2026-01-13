package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tipos")
public class Tipos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "name", length = 50)
    private String name;


    @Column(name = "name_eu", length = 50)
    private String nameEu;

    @OneToMany(mappedBy = "tipo")
    private List<Users> usersList;

    // Constructor vacío (requerido por JPA)
    public Tipos() {
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEu() {
        return nameEu;
    }

    public void setNameEu(String nameEu) {
        this.nameEu = nameEu;
    }
    
    @Override
    public String toString() {
        return "Tipos{" +
                "id=" + id +
                '}';
    }
}