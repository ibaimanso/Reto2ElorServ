package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class Users implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "email", length = 100)
    private String email;


    @Column(name = "username", length = 50)
    private String username;


    @Column(name = "password", length = 255)
    private String password;


    @Column(name = "nombre", length = 50)
    private String nombre;


    @Column(name = "apellidos", length = 100)
    private String apellidos;


    @Column(name = "dni", length = 20)
    private String dni;


    @Column(name = "direccion", length = 255)
    private String direccion;


    @Column(name = "telefono1", length = 20)
    private String telefono1;


    @Column(name = "telefono2", length = 20)
    private String telefono2;


    @Column(name = "argazkia_url", length = 255)
    private String argazkiaUrl;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipos tipo;

    @OneToMany(mappedBy = "profesor")
    private List<Horarios> horariosList;

    @OneToMany(mappedBy = "alumno")
    private List<Matriculaciones> matriculacionesList;

    @OneToMany(mappedBy = "profesor")
    private List<Reuniones> reunionesProfeList;

    @OneToMany(mappedBy = "alumno")
    private List<Reuniones> reunionesAlumnoList;

    // Constructor vacío (requerido por JPA)
    public Users() {
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getArgazkiaUrl() {
        return argazkiaUrl;
    }

    public void setArgazkiaUrl(String argazkiaUrl) {
        this.argazkiaUrl = argazkiaUrl;
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
    
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                '}';
    }
}