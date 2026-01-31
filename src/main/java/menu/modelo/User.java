package menu.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Integer id;

    private String email;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String argazkia_url;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // ===== RELACIÓN TIPO (NO SE SERIALIZA) =====
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipo tipo;

    // ===== RELACIONES QUE NO VAN A ANDROID =====
    @JsonIgnore
    @OneToMany(mappedBy = "profesor")
    private List<Horario> horariosProfesor;

    @JsonIgnore
    @OneToMany(mappedBy = "alumno")
    private List<Matriculacion> matriculaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "profesor")
    private List<Reunion> reunionesProfesor;

    @JsonIgnore
    @OneToMany(mappedBy = "alumno")
    private List<Reunion> reunionesAlumno;

    // ===== GETTERS / SETTERS =====

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

    public String getArgazkia_url() {
        return argazkia_url;
    }

    public void setArgazkia_url(String argazkia_url) {
        this.argazkia_url = argazkia_url;
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

    // ===== CAMPO VIRTUAL PARA ANDROID =====
    @JsonProperty("tipo_id")
    public Integer getTipoId() {
        return tipo != null ? tipo.getId() : null;
    }

    // ===== GETTERS INTERNOS (NO JSON) =====
    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
