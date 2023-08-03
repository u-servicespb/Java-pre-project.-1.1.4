package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transaction;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = getSessionFactory();
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users " +
                    "(id INT NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                    "age INT NOT NULL)").addEntity(User.class);
            session.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.createSQLQuery("DROP TABLE IF EXISTS users");
            session.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession().beginTransaction().rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.save(new User(name, lastName, age));
            session.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession().beginTransaction().rollback();
            }

        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession();) {
            session.delete(session.get(User.class, id));
            session.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession().beginTransaction().rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        CriteriaQuery<User> criteriaQuery = sessionFactory.openSession().getCriteriaBuilder().createQuery(User.class);
        criteriaQuery.from(User.class);
        List<User> userList = sessionFactory.openSession().createQuery(criteriaQuery).getResultList();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction().commit();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
            sessionFactory.openSession().beginTransaction().rollback();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.createSQLQuery("TRUNCATE TABLE IF EXISTS users;");
            session.beginTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession().beginTransaction().rollback();
            }
        }
    }
}