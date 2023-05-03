package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        User user = new User();
        UserService userService = new UserServiceImpl();
  //      UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Name1", "LastName1", (byte) 20);
        user = userService.getAllUsers().get(0);
        System.out.println("User с именем - " + user.getName() + " добавлен в базу данных.");
        userService.saveUser("Name2", "LastName2", (byte) 25);
        System.out.println("User с именем - " + userService.getAllUsers().get(1).getName() + " добавлен в базу данных.");
        userService.saveUser("Name3", "LastName3", (byte) 31);
        System.out.println("User с именем - " + userService.getAllUsers().get(2).getName() + " добавлен в базу данных.");
        userService.saveUser("Name4", "LastName4", (byte) 38);
        System.out.println("User с именем - " + userService.getAllUsers().get(3).getName() + " добавлен в базу данных.");
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
