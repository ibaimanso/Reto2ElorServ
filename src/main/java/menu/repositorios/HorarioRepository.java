package menu.repositorios;

import menu.dto.HorarioDTO;
import menu.modelo.Horario;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    @Query("""
        SELECT new menu.dto.HorarioDTO(
            h.dia,
            h.hora,
            h.modulo.nombre
        )
        FROM Horario h
        WHERE h.profesor.id = :id
        ORDER BY h.dia, h.hora
    """)
    List<HorarioDTO> horarioProfesor(@Param("id") Integer id);

    @Query("""
        SELECT new menu.dto.HorarioDTO(
            h.dia,
            h.hora,
            MIN(m.nombre)
        )
        FROM Horario h
        JOIN h.modulo m
        JOIN Matriculacion mat ON mat.ciclo = m.ciclo
        WHERE mat.alumno.id = :alumnoId
          AND m.curso = mat.curso
          AND m.nombre NOT IN ('Tutoria','Guardia')
        GROUP BY h.dia, h.hora
        ORDER BY h.dia, h.hora
    """)
    List<HorarioDTO> horarioAlumno(@Param("alumnoId") Integer alumnoId);
}
