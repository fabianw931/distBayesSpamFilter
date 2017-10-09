package ch.fhnw.dist;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        String source = "src/ch/fhnw/dist/resources/ham/anlern/";

        EmailReader er = new EmailReader(source, EmailType.HAM);
		Map<String, Word> words = new HashMap();

		// TODO remove this from main method and add it somewhere else
        List<Email> emails = er.getEmails();
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

		for(String key: words.keySet()) {
			System.out.println(words.get(key).toString());
		}
    }
}
