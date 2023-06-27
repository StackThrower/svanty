package com.svanty.service;

import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class FileStorageService {

    public static String UPLOAD_ROOT_DIR = "upload-dir";

    String[] allowedExt = {
            "bmp",
            "jpg",
            "gif",
            "jpeg",
            "png"
    };

    public String getFileName(String caption, String fileExt) throws RuntimeException {

        if (fileExt == null) return "";

        if(!Arrays.asList(allowedExt).contains(fileExt.toLowerCase()))
            throw new RuntimeException("This extention not allowed");


        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md.update(caption.getBytes());

        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest);

        return hash + "-" + System.currentTimeMillis() + "." + fileExt;
    }

    public void store(Path location, String fullFileName, String data64) {

        if(!Files.exists(location)) {
            try {
                Files.createDirectories(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] decodedBytes = Base64.getDecoder().decode(data64);

        if (decodedBytes.length > 0) {

            try {
                Files.copy(new ByteArrayInputStream(decodedBytes), location.resolve(fullFileName),
                        new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   }

}
