package ch.fhnw.dist;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
		String source = "resources/ham/anlern";

		EmailReader er = new EmailReader(source);

		Email[] emails = er.getEmails();

		Set<Word> words = new HashSet<Word>();

		for (Email email : emails) {
			words.addAll(email.getWords());
			//email.analyse();
		}

		System.out.println(words.toString());
	}
}
