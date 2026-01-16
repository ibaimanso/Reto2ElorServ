package dao;

import entidades.Horarios;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Horarios.
 */
public class HorariosDAO {
    
    /**
     * Crea un nuevo horario en la base de datos.
     */
    public Horarios create(Horarios horario) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(horario);
            
            transaction.commit();
            SocketLogger.debug("Horario creado: " + horario.getId());
            return horario;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear horario", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un horario por su ID.
     */
    public Horarios findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Horarios.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horario por ID: " + id, e);
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
            Query<Horarios> query = session.createQuery("FROM Horarios", Horarios.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los horarios", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horarios por profesor.
     */
    public List<Horarios> findByProfesor(Integer profesorId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.profesor.id = :profesorId ORDER BY h.dia, h.hora", Horarios.class);
            query.setParameter("profesorId", profesorId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horarios por profesor", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horarios por módulo.
     */
    public List<Horarios> findByModulo(Integer moduloId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.modulo.id = :moduloId ORDER BY h.dia, h.hora", Horarios.class);
            query.setParameter("moduloId", moduloId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horarios por módulo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horarios por día.
     */
    public List<Horarios> findByDia(String dia) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.dia = :dia ORDER BY h.hora", Horarios.class);
            query.setParameter("dia", dia);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horarios por día", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horarios por aula.
     */
    public List<Horarios> findByAula(String aula) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.aula = :aula ORDER BY h.dia, h.hora", Horarios.class);
            query.setParameter("aula", aula);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horarios por aula", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene horario específico por día, hora y aula (para verificar disponibilidad).
     */
    public Horarios findByDiaHoraAula(String dia, Byte hora, String aula) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Horarios> query = session.createQuery(
                "FROM Horarios h WHERE h.dia = :dia AND h.hora = :hora AND h.aula = :aula", Horarios.class);
            query.setParameter("dia", dia);
            query.setParameter("hora", hora);
            query.setParameter("aula", aula);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar horario por día/hora/aula", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un horario existente.
     */
    public Horarios update(Horarios horario) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(horario);
            
            transaction.commit();
            SocketLogger.debug("Horario actualizado: " + horario.getId());
            return horario;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar horario", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina un horario por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Horarios horario = session.get(Horarios.class, id);
            if (horario != null) {
                session.delete(horario);
                transaction.commit();
                SocketLogger.debug("Horario eliminado: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar horario", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de horarios.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(h) FROM Horarios h", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar horarios", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
