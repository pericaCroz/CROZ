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
		
		for(int i = 0 ; i < args.length ; i++) {
			if("-user".equals(args[i])) {
				username = args[++i];
				continue;
			}
			if("-pass".equals(args[i])) {
				password = args[++i];
				continue;
			}
			if("-verifyCert".equals(args[i]))
				verifyCert = true;
		}
		
		if(username == null || password == null) {
			usage();
			System.exit(0);
		}
		
		System.out.println("Username: " + username + " ; Password: " + password + " ; " + ((verifyCert == true) ? "true" : "false"));
		
		
	}
	
	public static void usage() {
		System.out.println("Usage: ");
		System.out.println("helloclient -user <username> -pass <password> [-verifyCert]");
	}

}
