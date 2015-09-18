package X509;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.crypto.*;

public class x509client {
	
	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;

		// get certificate from file

		ObjectInputStream in = new ObjectInputStream(new FileInputStream("X509/X509.txt"));
		X509Certificate x509cert = (X509Certificate) in.readObject();
		in.close();

		// -check expiration date
		try {
			x509cert.checkValidity();
		} catch (CertificateExpiredException e) {
			System.out.println("The certificate is expired.");
		} catch (CertificateNotYetValidException e) {
			System.out.println("The certificate is not yet valid.");
		}

		// -get public key
		PublicKey publickey = x509cert.getPublicKey();

		// -verify public key
		x509cert.verify(publickey);

		// use the key to encrypt the message above and send it over socket s to the server.
		
		Socket s = new Socket(host, port);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publickey);

		CipherOutputStream out = new CipherOutputStream(s.getOutputStream(), cipher);
		out.write(message.getBytes(), 0, message.getBytes().length);
		out.flush();
		out.close();
		s.close();

		System.out.println("Certificate:\n" + x509cert.toString());
		System.out.println("client:" + message);
	}
}
