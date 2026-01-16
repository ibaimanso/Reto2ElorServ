package dao;

import entidades.Matriculaciones;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Matriculaciones.
 */
public class MatriculacionesDAO {
    
    /**
     * Crea una nueva matriculación en la base de datos.
     */
    public Matriculaciones create(Matriculaciones matriculacion) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(matriculacion);
            
            transaction.commit();
            SocketLogger.debug("Matriculación creada: " + matriculacion.getId());
            return matriculacion;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear matriculación", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene una matriculación por su ID.
     */
    public Matriculaciones findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Matriculaciones.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar matriculación por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todas las matriculaciones.
     */
    public List<Matriculaciones> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Matriculaciones> query = session.createQuery("FROM Matriculaciones", Matriculaciones.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todas las matriculaciones", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene matriculaciones por alumno.
     */
    public List<Matriculaciones> findByAlumno(Integer alumnoId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Matriculaciones> query = session.createQuery(
                "FROM Matriculaciones m WHERE m.alumno.id = :alumnoId", Matriculaciones.class);
            query.setParameter("alumnoId", alumnoId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar matriculaciones por alumno", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene matriculaciones por ciclo.
     */
    public List<Matriculaciones> findByCiclo(Integer cicloId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Matriculaciones> query = session.createQuery(
                "FROM Matriculaciones m WHERE m.ciclo.id = :cicloId", Matriculaciones.class);
            query.setParameter("cicloId", cicloId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar matriculaciones por ciclo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene matriculaciones por ciclo y curso.
     */
    public List<Matriculaciones> findByCicloAndCurso(Integer cicloId, Byte curso) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Matriculaciones> query = session.createQuery(
                "FROM Matriculaciones m WHERE m.ciclo.id = :cicloId AND m.curso = :curso", Matriculaciones.class);
            query.setParameter("cicloId", cicloId);
            query.setParameter("curso", curso);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar matriculaciones por ciclo y curso", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Verifica si un alumno ya está matriculado en un ciclo y curso específico.
     */
    public boolean existsMatriculacion(Integer alumnoId, Integer cicloId, Byte curso) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Matriculaciones m WHERE m.alumno.id = :alumnoId AND m.ciclo.id = :cicloId AND m.curso = :curso", 
                Long.class);
            query.setParameter("alumnoId", alumnoId);
            query.setParameter("cicloId", cicloId);
            query.setParameter("curso", curso);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            SocketLogger.error("Error al verificar existencia de matriculación", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza una matriculación existente.
     */
    public Matriculaciones update(Matriculaciones matriculacion) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(matriculacion);
            
            transaction.commit();
            SocketLogger.debug("Matriculación actualizada: " + matriculacion.getId());
            return matriculacion;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar matriculación", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina una matriculación por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Matriculaciones matriculacion = session.get(Matriculaciones.class, id);
            if (matriculacion != null) {
                session.delete(matriculacion);
                transaction.commit();
                SocketLogger.debug("Matriculación eliminada: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar matriculación", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de matriculaciones.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(m) FROM Matriculaciones m", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar matriculaciones", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
