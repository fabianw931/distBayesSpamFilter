package ch.fhnw.dist;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        String source = "src/ch/fhnw/dist/resources/ham/anlern/";

        EmailReader er = new EmailReader(source, EmailType.HAM);
		String source2 = "src/ch/fhnw/dist/resources/spam/anlern/";

		EmailReader er2 = new EmailReader(source2, EmailType.SPAM);
		Map<String, Word> words = new HashMap();

		// TODO remove this from main method and add it somewhere else
        List<Email> hamEmails = er.getEmails();
		List<Email> spamEmails = er2.getEmails();
		List<Email> emails = hamEmails;
		emails.addAll(spamEmails);
        for (Email email : emails) {
        	for (String wordStr : email.getWords()) {
				Word wordObj;

				if (words.containsKey(wordStr)) {
					wordObj = words.get(wordStr);
				} else {
					wordObj = new Word(wordStr);
				}

				if (email.getEmailType() == EmailType.SPAM) {
					wordObj.incSpamCounter();
				} else {
					wordObj.incHamCounter();
				}

				words.put(wordStr, wordObj);
			}
        }

        calculateSpamProbabilities(emails, words, spamEmails.size(), hamEmails.size());

		System.out.println(words.size());
    }

    private static void calculateSpamProbabilities(List<Email> emails, Map<String, Word> words, int numberOfSpamMails, int numberOfHamMails) {
		int wrongHamCounter = 0, wrongSpamCounter = 0;
		for (Email email : emails) {
			double spamProbability, spamProd = 0f, hamProd = 0f;

			for (String wordStr : email.getWords()) {
				Word word = words.get(wordStr);

				if (word.getSpamCounter() != 0) {
					if (spamProd == 0f) {
						spamProd =  (double)word.getSpamCounter() / (double)numberOfSpamMails;
					} else {
						spamProd = spamProd * ((double)word.getSpamCounter() / (double)numberOfSpamMails);
					}
				} else {
					spamProd = 0f;
					break;
				}

				if (word.getHamCounter() != 0) {
					if (hamProd == 0f) {
						hamProd = (double)word.getHamCounter() / (double)numberOfHamMails;
					} else {
						hamProd = hamProd * ((double)word.getHamCounter() / (double)numberOfHamMails);
					}
				}
			}

			spamProbability = spamProd / (spamProd + hamProd) * 100f;
			spamProbability = (double)Math.round(spamProbability * 100d) / 100d;

			if (spamProbability > 50 && email.getEmailType() == EmailType.HAM) {
				wrongHamCounter++;
				System.out.println("Spam probability: " + spamProbability + "%" + "\t(Type: " + email.getEmailType() + ")");
			}
			if (spamProbability < 50 && email.getEmailType() == EmailType.SPAM) {
				wrongSpamCounter++;
				System.out.println("Spam probability: " + spamProbability + "%" + "\t(Type: " + email.getEmailType() + ")");
			}

			email.setSpamProbability(spamProbability);
		}
		System.out.println(wrongHamCounter + "/" + numberOfHamMails + " ham emails wrongly guessed");
		System.out.println(wrongSpamCounter + "/" + numberOfSpamMails + " spam emails wrongly guessed");
	}
}
