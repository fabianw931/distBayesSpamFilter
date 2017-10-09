package ch.fhnw.dist;

public class Word {
    private String word;
    private int spamCounter;
    private int hamCounter;

    public Word(String word) {
        this.word = word;
        spamCounter = 0;
        hamCounter = 0;
    }

    public String getWord() {
        return word;
    }

    public void incSpamCounter() {
        spamCounter++;
    }

    public int getSpamCounter() {
        return spamCounter;
    }

    public void incHamCounter() {
        hamCounter++;
    }

    public int getHamCounter() {
        return hamCounter;
    }

    public String toString() {
        return word + " [spam: "  + spamCounter + ", ham: " + hamCounter + "]";
    }
}
