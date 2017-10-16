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

		List<Email> anlernEmails = hamAnlernEmails;
		anlernEmails.addAll(spamAnlernEmails);

		List<Email> kallibrierungEmails = hamKallibrierungEmails;
		kallibrierungEmails.addAll(spamKallibrierungEmails);

		List<Email> testEmails = hamTestEmails;
		testEmails.addAll(spamTestEmails);

      	words = addWords(anlernEmails, words);
      //	words = addWords(kallibrierungEmails, words);

        calculateSpamProbabilities(kallibrierungEmails, words, spamAnlernEmails.size(), hamAnlernEmails.size());

		System.out.println(words.size() + " Words in total");
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
		for (Email email : emails) {
			double spamProbability;
			double wordProd = 0d;
			for (String wordStr : email.getWords()) {
				double spamOccurences = 0d, hamOccurences = 0d;
				if (words.containsKey(wordStr)) {
					Word word = words.get(wordStr);
					spamOccurences = (double) word.getSpamCounter() / (double) numberOfSpamMails;
					hamOccurences = (double) word.getHamCounter() / (double) numberOfHamMails;
				}

				if (spamOccurences == 0d) {
					spamOccurences = 0.4d;
				}


				if (hamOccurences == 0d) {
					hamOccurences = 0.2d;
				}

				//spamOccurences = (double)Math.round(spamOccurences * 100d) / 100d;
				//hamOccurences = (double)Math.round(hamOccurences * 100d) / 100d;

				//System.out.println("spamOccurences: " + spamOccurences);
				//System.out.println("hamOccurences: " + hamOccurences);

				double hamSpam = (spamOccurences * 100) / (hamOccurences * 100) / 100;

				if (wordProd == 0d) {
					wordProd = hamSpam;
				} else {
					wordProd = wordProd * hamSpam;
				}

				wordProd = (double)Math.round(wordProd * 100d) / 100d;
				//System.out.println("WordProd: " + wordProd);
			}

			spamProbability = 1.0 / (wordProd + 1) * 100;
			spamProbability = (double)Math.round(spamProbability * 100d) / 100d;

			System.out.println("Spam probability: " + spamProbability + "%" + "\t(Type: " + email.getEmailType() + ")");
			//System.exit(0);

			if (spamProbability > 50 && email.getEmailType() == EmailType.HAM) {
				wrongHamCounter++;
			}
			if (spamProbability < 50 && email.getEmailType() == EmailType.SPAM) {
				wrongSpamCounter++;
			}

			email.setSpamProbability(spamProbability);
		}
		System.out.println(wrongHamCounter + " ham emails wrongly guessed");
		System.out.println(wrongSpamCounter + " spam emails wrongly guessed");
	}
}
