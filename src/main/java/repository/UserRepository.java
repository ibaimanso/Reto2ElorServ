package repository;

import entidades.Users;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Repositorio para acceso a datos de Users.
 * Usa Hibernate para las operaciones CRUD.
 */
public class UserRepository {
    
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
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Busca un usuario por ID.
     */
    public Users findById(Integer id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Users.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los alumnos (tipo_id = 4 según tu BD).
     */
    public List<Users> findAllAlumnos() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u JOIN FETCH u.tipo t WHERE t.name = 'Alumno'", Users.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Obtiene todos los profesores.
     */
    public List<Users> findAllProfesores() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Users> query = session.createQuery(
                "FROM Users u JOIN FETCH u.tipo t WHERE t.name = 'Profesor'", Users.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error al obtener profesores: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Actualiza un usuario.
     */
    public boolean update(Users user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Guarda un nuevo usuario.
     */
    public Users save(Users user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}
