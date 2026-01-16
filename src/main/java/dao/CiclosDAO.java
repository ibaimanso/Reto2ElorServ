package dao;

import entidades.Ciclos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Ciclos.
 */
public class CiclosDAO {
    
    /**
     * Crea un nuevo ciclo en la base de datos.
     */
    public Ciclos create(Ciclos ciclo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(ciclo);
            
            transaction.commit();
            SocketLogger.debug("Ciclo creado: " + ciclo.getNombre());
            return ciclo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear ciclo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un ciclo por su ID.
     */
    public Ciclos findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Ciclos.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar ciclo por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un ciclo por su nombre.
     */
    public Ciclos findByNombre(String nombre) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Ciclos> query = session.createQuery(
                "FROM Ciclos c WHERE c.nombre = :nombre", Ciclos.class);
            query.setParameter("nombre", nombre);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar ciclo por nombre", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los ciclos.
     */
    public List<Ciclos> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Ciclos> query = session.createQuery("FROM Ciclos", Ciclos.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los ciclos", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un ciclo existente.
     */
    public Ciclos update(Ciclos ciclo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(ciclo);
            
            transaction.commit();
            SocketLogger.debug("Ciclo actualizado: " + ciclo.getNombre());
            return ciclo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar ciclo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina un ciclo por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Ciclos ciclo = session.get(Ciclos.class, id);
            if (ciclo != null) {
                session.delete(ciclo);
                transaction.commit();
                SocketLogger.debug("Ciclo eliminado: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar ciclo", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de ciclos.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Ciclos c", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar ciclos", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
