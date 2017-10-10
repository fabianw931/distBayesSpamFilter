package ch.fhnw.dist;

import java.math.BigDecimal;
import java.util.Set;

public class Email {

    private EmailType emailType;
    private Set<String> words;

    // percentage
    // 0%: ham
    // 100%: spam
    private double spamProbability;

    public Email(EmailType emailType, Set<String> words) {
        this.emailType = emailType;
        this.words = words;
    }

    
    public void analyse() {
        System.out.println(words.size());
    }

    public double getSpamProbability() {
        return spamProbability;
    }

    public void setSpamProbability(double spamProbability) {
        this.spamProbability = spamProbability;
    }

    public Set<String> getWords() {
        return words;
    }

    public EmailType getEmailType() {
        return emailType;
    }
}
