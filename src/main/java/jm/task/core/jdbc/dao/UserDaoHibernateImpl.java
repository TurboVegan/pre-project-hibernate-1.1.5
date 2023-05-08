package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class UserDaoHibernateImpl implements UserDao {

    Transaction transaction = null;

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id int auto_increment primary key," +
                "name varchar(40) null, lastName varchar(40) null, age tinyint null);";

        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery(sql, User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery(sql, User.class).executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.persist(user);

            transaction.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            session.remove(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List <User> list = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            list = session.createQuery("FROM User", User.class).stream().peek(System.out::println).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "truncate users";

        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery(sql, User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            System.err.printf("COULDN'T TRUNCATE TABLE " + e.getCause());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}