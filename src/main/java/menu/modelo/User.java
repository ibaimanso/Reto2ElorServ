package  menu.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // nombre de la tabla en la base de datos
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private Integer tipoId;
    private String argazkiaUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono1() { return telefono1; }
    public void setTelefono1(String telefono1) { this.telefono1 = telefono1; }

    public String getTelefono2() { return telefono2; }
    public void setTelefono2(String telefono2) { this.telefono2 = telefono2; }

    public Integer getTipoId() { return tipoId; }
    public void setTipoId(Integer tipoId) { this.tipoId = tipoId; }

    public String getArgazkiaUrl() { return argazkiaUrl; }
    public void setArgazkiaUrl(String argazkiaUrl) { this.argazkiaUrl = argazkiaUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
