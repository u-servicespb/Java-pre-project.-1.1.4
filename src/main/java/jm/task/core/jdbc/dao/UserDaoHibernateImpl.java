package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.createNativeQuery(
                            "CREATE TABLE IF NOT EXISTS users" +
                            "  id       BIGINT       PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "  name     VARCHAR(250) DEFAULT NULL," +
                            "  lastName VARCHAR(250) DEFAULT NULL," +
                            "  age      TINYINT      DEFAULT NULL)");
            session.beginTransaction().commit();
            System.out.println("Таблица создана");
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
            session.createNativeQuery("DROP TABLE IF EXISTS users");
            session.beginTransaction().commit();
            System.out.println("Таблица удалена");
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
            System.out.println("User с именем – " + name + " добавлен в базу данных");
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
            System.out.println("User удален");
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
            session.createNativeQuery("TRUNCATE TABLE users;");
            session.beginTransaction().commit();
            System.out.println("Таблица очищена");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (sessionFactory.openSession().beginTransaction() != null) {
                sessionFactory.openSession().beginTransaction().rollback();
            }
        }
    }
}