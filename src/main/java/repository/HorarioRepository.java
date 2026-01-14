package repository;

import entidades.Horarios;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Repositorio para acceso a datos de Horarios.
 */
public class HorarioRepository {
    
    /**
     * Obtiene todos los horarios de un profesor por su ID.
     */
    public List<Horarios> findByProfesorId(Integer profesorId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.profesor.id = :profesorId ORDER BY h.dia, h.hora", 
                Horarios.class);
            query.setParameter("profesorId", profesorId);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener horarios del profesor: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un horario por ID.
     */
    public Horarios findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Horarios.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar horario por ID: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los horarios.
     */
    public List<Horarios> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h ORDER BY h.dia, h.hora", Horarios.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los horarios: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horarios de un alumno basándose en sus matrículas.
     * El horario de un alumno es el de los profesores de sus módulos matriculados.
     */
    public List<Horarios> findByAlumnoId(Integer alumnoId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // HQL complejo: obtener horarios de los módulos en los que está matriculado el alumno
            String hql = "SELECT h FROM Horarios h " +
                        "JOIN h.modulo m " +
                        "JOIN Matriculaciones mat ON mat.ciclo.id = m.ciclo.id " +
                        "WHERE mat.alumno.id = :alumnoId " +
                        "AND m.curso = mat.curso " +
                        "ORDER BY h.dia, h.hora";
            
            Query<Horarios> query = session.createQuery(hql, Horarios.class);
            query.setParameter("alumnoId", alumnoId);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener horarios del alumno: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}
