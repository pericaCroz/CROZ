package hr.croz.zadatak3;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class Main {

	public static void main(String[] args) {

		if (args.length < 7) {
			usage();
			System.exit(0);
		}
		
		String userhost = "";
		String lport = "";
		String rhost = "";
		String rport = "";
		
		for (int i = 0 ; i < args.length ; i++) {
			if ("-lport".equals(args[i]))
				lport = args[++i];
			else if ("-rhost".equals(args[i]))
				rhost = args[++i];
			else if ("-rport".equals(args[i]))
				rport = args[++i];
			else
				userhost = args[i];
		}
		
		if(userhost==null || userhost.isEmpty() || lport==null || lport.isEmpty() || rhost==null || rhost.isEmpty() ||
				rport==null || rport.isEmpty()) {
			usage();
			System.exit(0);
		}
		
		String[] separated = separateUserHost(userhost);
		
		forward(separated[0], separated[1], Integer.parseInt(lport), rhost, Integer.parseInt(rport));
		
	}
	
	private static void usage() {
		System.out.println("Usage:");
		System.out.println("sshtunnel <user@host> -lport <localport> -rhost <remotehost> -rport <remoteport>");
	}
	
	private static String[] separateUserHost(String userhost) {
		
		String[] result = userhost.split("@");
		
		if (result.length != 2 || result[0]==null || result[0].isEmpty() || result[1]==null || result[1].isEmpty()) {
			System.out.println(userhost + " is not a valid user@hostname directive.");
			System.exit(0);
		}
		
		return result;
	}
	
	private static void forward(String user, String host, int lport, String rhost, int rport) {
		try {
			JSch jsch = new JSch();
			
			Session session = jsch.getSession(user, host, 22);
			
			//System.out.println("Password for " + user + "@" + host + ":");
			//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			//String passwd = in.readLine();
			
			UserInfo ui = new MyUserInfo() {
				public void showMessage(String message){
			          System.out.println(message);
			        }
			    public boolean promptYesNo(String message){
			          System.out.println(message);
			          int foo=0; // default true
			          String read = "";
			          BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
			          while ((read==null || read.isEmpty()) && !(("yes".equals(read)) || ("no".equals(read)))) {
			        	  System.out.print("yes/no");
			        	  try {
			        		  read = rd.readLine();
			        	  } catch (Exception e) {
			        		  System.out.println(e.getMessage());
			        	  }
			          }
			        	  
			          if("no".equals(read))
			        		  foo=1;
			          
			          return foo==0;
			        }
			    
			};
			
			//session.setPassword(passwd);
			session.setUserInfo(ui);
			
			session.connect();
			int assigned = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:"+assigned+"-->" + rhost + ":" + rport);
			
			while(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static abstract class MyUserInfo implements UserInfo {
			private String passwd;
		
			public String getPassword(){ return passwd; }
			public boolean promptYesNo(String str){ return false; }
			public String getPassphrase(){ return null; }
			public boolean promptPassphrase(String message){ return false; }
			public boolean promptPassword(String message){ 
				System.out.println(message);
	    		String read = "";
	    		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
	    		try {
	    			read = rd.readLine();
	    		} catch (Exception e) {
	    			System.out.println(e.getMessage());
	    		}
	    		if (read==null || read.isEmpty()) {
	    			return false;
	    		} else {
	    			passwd = read;
	    			return true;
	    		}
	    	}
			public void showMessage(String message){ }
	}

}
