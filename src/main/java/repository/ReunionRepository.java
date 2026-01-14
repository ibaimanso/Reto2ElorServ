package repository;

import entidades.Reuniones;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para acceso a datos de Reuniones.
 */
public class ReunionRepository {
    
    /**
     * Guarda una nueva reunión.
     */
    public Reuniones save(Reuniones reunion) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            reunion.setCreatedAt(LocalDateTime.now());
            reunion.setUpdatedAt(LocalDateTime.now());
            
            session.save(reunion);
            transaction.commit();
            return reunion;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al guardar reunión: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza una reunión existente.
     */
    public boolean update(Reuniones reunion) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            reunion.setUpdatedAt(LocalDateTime.now());
            
            session.update(reunion);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al actualizar reunión: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca una reunión por ID.
     */
    public Reuniones findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Reuniones.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar reunión por ID: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todas las reuniones de un usuario (como profesor o como alumno).
     */
    public List<Reuniones> findByUserId(Integer userId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.profesor.id = :userId OR r.alumno.id = :userId " +
                "ORDER BY r.fecha DESC", Reuniones.class);
            query.setParameter("userId", userId);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener reuniones del usuario: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene reuniones de un profesor en un rango de fechas.
     * Útil para verificar conflictos.
     */
    public List<Reuniones> findByProfesorAndDateRange(Integer profesorId, 
                                                       LocalDateTime startDate, 
                                                       LocalDateTime endDate) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reuniones> query = session.createQuery(
                "FROM Reuniones r WHERE r.profesor.id = :profesorId " +
                "AND r.fecha BETWEEN :startDate AND :endDate " +
                "AND r.estado != 'CANCELADA'", Reuniones.class);
            query.setParameter("profesorId", profesorId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al verificar conflictos: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina una reunión (soft delete cambiando estado a BORRADA).
     */
    public boolean delete(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Reuniones reunion = session.get(Reuniones.class, id);
            if (reunion != null) {
                session.delete(reunion);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al eliminar reunión: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
}
