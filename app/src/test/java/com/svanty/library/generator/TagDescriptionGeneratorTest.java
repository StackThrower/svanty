package com.svanty.library.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagDescriptionGeneratorTest {

    @Test
    void getCorrectDescriptionLength() {

        TagDescriptionGenerator tagDescriptionGenerator = new TagDescriptionGenerator();

        String newString = tagDescriptionGenerator.getCorrectDescriptionLength("Будущие стоковые изображения готовы — «Плотина». ✓ Популярные фото с тегами: #вода, #большой, #горизонт. Цветовые коды для вашего искусства: #6AA0E8, #445871, #181C27...");

        System.out.printf(newString);

        assertEquals(newString.length(), 143);


    }
}