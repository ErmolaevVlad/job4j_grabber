package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGE_NUMBER = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        for (int index = 1; index <= PAGE_NUMBER; index++) {
            try {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, index, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var dataElement = row.select(".basic-date").first();
                    String date = dataElement.attr("datetime");
                    var titleElement = row.select(".vacancy-card__title").first();
                    var linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    String description = retrieveDescription(link);
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setDescription(description);
                    post.setTime(ZonedDateTime.of(new HabrCareerDateTimeParser().parse(date), ZoneId.systemDefault())
                            .toInstant().toEpochMilli());
                    result.add(post);
                });
            } catch (IOException e) {
                LOG.error("When load page", e);
            }
        }
        return result;
    }

    private String retrieveDescription(String link) {
        String rsl;
        try {
            var connection = Jsoup.connect(link);
            var document = connection.get();
            var rows = document.select(".style-ugc");
            rsl = rows.text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rsl;
    }
}