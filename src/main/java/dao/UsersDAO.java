package dao;

import entidades.Users;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;
import util.SocketLogger;

import java.util.List;

/**
 * DAO para operaciones CRUD sobre la entidad Users.
 */
public class UsersDAO {
    
    /**
     * Crea un nuevo usuario en la base de datos.
     */
    public Users create(Users user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(user);
            
            transaction.commit();
            SocketLogger.debug("Usuario creado: " + user.getUsername());
            return user;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al crear usuario", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene un usuario por su ID.
     */
    public Users findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Users.class, id);
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuario por ID: " + id, e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un usuario por email.
     */
    public Users findByEmail(String email) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.email = :email", Users.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuario por email", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un usuario por username.
     */
    public Users findByUsername(String username) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuario por username", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un usuario por DNI.
     */
    public Users findByDni(String dni) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.dni = :dni", Users.class);
            query.setParameter("dni", dni);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuario por DNI", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los usuarios.
     */
    public List<Users> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery("FROM Users", Users.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener todos los usuarios", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene usuarios por tipo.
     */
    public List<Users> findByTipo(Integer tipoId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.tipo.id = :tipoId", Users.class);
            query.setParameter("tipoId", tipoId);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuarios por tipo", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los profesores (tipo_id = 3).
     */
    public List<Users> findAllProfesores() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.tipo.id = 3", Users.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener profesores", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los alumnos (tipo_id = 4).
     */
    public List<Users> findAllAlumnos() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.tipo.id = 4", Users.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener alumnos", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los administradores (tipo_id = 2).
     */
    public List<Users> findAllAdministradores() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.tipo.id = 2", Users.class);
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al obtener administradores", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca usuarios por nombre o apellidos (búsqueda parcial).
     */
    public List<Users> searchByNombreOrApellidos(String searchTerm) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE LOWER(u.nombre) LIKE :search OR LOWER(u.apellidos) LIKE :search", 
                Users.class);
            query.setParameter("search", "%" + searchTerm.toLowerCase() + "%");
            return query.list();
        } catch (Exception e) {
            SocketLogger.error("Error al buscar usuarios por nombre/apellidos", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Valida credenciales de usuario (login).
     */
    public Users validateCredentials(String username, String password) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u WHERE u.username = :username AND u.password = :password", Users.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al validar credenciales", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Verifica si un email ya está registrado.
     */
    public boolean existsByEmail(String email) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM Users u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            SocketLogger.error("Error al verificar existencia de email", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Verifica si un username ya está registrado.
     */
    public boolean existsByUsername(String username) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM Users u WHERE u.username = :username", Long.class);
            query.setParameter("username", username);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            SocketLogger.error("Error al verificar existencia de username", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un usuario existente.
     */
    public Users update(Users user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(user);
            
            transaction.commit();
            SocketLogger.debug("Usuario actualizado: " + user.getUsername());
            return user;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al actualizar usuario", e);
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Elimina un usuario por su ID.
     */
    public boolean delete(Integer id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Users user = session.get(Users.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                SocketLogger.debug("Usuario eliminado: " + id);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            SocketLogger.error("Error al eliminar usuario", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta el número total de usuarios.
     */
    public Long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM Users u", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar usuarios", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Cuenta usuarios por tipo.
     */
    public Long countByTipo(Integer tipoId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM Users u WHERE u.tipo.id = :tipoId", Long.class);
            query.setParameter("tipoId", tipoId);
            return query.uniqueResult();
        } catch (Exception e) {
            SocketLogger.error("Error al contar usuarios por tipo", e);
            return 0L;
        } finally {
            if (session != null) session.close();
        }
    }
}
