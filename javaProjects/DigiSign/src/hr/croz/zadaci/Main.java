package hr.croz.zadaci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

public class Main {

	public static void main(String[] args) {
		
		if (args.length < 5) {
			usage();
			System.exit(0);
		}
		
		String keystore = "";
		boolean signVerify = true; // default sign
		String file = "";
		String signature = "";
		
		
		for(int i = 0 ; i < args.length ; i++) {
			if ("-keystore".equals(args[i]))
				keystore = args[++i];
			else if ("-verify".equals(args[i]))
				signVerify = false;
			else if ("-file".equals(args[i]))
				file = args[++i];
			else if ("-signature".equals(args[i]))
				signature = args[++i];
		}
			// ALIASI
			// signer - to je certifikat signera
		// ide u keystore s keytool -import -trustcacerts -alias signerKey -file signer.key -keystore signer.jks
		// bitno je da ime organizaije CA i potpisanog certifikata nije isto (dakle ne dvaput CROZ)
			// signerKey = to je kljuc signera
			// verifier - to je certifikat verifiera
			// verifierKey - key verifiera
			// password keystorea je 'signer'
		
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			System.out.println("Keystore password: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String multiPurpose = br.readLine();
			char[] charPass = multiPurpose.toCharArray();
			FileInputStream fis = new FileInputStream(new File(keystore));
			ks.load(fis, charPass);
			
			if (signVerify)
				sign(ks, charPass, file, "SHA1withRSA");
			else
				verify(ks, charPass, file, signature, "SHA1withRSA");
			
			
			
			fis.close();
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in main.");
		}
		
		
	}
	
	private static void sign(KeyStore ks, char[] passwd, String filename, String sigType) {
		
		try {
			System.out.println("Enter signer private key alias: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = br.readLine();
			
			PrivateKey pKey = (PrivateKey)ks.getKey(in, passwd);
			//System.out.println(pKey.toString());
			
			FileInputStream is = new FileInputStream(filename);
			DataInputStream ds = new DataInputStream(is);
			br = new BufferedReader(new InputStreamReader(ds));
			String text = "";
			String tmp = "";
			while((tmp = br.readLine()) != null) {
				text += tmp;
			}
			//System.out.println(text);
			
			Signature sig = Signature.getInstance(sigType);
			byte[] byteData = text.getBytes();
			
			sig.initSign(pKey);
			sig.update(byteData);
			byte[] signed = sig.sign();
			String result = new String(Base64.encode(signed));
			
			//String result = calculate(text, sigType, null, pKey);
			
			//System.out.println(result);
			
			String newFile = filename + ".signature";
			
			FileWriter fw = new FileWriter(newFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(result);
			bw.flush();
			
			bw.close();
			br.close();
			ds.close();
			is.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void verify(KeyStore ks, char[] passwd, String filename, String signature, String sigType) {
		try {
			System.out.println("Enter signer certificate alias: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = br.readLine();
			
			X509Certificate cert;
			cert = (X509Certificate)ks.getCertificate(in);
			PublicKey pKey = (PublicKey)cert.getPublicKey();
			//System.out.println(pKey.toString());
			
			FileInputStream is = new FileInputStream(filename);
			DataInputStream ds = new DataInputStream(is);
			br = new BufferedReader(new InputStreamReader(ds));
			String text = "";
			String tmp = "";
			while((tmp = br.readLine()) != null) {
				text += tmp;
			}
			//System.out.println(text);
			br.close();
			ds.close();
			is.close();
			
			is = new FileInputStream(signature);
			ds = new DataInputStream(is);
			br = new BufferedReader(new InputStreamReader(ds));
			String sig = "";
			tmp = "";
			while((tmp = br.readLine()) != null) {
				sig += tmp;
			}
			//System.out.println(sig);
			byte[] decoded = Base64.decode(sig);
			
			Signature verify = Signature.getInstance(sigType);
			verify.initVerify(pKey);
			verify.update(text.getBytes());
			boolean verified = verify.verify(decoded);
			
			if (verified) {
				System.out.println("The message is verified.");
			} else {
				System.out.println("The message is not verified.");
			}
			
			
			br.close();
			ds.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String calculate(String text, String sigType, PublicKey pubKey, PrivateKey privKey) {
		
		String result = "";
		
		try {
			
			
			if (pubKey != null && privKey == null) {
				//sig.initVerify(pubKey);
				//sig.update(byteData);
				
				
				
			} else if (pubKey == null && privKey != null) {
				
				
			} else {
				System.out.println("Error? PubKey and PrivKey null?");
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	public static void usage() {
		System.out.println("Usage:");
		System.out.println("digisign -keystore <keystore> -sign -file <file>");
		System.out.println("digisign -keystore <keystore> -verify -file <file> -signature <signature>");
	}
	
	
}
