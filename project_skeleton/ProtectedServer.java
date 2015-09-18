package project_skeleton;

import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		String user;
		long t1;
		long t2;
		double r1;
		double r2;
		
		int length = in.readInt();
		byte[] receivedDigest = new byte[length];
		in.readFully(receivedDigest);
		
		user = in.readUTF();
		t1 = in.readLong();
		t2 = in.readLong();
		
		r1 = in.readDouble();
		r2 = in.readDouble();
		
		byte[] recomputedDigest = Protection.makeDigest(Protection.makeDigest(user, lookupPassword(user), t1, r1), t2, r2);
		
		if(MessageDigest.isEqual(recomputedDigest, receivedDigest) ){
			return true;
		}
		else{
			return false;
		}
		
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}