package hr.croz.zadatak4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class Main {

	public static void main(String[] args) {

		if (args.length < 5) {
			usage();
			System.exit(0);
		}
		
		String hashname = "";
		boolean calc = true;
		boolean verify = false;
		String file = "";
		String checksum = "";
		
		for (int i = 0 ; i < args.length ; i++) {
			if ("-hash".equals(args[i]))
				hashname = args[++i];
			else if ("-calc".equals(args[i])) {
				calc = true;
				verify = false;
			}
			else if ("-verify".equals(args[i])) {
				calc = false;
				verify = true;
			}
			else if ("-file".equals(args[i]))
				file = args[++i];
			else if ("-checksum".equals(args[i]))
				checksum = args[++i];
		}
		
		if (!hashname.equals("SHA-1") && !hashname.equals("SHA-256") && !hashname.equals("SHA-512")) {
			usage();
			System.exit(0);
		}

		if (file.isEmpty()) {
			usage();
			System.exit(0);
		}
		
		if (verify && checksum.isEmpty()) {
			usage();
			System.exit(0);
		}
		
		if (calc) {
			calculateHash(hashname, file);
		}
		if (verify) {
			verifyHash(hashname, file, checksum);
		}
	}
	
	private static void usage() {
		System.out.println("Usage:");
		System.out.println("cryptohash -hash <name> -calc -file <filename>");
		System.out.println("cryptohash -hash <name> -verify -file <filename> -checksum <filename>");
		System.out.println("Supported hashing algorithms: SHA-1, SHA-256, SHA-512");
	}
	
	private static String calculateHash(String hashname, String filename) {
		try {
			FileInputStream is = new FileInputStream(filename);
			DataInputStream ds = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(ds));
			
			String tmp = "";
			String text = "";
			
			while((tmp = br.readLine()) != null) {
				text += tmp;
			}
			
			br.close();
			ds.close();
			is.close();
			
			String converted = calculate(hashname, text);
			
			String ending = "";
			if (hashname.equals("SHA-1"))
				ending = ".sha1";
			else if (hashname.equals("SHA-256"))
				ending = ".sha256";
			else if (hashname.equals("SHA-512"))
				ending = ".sha512";
			
			String newdigest = filename + ending;
			FileWriter fw = new FileWriter(newdigest);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(converted);
			bw.flush();
			
			bw.close();
			fw.close();
			return converted;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private static void verifyHash(String hashname, String filename, String checksum) {
		try {
			FileInputStream is = new FileInputStream(filename);
			DataInputStream ds = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(ds));
			
			String tmp = "";
			String text = "";
			
			while((tmp = br.readLine()) != null) {
				text += tmp;
			}
			
			br.close();
			ds.close();
			is.close();
			
			String calculated = calculate(hashname, text);
			
			is = new FileInputStream(checksum);
			ds = new DataInputStream(is);
			br = new BufferedReader(new InputStreamReader(ds));
			
			tmp = "";
			String verify = "";
			
			while((tmp = br.readLine()) != null) {
				verify += tmp;
			}
			
			br.close();
			ds.close();
			is.close();
			
			if (calculated.equals(verify))
				System.out.println("Correct.");
			else
				System.out.println("False.");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static String toHex(byte[] input) {
		if (input == null)
			return null;
		
		StringBuffer buf = new StringBuffer();
		
		for(byte b : input) {
			int x = b & 0xff;
			if (x < 16) {
				buf.append('0');
			}
			buf.append(Integer.toHexString(x));
		}
		
		return buf.toString();
	}
	
	private static String calculate(String hashname, String text) {
		try {
			MessageDigest md = MessageDigest.getInstance(hashname);
			md.update(text.getBytes("UTF-8"));
			byte[] digest = md.digest();
		
			return toHex(digest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
