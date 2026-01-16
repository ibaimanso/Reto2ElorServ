package dao;

import entidades.Modulos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Modulos.
 */
public class ModulosDAO {
    
    /**
     * Crea un nuevo módulo en la base de datos.
     */
    public Modulos create(Modulos modulo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(modulo);
            
            transaction.commit();
            SocketLogger.debug("Módulo creado: " + modulo.getNombre());
            return modulo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear módulo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un módulo por su ID.
     */
    public Modulos findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Modulos.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar módulo por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los módulos.
     */
    public List<Modulos> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Modulos> query = session.createQuery("FROM Modulos", Modulos.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los módulos", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene módulos por ciclo.
     */
    public List<Modulos> findByCiclo(Integer cicloId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Modulos> query = session.createQuery(
                "FROM Modulos m WHERE m.ciclo.id = :cicloId", Modulos.class);
            query.setParameter("cicloId", cicloId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar módulos por ciclo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene módulos por ciclo y curso.
     */
    public List<Modulos> findByCicloAndCurso(Integer cicloId, Byte curso) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Modulos> query = session.createQuery(
                "FROM Modulos m WHERE m.ciclo.id = :cicloId AND m.curso = :curso", Modulos.class);
            query.setParameter("cicloId", cicloId);
            query.setParameter("curso", curso);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar módulos por ciclo y curso", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un módulo por nombre.
     */
    public Modulos findByNombre(String nombre) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Modulos> query = session.createQuery(
                "FROM Modulos m WHERE m.nombre = :nombre", Modulos.class);
            query.setParameter("nombre", nombre);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar módulo por nombre", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un módulo existente.
     */
    public Modulos update(Modulos modulo) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(modulo);
            
            transaction.commit();
            SocketLogger.debug("Módulo actualizado: " + modulo.getNombre());
            return modulo;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar módulo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina un módulo por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Modulos modulo = session.get(Modulos.class, id);
            if (modulo != null) {
                session.delete(modulo);
                transaction.commit();
                SocketLogger.debug("Módulo eliminado: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar módulo", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de módulos.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(m) FROM Modulos m", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar módulos", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
