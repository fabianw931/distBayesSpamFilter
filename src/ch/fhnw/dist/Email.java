package ch.fhnw.dist;

import java.util.Set;

public class Email {

    private EmailType emailType;
    private Set<String> words;

    public Email(EmailType emailType, Set<String> words) {
        this.emailType = emailType;
        this.words = words;
    }

    
    public void analyse() {
        System.out.println(words.size());
    }

    public Set<String> getWords() {
        return words;
    }

    public EmailType getEmailType() {
        return emailType;
    }
}
