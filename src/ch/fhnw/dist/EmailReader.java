package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EmailReader {

    private String source;
    private EmailType emailType;

    public EmailReader(String source, EmailType emailType) {
        this.source = source;
        this.emailType = emailType;
    }

    public List<Email> getEmails() {
        File[] allFiles = new File(source).listFiles();

        List<Email> emails = new ArrayList();

        for (File file : allFiles) {

            Set<String> words = new HashSet();
            BufferedReader bf;
            try {
                bf = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()), "utf-8"));

                String line;
                while ((line = bf.readLine()) != null) {
                    String[] wordsOfLine = line.split(" ");
                    words.addAll(Arrays.asList(wordsOfLine));
                }
                emails.add(new Email(emailType, words));
            } catch (IOException ex) {
                System.out.println("fuck");
            }

        }
        return emails;
    }
}
