package ru.job4j.grabber.model;

import java.util.Objects;

public class Post {
    private Long id;
    private String title;
    private String link;
    private String description;
    private Long time;

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", time=" + time
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post post)) {
            return false;
        }
        return Objects.equals(id, post.id) && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
