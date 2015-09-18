package project_skeleton;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		long t1;
		long t2;
		double r1; 
		double r2;
		
		t1 = new Date().getTime();
		r1 = new Random().nextGaussian();
		byte[] digest1 = Protection.makeDigest(user, password, t1, r1);
		
		t2 = new Date().getTime();
		r2 = new Random().nextGaussian();
		
		byte[] digest2 = Protection.makeDigest(digest1, t2, r2);
		out.writeInt(digest2.length);
		out.write(digest2);
		out.writeUTF(user);
		out.writeLong(t1);
		out.writeLong(t2);
		out.writeDouble(r1);
		out.writeDouble(r2);
		
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}