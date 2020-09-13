package me.qiooip.lazarus.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class FileUtils {

    public static File getOrCreateFile(File parent, String name) {
        File file = new File(parent, name);

        if(!parent.exists()) parent.mkdir();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static void writeString(File file, String content) {
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(content);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String readWholeFile(File file) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.length() != 0 ? builder.toString() : null;

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Instant creationDate(File file) {
        try {

            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attributes.creationTime().toInstant();

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
