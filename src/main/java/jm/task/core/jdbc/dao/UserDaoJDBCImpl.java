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
    public void createUsersTable() throws SQLException {
        try (Connection connection = Util.getconnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS preproject.users" +
                            "( id int NOT NULL AUTO_INCREMENT, " +
                            "name VARCHAR(25), " +
                            "lastName VARCHAR(25), " +
                            "age tinyint, " +
                            "PRIMARY KEY (id))")
                    .execute();
        } catch (SQLException e) {
            System.out.println("SQL Exeption: " + e.getMessage());
        }

    }

    public void dropUsersTable() throws SQLException {
        try (Connection connection = Util.getconnection()) {
            connection.prepareStatement("DROP TABLE IF EXISTS users").execute();
            System.out.println("Delete table");
        } catch (SQLException e) {
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        Connection connection = null;
        try {
            connection = Util.getconnection();
            String sql = "insert into users (name, lastName, age) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();    //не получается использовать роллбэк в трай с ресурсами. Ролбэк нужен только в двух методах как я понял
            System.out.println("SQL Exeption: " + e.getMessage());
        } finally {
            connection.close();
        }
    }

    public void removeUserById(long id) throws SQLException {
        Connection connection = null;
        try {
            connection = Util.getconnection();
            String sql = "DELETE FROM preproject.users where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();  //  не получается использовать роллбэк в трай с ресурсами
            System.out.println("SQL Exeption: " + e.getMessage());
        } finally {
            connection.close();
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
        try (Connection connection = Util.getconnection()) {
            connection.prepareStatement("TRUNCATE TABLE users").executeUpdate();
            System.out.println("Clear table");
        } catch (SQLException e) {
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }
}
