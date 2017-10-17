package ch.fhnw.dist;

import java.util.Set;

public class Email {

    private EmailType emailType;
    private Set<String> words;

    // percentage
    // 100%: ham
    // 0%: spam
    private double hamProbability;

    public Email(EmailType emailType, Set<String> words) {
        this.emailType = emailType;
        this.words = words;
    }

    
    public void analyse() {
        System.out.println(words.size());
    }

    public double getHamProbability() {
        return hamProbability;
    }

    public void setHamProbability(double hamProbability) {
        this.hamProbability = hamProbability;
    }

    public void setEmailType(EmailType et) {
        this.emailType = et;
    }

    public Set<String> getWords() {
        return words;
    }

    public EmailType getEmailType() {
        return emailType;
    }
}
