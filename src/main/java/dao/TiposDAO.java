package dao;

import entidades.Tipos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Tipos.
 */
public class TiposDAO {
    
    /**
     * Crea un nuevo tipo en la base de datos.
     */
    public Tipos create(Tipos tipo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(tipo);
            
            transaction.commit();
            SocketLogger.debug("Tipo creado: " + tipo.getName());
            return tipo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear tipo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un tipo por su ID.
     */
    public Tipos findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Tipos.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar tipo por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un tipo por nombre.
     */
    public Tipos findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tipos> query = session.createQuery(
                "FROM Tipos t WHERE t.name = :name", Tipos.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar tipo por nombre", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los tipos.
     */
    public List<Tipos> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Tipos> query = session.createQuery("FROM Tipos", Tipos.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los tipos", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un tipo existente.
     */
    public Tipos update(Tipos tipo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(tipo);
            
            transaction.commit();
            SocketLogger.debug("Tipo actualizado: " + tipo.getName());
            return tipo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar tipo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina un tipo por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Tipos tipo = session.get(Tipos.class, id);
            if (tipo != null) {
                session.delete(tipo);
                transaction.commit();
                SocketLogger.debug("Tipo eliminado: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar tipo", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de tipos.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(t) FROM Tipos t", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar tipos", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene el tipo "profesor".
     */
    public Tipos getTipoProfesor() {
        return findByName("profesor");
    }
    
    /**
     * Obtiene el tipo "alumno".
     */
    public Tipos getTipoAlumno() {
        return findByName("alumno");
    }
    
    /**
     * Obtiene el tipo "administrador".
     */
    public Tipos getTipoAdministrador() {
        return findByName("administrador");
    }
    
    /**
     * Obtiene el tipo "god".
     */
    public Tipos getTipoGod() {
        return findByName("god");
    }
}
