package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    void whenFormatDateCorrect() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String dateString = "2022-03-17T14:10:36+03:00";
        LocalDateTime dateTime = parser.parse(dateString);
        System.out.println(dateTime);
        assertThat(dateTime).isEqualTo("2022-03-17T14:10:36");
    }

    @Test
    void whenStringIsNull() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        assertThatThrownBy(() -> parser.parse(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void whenFormatDateIsIncorrect() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String dateString = "textee";
        assertThatThrownBy(() -> parser.parse(dateString)).isInstanceOf(DateTimeParseException.class);
    }
}