package hr.croz.main;

public class Main {

	public static void main(String[] args) {
		
		if (args.length < 4) {
			usage();
			System.exit(0);
		}
		
		String username = null;
		String password = null;
		boolean verifyCert = false;
		boolean info = false;
		String address = null;
		
		for(int i = 0 ; i < args.length ; i++) {
			if("-user".equals(args[i])) {
				username = args[++i];
				continue;
			}
			if("-pass".equals(args[i])) {
				password = args[++i];
				continue;
			}
			//if("-address".equals(args[i])) {
			//	address = args[++i];
			//	continue;
			//}
			if("-verifyCert".equals(args[i]))
				verifyCert = true;
			if("-info".equals(args[i]))
				info = true;
		}
		
		if(username == null || password == null) {
			usage();
			System.exit(0);
		}
		
		//System.out.println("Username: " + username + " ; Password: " + password + " ; " + (verifyCert ? "true" : "false"));
		
		address="https://testbox/app/login";
		
		Connector connectMe = new Connector(address, verifyCert);
		connectMe.connect();
		
		if(info) {
			connectMe.printCert();
		}
		
		connectMe.login(username, password);
		
		String response = connectMe.read();
		//System.out.println(response);
		
		if (response.contains("Authentication unsuccessful.")) {
			System.out.println("Authentication unsuccessful.");
		} else {
			String[] s = response.split("<div>");
			for(int i = 1 ; i < s.length ; i++) {
				String[] eh = s[i].split("</div>");
				System.out.println(eh[0]);
			}
		}
		
	}
	
	public static void usage() {
		System.out.println("Usage: ");
		System.out.println("helloclient -user <username> -pass <password> [-info] [-verifyCert]");
	}

}
