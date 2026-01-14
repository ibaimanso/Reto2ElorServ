package dto;

import java.io.Serializable;

/**
 * DTO para transferir información de usuario.
 * NO incluye la contraseña por seguridad.
 */
public class UserDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String email;
    private String username;
    private String nombre;
    private String apellidos;
    private String dni;
    private String direccion;
    private String telefono1;
    private String telefono2;
    private String argazkiaUrl;
    private String tipoId;  // ID del tipo de usuario
    private String tipoNombre;  // Nombre del tipo (PROFESOR, ALUMNO, etc.)
    
    // Constructores
    public UserDTO() {
    }
    
    public UserDTO(Integer id, String email, String nombre, String apellidos, String tipoNombre) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipoNombre = tipoNombre;
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
    
    public String getTipoId() {
        return tipoId;
    }
    
    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }
    
    public String getTipoNombre() {
        return tipoNombre;
    }
    
    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tipoNombre='" + tipoNombre + '\'' +
                '}';
    }
}
