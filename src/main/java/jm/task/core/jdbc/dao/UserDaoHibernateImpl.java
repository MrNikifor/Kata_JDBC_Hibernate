package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getConnectionHibernate();

    public UserDaoHibernateImpl() {


    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                "  name VARCHAR(255),\n" +
                "  lastName VARCHAR(255),\n" +
                "  age TINYINT\n" +
                ")";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(sql);
            session.getTransaction().commit();
            System.out.println("Таблица создана!");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS kata.users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
                System.out.println("Таблица удалена");
            } catch (HibernateException e) {
                transaction.rollback();
                e.printStackTrace();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(new User(name, lastName, age));
                transaction.commit();
                System.out.println("User с именем – " + name + " добавлен в базу данных");
            }catch (HibernateException e){
                transaction.rollback();
                e.printStackTrace();;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(session.get(User.class, id));
                transaction.commit();
                System.out.println("User удален");
            }catch (HibernateException e){
                transaction.rollback();
                e.printStackTrace();;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        criteriaQuery.from(User.class);
        Transaction transaction = session.beginTransaction();
        List<User> userList = session.createQuery(criteriaQuery).getResultList();
        try {
            transaction.commit();
            return userList;
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery("TRUNCATE TABLE kata.users;").executeUpdate();
                transaction.commit();
                System.out.println("Таблица очищена");
            }catch (HibernateException e){
                transaction.rollback();
                e.printStackTrace();;
            }
        } catch (HibernateException e) {
            e.printStackTrace();

        }
    }
}
