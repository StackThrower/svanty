package com.svanty.library.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svanty.constans.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;


import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;

public class Translator {

    static final String KEY = "key";

    @Data
    @AllArgsConstructor
    static class TranslateRequestObject {
        String q;
        //        String source;
        String target;
        String format;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class TranslateResponseTranslationObject {
        String translatedText;
        String detectedSourceLanguage;
    }


    @Data
    @NoArgsConstructor
    static class TranslateResponseTranslationsObject {
        List<TranslateResponseTranslationObject> translations;
    }

    @Data
    @NoArgsConstructor
    static class TranslateResponseDataObject {
        TranslateResponseTranslationsObject data;
    }


    @Data
    @NoArgsConstructor
    static class TranslateResponseObject {
        String q;
        //        String source;
        String target;
        String format;
    }

    public static String translate(String textInput, Language language) {

        ObjectMapper objectMapper = new ObjectMapper();
        TranslateRequestObject translateObject = new TranslateRequestObject(textInput, language.languageCode, "text");

        try {
            String text = objectMapper.writeValueAsString(translateObject);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://translation.googleapis.com/language/translate/v2?key=" + KEY))
                    .header("Content-Type", "application/json")
                    .POST(ofInputStream(() -> new ByteArrayInputStream(text.getBytes())))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            TranslateResponseDataObject resultObj = objectMapper.readValue(response.body(), new TypeReference<TranslateResponseDataObject>(){});

            return (resultObj != null) ?
                resultObj.getData()
                        .getTranslations()
                        .stream()
                        .findFirst()
                        .orElse(new TranslateResponseTranslationObject(textInput, ""))
                        .getTranslatedText()
                : textInput;


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }
}
