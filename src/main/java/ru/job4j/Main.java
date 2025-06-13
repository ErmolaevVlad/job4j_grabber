package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(SchedulerManager.class);

    public static void main(String[] args) {
        var config = new Config();
        config.load("C:\\projects\\job4j_grabber\\src\\main\\resources\\application.properties");
        try (var connection = DriverManager.getConnection(
                config.get("db.url"),
                config.get("db.username"),
                config.get("db.password"))
        ) {
            var store = new JdbcStore(connection);
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}
