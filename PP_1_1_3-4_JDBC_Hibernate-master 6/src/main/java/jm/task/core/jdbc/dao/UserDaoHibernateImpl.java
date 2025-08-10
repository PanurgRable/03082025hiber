package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static SessionFactory sessionFactory;
    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }



    @Override
    public void createUsersTable() {
        Transaction tx = null;
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50),
                lastName VARCHAR(50),
                age TINYINT
            )
        """;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при создании таблицы пользователей");
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = sessionFactory.openSession())  {
            tx = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении таблицы пользователей");
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);

        try (Session session = sessionFactory.openSession())  {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();

        } catch (Exception e) {
            System.err.println("Ошибка при сохранении пользователя");
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession())  {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователя");
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction tx = null;
        List<User> users = null;
        try (Session session = sessionFactory.openSession())  {
            tx = session.beginTransaction();
            users = session.createQuery("from User", User.class).list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession())  {
            tx = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Ошибка при очистке таблицы пользователей");
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }
    }
}
