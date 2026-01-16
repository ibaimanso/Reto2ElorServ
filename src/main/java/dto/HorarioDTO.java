package dto;

import java.io.Serializable;

/**
 * DTO para transferir información de horarios.
 */
public class HorarioDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String dia; 
    private Byte hora;  
    private String aula;
    private String observaciones;
    
    private Integer profesorId;
    private String profesorNombre;
    private String profesorApellidos;
    
    private Integer moduloId;
    private String moduloNombre;
    private String moduloNombreEus;
    
    public HorarioDTO() {
    }
    
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
    
    public Integer getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }
    
    public String getProfesorNombre() {
        return profesorNombre;
    }
    
    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }
    
    public String getProfesorApellidos() {
        return profesorApellidos;
    }
    
    public void setProfesorApellidos(String profesorApellidos) {
        this.profesorApellidos = profesorApellidos;
    }
    
    public Integer getModuloId() {
        return moduloId;
    }
    
    public void setModuloId(Integer moduloId) {
        this.moduloId = moduloId;
    }
    
    public String getModuloNombre() {
        return moduloNombre;
    }
    
    public void setModuloNombre(String moduloNombre) {
        this.moduloNombre = moduloNombre;
    }
    
    public String getModuloNombreEus() {
        return moduloNombreEus;
    }
    
    public void setModuloNombreEus(String moduloNombreEus) {
        this.moduloNombreEus = moduloNombreEus;
    }
    
    @Override
    public String toString() {
        return "HorarioDTO{" +
                "id=" + id +
                ", dia='" + dia + '\'' +
                ", hora=" + hora +
                ", aula='" + aula + '\'' +
                ", moduloNombre='" + moduloNombre + '\'' +
                '}';
    }
}
