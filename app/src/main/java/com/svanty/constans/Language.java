package com.svanty.constans;

import java.util.Arrays;

public enum Language {

    ARABIC("ar", "اللغة العربية","Arabic", "", true),
    BENGALI("bn", "বাংলা","Bengali", "", false),
    GERMAN("de", "Deutsch","German", "", false),
    ENGLISH("en", "English","English", "", true),
    SPANISH("es", "español","Spanish", "", false),
    PERSIAN("fa", "فارسی","Persian", "", false),
    FRENCH("fr", "français","French", "", false),
    GUJARATI("gu", "ગુજરાતી","Gujarati", "", false),
    HINDI("hi", "नहीं।","Hindi", "", false),
    ITALIAN("it", "Italiana","Italian", "", false),
    HEBREW("iw", "עִברִית","Hebrew", "", false),
    JAPANESE("ja", "日本","Japanese", "", false),
    JAVANESE("jv", "basa jawa","Javanese", "", false),

    KOREAN("ko", "한국인","Korean", "", false),
    MARATHI("ko", "मराठी","Marathi", "", false),
    MALAY("ms", "Melayu","Malay", "", false),
    PORTUGUESE("pt", "Português","Portuguese", "", false),
    RUSSIAN("ru", "Русский","Russian", "", true),
    TAMIL("ta", "தமிழ்","Tamil", "", false),
    TELUGU("te", "కార్ట్","Telugu", "", false),
    TURKISH("tr", "Türkçe","Turkish", "", false),
    UKRAINIAN("uk", "Українська","Ukrainian", "", true),
    URDU("ur", "اردو","Ukrainian", "", false),
    VIETNAMESE("vi", "Tiếng Việt","Vietnamese", "", false),
    CHINESE("zh", "中文","Chinese", "", false);

    public String languageCode;
    public String title;
    public String description;
    public String countryCode;
    public Boolean enabled;

    Language(String languageCode, String title,
                     String description, String countryCode, Boolean enabled) {
        this.languageCode = languageCode;
        this.title = title;
        this.description = description;
        this.countryCode = countryCode;
        this.enabled = enabled;
    }

    public static Language getByCode(String code) {

        return Arrays.stream(Language.values())
                .filter(e -> e.languageCode.equalsIgnoreCase(code))
                .findFirst()
                .orElse(Language.ENGLISH);
    }
}
