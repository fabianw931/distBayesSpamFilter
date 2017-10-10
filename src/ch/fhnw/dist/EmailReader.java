package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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
            Map<String, Integer> wordCounters = new TreeMap<String,Integer>();
            Set<String> significantWords = new HashSet();

            BufferedReader bf;
            try {
                bf = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()), "utf-8"));

                String line;
                while ((line = bf.readLine()) != null) {
                    String[] wordsOfLine = line.split(" ");

                    for (String word : wordsOfLine) {
                       if (wordCounters.containsKey(word)) {
                          wordCounters.put(word, wordCounters.get(word) + 1);
                       } else {
                          wordCounters.put(word, 1);
                       }
                    }

                    words.addAll(Arrays.asList(wordsOfLine));
                }


              Map<String, Integer> topWords =
                       wordCounters.entrySet()
                               .stream()
                               .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                               .limit(10)
                               .collect(Collectors.toMap(
                                       Map.Entry::getKey,
                                       Map.Entry::getValue,
                                       (e1, e2) -> e1,
                                       LinkedHashMap::new
                               ));

                significantWords = topWords.keySet();

               emails.add(new Email(emailType, significantWords));
            } catch (IOException ex) {
                System.out.println("not good");
            }

        }
        return emails;
    }
}
