package dao;

import entidades.Reuniones;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Reuniones.
 */
public class ReunionesDAO {
    
    /**
     * Crea una nueva reunión en la base de datos.
     */
    public Reuniones create(Reuniones reunion) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(reunion);
            
            transaction.commit();
            SocketLogger.debug("Reunión creada: " + reunion.getIdReunion());
            return reunion;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear reunión", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene una reunión por su ID.
     */
    public Reuniones findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Reuniones.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reunión por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todas las reuniones.
     */
    public List<Reuniones> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery("FROM Reuniones ORDER BY fecha DESC", Reuniones.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todas las reuniones", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones por profesor.
     */
    public List<Reuniones> findByProfesor(Integer profesorId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.profesor.id = :profesorId ORDER BY r.fecha DESC", Reuniones.class);
            query.setParameter("profesorId", profesorId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reuniones por profesor", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones por alumno.
     */
    public List<Reuniones> findByAlumno(Integer alumnoId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.alumno.id = :alumnoId ORDER BY r.fecha DESC", Reuniones.class);
            query.setParameter("alumnoId", alumnoId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reuniones por alumno", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones por estado.
     */
    public List<Reuniones> findByEstado(String estado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.estado = :estado ORDER BY r.fecha DESC", Reuniones.class);
            query.setParameter("estado", estado);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reuniones por estado", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones pendientes por profesor.
     */
    public List<Reuniones> findPendientesByProfesor(Integer profesorId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.profesor.id = :profesorId AND r.estado = 'pendiente' ORDER BY r.fecha", 
                Reuniones.class);
            query.setParameter("profesorId", profesorId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reuniones pendientes por profesor", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones por rango de fechas.
     */
    public List<Reuniones> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fecha", 
                Reuniones.class);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar reuniones por rango de fechas", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza una reunión existente.
     */
    public Reuniones update(Reuniones reunion) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(reunion);
            
            transaction.commit();
            SocketLogger.debug("Reunión actualizada: " + reunion.getIdReunion());
            return reunion;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar reunión", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina una reunión por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Reuniones reunion = session.get(Reuniones.class, id);
            if (reunion != null) {
                session.delete(reunion);
                transaction.commit();
                SocketLogger.debug("Reunión eliminada: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar reunión", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de reuniones.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(r) FROM Reuniones r", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar reuniones", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
