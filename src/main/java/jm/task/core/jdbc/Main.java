package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Коля", "Лагутин", (byte) 38);
        userService.saveUser("Серегей", "Маркушев", (byte) 32);
        userService.saveUser("Мария", "Лысенкова", (byte) 21);
        userService.saveUser("Виолетта", "Осинская", (byte) 8);

        userService.getAllUsers();

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
