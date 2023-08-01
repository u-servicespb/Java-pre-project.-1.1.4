package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import java.sql.SQLException;
import static jm.task.core.jdbc.util.Util.closeConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable(); // Создание таблицы User(ов)

        userService.saveUser("Name1", "LastName1", (byte) 20); // Добавление 4 User(ов) в таблицу с данными на свой выбор.
        userService.saveUser("Name2", "LastName2", (byte) 25); // После каждого добавления должен быть вывод в консоль
        userService.saveUser("Name3", "LastName3", (byte) 31); // ( User с именем – name добавлен в базу данных )
        userService.saveUser("Name4", "LastName4", (byte) 38);

        userService.getAllUsers(); // Получение всех User из базы и вывод в консоль ( должен быть переопределен toString
                               // в классе User)

        userService.removeUserById(1); // Удаление User из таблицы ( по id )

        userService.cleanUsersTable(); // Очистка таблицы User(ов)
        userService.dropUsersTable();  // Удаление таблицы

        closeConnection(); // закрываем соединение с БД
    }
}
