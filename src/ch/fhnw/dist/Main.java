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

        calculateSpamProbabilities(kallibrierungEmails, words, spamKallibrierungEmails.size(), hamKallibrierungEmails.size());

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
			double spamProbability, spamProd = 0d, hamProd = 0d;

			for (String wordStr : email.getWords()) {
				if (words.containsKey(wordStr)) {
					Word word = words.get(wordStr);
					if (word.getSpamCounter() == 0 && word.getHamCounter() != 0) {
						spamProd = 0.01d;
						continue;
					}

					if (word.getHamCounter() == 0 && word.getSpamCounter() != 0) {
						hamProd = 0.1d;
						continue;
					}

					if (word.getSpamCounter() != 0) {
						if (spamProd == 0d) {
							spamProd = (double)word.getSpamCounter() / (double)numberOfSpamMails;
						} else {
							spamProd *= ((double)word.getSpamCounter() / (double)numberOfSpamMails);
						}
					} else {
						//spamProd = 0d;
						//break;
					}

					if (word.getHamCounter() != 0) {
						if (hamProd == 0d) {
							hamProd = (double)word.getHamCounter() / (double)numberOfHamMails;
						} else {
							hamProd *= ((double)word.getHamCounter() / (double)numberOfHamMails);
						}
					} else {
						//hamProd = 0d;
						//break;
					}
				}
			}

			spamProbability = spamProd / (spamProd + hamProd) * 100d;
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
