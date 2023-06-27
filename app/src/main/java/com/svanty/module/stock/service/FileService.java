package com.svanty.module.stock.service;

import net.coobird.thumbnailator.Thumbnails;

import java.util.List;
import java.util.Random;

public class FileService {

    public static boolean isImage(String filename) {

        String[] oldFileNameParts = filename.split("\\.");

        List<String> exts = List.of("bmp", "jpg", "png", "jpeg");

        return  exts.contains(oldFileNameParts[1].toLowerCase());

    }

    public static void resizeImage(String path, String newFile, int targetWidth, int targetHeight) throws Exception {

        Thumbnails.of(path)
                .width(targetWidth)
                .outputQuality(1)
                .toFile(newFile);
    }

    public static String generateRandomFileName(String oldFileName, int size) {

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = size;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return  generatedString + "." + getFileExtension(oldFileName);
    }


    public static String getFileExtension(String filename) {
        String[] oldFileNameParts = filename.split("\\.");

        return  oldFileNameParts[1];

    }

}
