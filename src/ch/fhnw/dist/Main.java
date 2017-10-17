package ch.fhnw.dist;

import java.util.*;

public class Main {

    public static void main(String[] args) {
		Map<String, Word> words = new HashMap();

		// ham emails
        List<Email> hamAnlernEmails = getEmails("src/ch/fhnw/dist/resources/ham/anlern/", EmailType.HAM);
		List<Email> hamKallibrierungEmails = getEmails("src/ch/fhnw/dist/resources/ham/kallibrierung/", EmailType.HAM);
		List<Email> hamTestEmails = getEmails("src/ch/fhnw/dist/resources/ham/test/", EmailType.HAM);

		// spam emails
		List<Email> spamAnlernEmails = getEmails("src/ch/fhnw/dist/resources/spam/anlern/", EmailType.SPAM);
		List<Email> spamKallibrierungEmails = getEmails("src/ch/fhnw/dist/resources/spam/kallibrierung/", EmailType.SPAM);
		List<Email> spamTestEmails = getEmails("src/ch/fhnw/dist/resources/spam/test/", EmailType.SPAM);

		// anlern emails
		List<Email> anlernEmails = hamAnlernEmails;
		anlernEmails.addAll(spamAnlernEmails);

		// kallibrierung emails
		List<Email> kallibrierungEmails = hamKallibrierungEmails;
		kallibrierungEmails.addAll(spamKallibrierungEmails);

		// test emails
		List<Email> testEmails = hamTestEmails;
		testEmails.addAll(spamTestEmails);

      	words = addWords(anlernEmails, words);
      	words = addWords(kallibrierungEmails, words);

        calculateSpamProbabilities(testEmails, words, spamAnlernEmails.size(), hamAnlernEmails.size());
    }

    private static List<Email> getEmails(String source, EmailType t) {
    	EmailReader er = new EmailReader(source, t);

    	return er.getEmails();
	}

    private static Map<String, Word> addWords(List<Email> emails, Map<String, Word> words) {
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

		return words;
	}

    private static void calculateSpamProbabilities(List<Email> emails, Map<String, Word> words, int numberOfSpamMails, int numberOfHamMails) {
		int wrongHamCounter = 0, wrongSpamCounter = 0;
		int threshold = 48;
		double alphaSpam = 0.000005;
		double alphaHam = 0.0000002;
		for (Email email : emails) {
			double hamProbability;
			double spamProd = 0, hamProd = 0;
			for (String wordStr : email.getWords()) {
				double spamOccurences = 0, hamOccurences = 0;
				if (words.containsKey(wordStr)) {
					Word word = words.get(wordStr);
					spamOccurences = (double) word.getSpamCounter() / (double) numberOfSpamMails;
					hamOccurences = (double) word.getHamCounter() / (double) numberOfHamMails;
				}

				if (spamOccurences == 0.0 && hamOccurences != 0.0) {
					spamOccurences = alphaSpam;
				} else if (hamOccurences == 0.0 && spamOccurences != 0.0) {
					hamOccurences = alphaHam;
				}

				if (spamOccurences != 0 && hamOccurences != 0) {
					spamProd += Math.abs(Math.log(spamOccurences)); // switch to log because we get an underflow otherwise
					hamProd += Math.abs(Math.log(hamOccurences));
				}
			}
			hamProbability = spamProd / (spamProd + hamProd) * 100; // apply the formula
			hamProbability = (double)Math.round(hamProbability * 100) / 100;


			if (hamProbability < threshold && email.getEmailType() == EmailType.HAM) {
				wrongHamCounter++;
			}
			if (hamProbability > threshold && email.getEmailType() == EmailType.SPAM) {
				wrongSpamCounter++;
			}

			//System.out.println("Spam probability: " + hamProbability + "%" + "\t(Type: " + email.getEmailType() + ")");

			email.setHamProbability(hamProbability);

			// set email type based on hamProbability
			if (hamProbability < threshold) {
				email.setEmailType(EmailType.SPAM);
				numberOfSpamMails++;
			} else {
				email.setEmailType(EmailType.HAM);
				numberOfHamMails++;
			}

			// add words to words array
			List<Email> el = new ArrayList<>();
			el.add(email);
			words = addWords(el, words);

		}
		
		System.out.println("\nThreshold: " + threshold + "% (0-" + threshold + "% = SPAM, " + threshold + "-100% = HAM)");
		System.out.println("Alpha value for spam: " + alphaSpam);
		System.out.println("Alpha value for ham: " + alphaHam);

		System.out.println("\n" + wrongHamCounter + " ham emails wrongly guessed");
		System.out.println(wrongSpamCounter + " spam emails wrongly guessed");

		int wrongCounter = wrongHamCounter + wrongSpamCounter;
		double successRate = (double) 100 / (double)emails.size() * (emails.size() - (double)wrongCounter);
		successRate = (double)Math.round(successRate * 100d) / 100d;
		System.out.println("\n" + wrongCounter + " out of " + emails.size() + " emails wrongly guessed (" + successRate + "% success rate)");
	}
}
