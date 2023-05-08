package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();
        userService.saveUser("Tom", "test1", (byte) 35);
        userService.saveUser("Mike", "test2", (byte) 30);
        userService.saveUser("John", "test3", (byte) 25);
        userService.saveUser("Katy", "test4", (byte) 20);


        userService.getAllUsers();

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
