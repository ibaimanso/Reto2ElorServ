package menu.dto;

public class HorarioDTO {

    private String dia;
    private Integer hora;
    private String modulo;

    public HorarioDTO(String dia, Integer hora, String modulo) {
        this.dia = dia;
        this.hora = hora;
        this.modulo = modulo;
    }

    public String getDia() { return dia; }
    public Integer getHora() { return hora; }
    public String getModulo() { return modulo; }
}

