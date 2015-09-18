package project_skeleton;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		
		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.
		
		KeyGenerator keygen = KeyGenerator.getInstance("DES");
		keygen.init(new SecureRandom());
		Key k = keygen.generateKey();
		
		// -Store it in a file.
				
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("project_skeleton/KeyFile.txt"));
		out.writeObject(k);
		out.close();
		
		// -Use the key to encrypt the message above and send it over socket s to the server.
		
		Socket s = new Socket(host, port);

		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, k);
		
		CipherOutputStream cipherout = new CipherOutputStream(s.getOutputStream(), cipher);
		cipherout.write(message.getBytes(), 0, message.getBytes().length);
		cipherout.flush();
		cipherout.close();
		
	}
}