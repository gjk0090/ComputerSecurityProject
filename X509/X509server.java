package X509;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.X509Certificate;

import javax.crypto.*;

public class X509server {
	
	private static Key privatekey;

	public static void main(String[] args) throws Exception {
		int port = 7999;
		ServerSocket server = new ServerSocket(port);

		// load keygjk
		
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(new File("X509/keygjk")),"111111".toCharArray());
		
		// -get private key
		privatekey = keystore.getKey("gjk", "111111".toCharArray());
		if (privatekey == null) {
			System.out.println("Got null key from keystore.");
			return;
		}
		
		// -get certificate
		X509Certificate x509cert = (X509Certificate) keystore.getCertificate("gjk");
		if (x509cert == null)
			System.out.println("Got null cert from keystore.");

		// save certificate to a file
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("X509/X509.txt"));
		out.writeObject(x509cert);
		out.close();

		// wait for client
		
		Socket s = server.accept();
		
		// use the key to decrypt the incoming message from socket s.

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privatekey);
		CipherInputStream cis = new CipherInputStream(s.getInputStream(), cipher);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(cis));
		String message = br.readLine();
		br.close();
		
		// print
		
		System.out.println("server:" + message);
	}

}
