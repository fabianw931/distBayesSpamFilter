package ch.fhnw.dist;

public class Main {

    public static void main(String[] args) {
		String source = 'resources/ham/anlern';

		EmailReader er = new EmailReader(source);

		Email[] emails = er.getEmails();

		for (email : emails) {
			email.analyse();
		}
    }
}
