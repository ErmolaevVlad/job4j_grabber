package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO post(title, link, description, time) "
                             + "values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, new Timestamp(post.getTime()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement =
                connection.prepareStatement(" SELECT * FROM post")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                posts.add(new Post(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("description"),
                        rs.getTimestamp("time").getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(" SELECT * FROM post WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                post = new Post(rs.getLong("id"), rs.getString("title"),
                        rs.getString("link"), rs.getString("description"),
                        rs.getTimestamp("time").getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(post);
    }
}
