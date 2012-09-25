package hr.croz.zadaci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xades4j.algorithms.CanonicalXMLWithoutComments;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.DataObjectReference;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesBesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.properties.DataObjectDesc;
import xades4j.providers.AlgorithmsProviderEx;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.KeyStoreKeyingDataProvider;


public class Main {

	public static void main(String[] args) {

		if (args.length < 3) {
			usage();
			System.exit(0);
		}
		
		String file = "";
		boolean signVerify = true;
		String truststore = "";
		
		for(int i = 0 ; i < args.length ; i++) {
			if ("-verifyxml".equals(args[i]))
				signVerify = true;
			else if ("-file".equals(args[i]))
				file = args[++i];
			else if ("-truststore".equals(args[i]))
				truststore = args[++i];
		}
		
		DocumentBuilder db;
		Document doc;
		Element el;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String pass = "";
		System.out.println("Keystore password: ");
		
		KeyStoreKeyingDataProvider kp;
		
		XadesSigner sign;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(new File(file));
			el = doc.getDocumentElement();
			pass = br.readLine();
			kp = new FileSystemKeyStoreKeyingDataProvider(
					"pkcs12",
					truststore,
					new FirstCertificateSelector(),
					new DirectPasswordProvider(pass),
					new DirectPasswordProvider(pass),
					true);
			System.out.println(kp.toString());
			sign = new XadesBesSigningProfile(kp).newSigner();
			
			DataObjectDesc obj1 = new DataObjectReference("#" + el.getAttribute("Id")).withTransform(new EnvelopedSignatureTransform()).withTransform(new CanonicalXMLWithoutComments());
			SignedDataObjects obj = new SignedDataObjects(obj1);
			
			sign.sign(obj, el);
			
			TransformerFactory tf = TransformerFactory.newInstance();
	        String path = file + ".signature";
	        FileOutputStream out = new FileOutputStream(path);
	        tf.newTransformer().transform(
	                new DOMSource(doc),
	                new StreamResult(out));
	        out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 
		
	}
	
	public static void usage() {
		System.out.println("Usage:");
		System.out.println("digisignxml -truststore <truststore> -signxml -file <filepath>");
		System.out.println("digisignxml -truststore <truststore> -verifyxml -file <signed_filepath>");
	}

}
