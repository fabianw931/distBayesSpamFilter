package ch.fhnw.dist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String source = "src/ch/fhnw/dist/resources/ham/anlern/";

        EmailReader er = new EmailReader(source, EmailType.HAM);
        Set<Word> allWords = new HashSet();

        List<Email> emails = er.getEmails();
        for (Email email : emails) {
            email.analyse();
        }

        System.out.println(emails.size());

    }
}
