package jm.task.core.jdbc.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() throws SQLException {

    }
    private static final Connection connection = Util.getconnection();
    public void createUsersTable() throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("CREATE TABLE IF NOT EXISTS preproject.users" +
                        "( id int NOT NULL AUTO_INCREMENT, " +
                        "name VARCHAR(25), " +
                        "lastName VARCHAR(25), " +
                        "age tinyint, " +
                        "PRIMARY KEY (id))")) {
            preparedStatement.executeUpdate();
            connection.commit(); //а зачем мы сдесь делаем автокомит в курсе Трегулова говорится при сождании и
            //удалении таблиц commit вызывается автоматически и его прописывать не надо(таблица сразу сохраняняется
            //у коллег одногрупников спрашивал ник то не может обьяснить, может можете обьяснить. Я уже третий день сижу с этой задачей
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public void dropUsersTable() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS users")) {
            preparedStatement.execute();
            System.out.println("Delete table");
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("insert into users (name, lastName, age) values (?,?,?)")) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public void removeUserById(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM preproject.users where id = ?")) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if(connection != null) {
                connection.rollback();
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = Util.getconnection()) {
            ResultSet resultSet = connection.prepareStatement("select * from preproject.users").executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                usersList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exeption: " + e.getMessage());
        }
        return usersList;
    }

    public void cleanUsersTable() throws SQLException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("TRUNCATE TABLE users")) {
            preparedStatement.executeUpdate();
            System.out.println("Clear table");
        } catch (SQLException e) {
            if(connection != null) {
                connection.rollback();
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }
}
