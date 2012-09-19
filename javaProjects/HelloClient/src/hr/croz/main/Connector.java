package hr.croz.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Connector {
	
	private String address;
	private HttpsURLConnection connection;
	private boolean verifyCert;
	
	public Connector(String address, boolean verifyCert) {
		this.address = address;
		this.verifyCert = verifyCert;
	}
	
	public void connect() {
		if(!verifyCert)
			connectNoVerify();
		else
			connectVerify();
	}
	
	public void connectNoVerify() {
		
		TrustManager[] trustAllCerts = new TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};

			try {
				HostnameVerifier hv = new HostnameVerifier()
			    {
			        public boolean verify(String urlHostName, SSLSession session)
			        {
			            System.out.println("Warning: URL Host: " + urlHostName + " vs. "
			                    + session.getPeerHost());
			            return true;
			        }
			    };
			    
			    HttpsURLConnection.setDefaultHostnameVerifier(hv);
			    SSLContext sc = SSLContext.getInstance("SSL");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			}
		
		URL url; 
		try {
			url = new URL(address);
			
			connection = (HttpsURLConnection)url.openConnection();
			
		} catch (MalformedURLException e) {
		     e.printStackTrace();
	    } catch (IOException e) {
	    	 e.printStackTrace();
	    }
	}
	
	public void connectVerify() {
		
		URL url; 
		try {
			url = new URL(address);
			
			connection = (HttpsURLConnection)url.openConnection();
			
		} catch (MalformedURLException e) {
		     e.printStackTrace();
	    } catch (IOException e) {
	    	 e.printStackTrace();
	    }
	}
	
	public void printCert() {
	    if(connection!=null){
	    	 
	        try {
	   
	        		System.out.println("Response Code : " + connection.getResponseCode());
	        		System.out.println("Cipher Suite : " + connection.getCipherSuite());
	        		System.out.println("\n");
	   
	        		Certificate[] certs = connection.getServerCertificates();
	        		
	        		for(Certificate cert : certs){
	        			System.out.println("Cert Type : " + cert.getType());
	        			System.out.println("Cert Hash Code : " + cert.hashCode());
	        			System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
	        			System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
	        			System.out.println("\n");
	        		}
	   
	        } catch (SSLPeerUnverifiedException e) {
	        	e.printStackTrace();
	        } catch (IOException e){
	        	e.printStackTrace();
	        }
	   
	    }
		
	}
	
	public String read() {
		
		String input = null;
		
		if(connection!=null){
			 
			try {
		 
			   //System.out.println("****** Content of the URL ********");			
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
		 
			   input = "";
			   String tmp;
			   while ((tmp = br.readLine()) != null){
			      input += tmp;
			   }
			   br.close();
		 
			} catch (IOException e) {
			   e.printStackTrace();
			}
		 
		}
		return input;
	}

	public void login(String username, String password) {
		
		try {
		
		connectNoVerify();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(false);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

		String query="";

		query += "username=" + username;
		query +="&";
		query += "password=" + password;
		query += "&submit=Send";
		
		//System.out.println("Sending query: " + connection.getURL().getPath() + "?" + query);

		bw.write(query);
		//bw.write("\n");
		bw.newLine();
		bw.flush();
		bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String location = connection.getHeaderField("Location");
		String cookie = connection.getHeaderField("Set-Cookie").split(";")[0];
		
		//System.out.println(location);
		//System.out.println(cookie);
		
		try {
		connection = (HttpsURLConnection)(new URL(location)).openConnection();
		
		connection.addRequestProperty("Cookie", cookie);
		
		} catch (Exception e) {
			
		}
		
	}
}	
