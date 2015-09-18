package RSA;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class RSABobAuthen {
	
	protected static byte[] decipher(byte[] msg, Cipher cipher)throws IllegalBlockSizeException, BadPaddingException, IOException {
		ByteArrayOutputStream byte_stream = new ByteArrayOutputStream();
		byte[] temp;
		int blocksize = 128;
		int i = 0;

		while (i < msg.length) {
			if ((msg.length - i) >= blocksize) {
				temp = cipher.doFinal(msg, i, blocksize);
			} else {
				temp = cipher.doFinal(msg, i, msg.length - i);
			}
			byte_stream.write(temp, 0, temp.length);
			i += blocksize;
		}

		byte[] deciphermessage = byte_stream.toByteArray();
		byte_stream.close();
		return deciphermessage;
	}

	public static void main(String[] args) throws Exception {
		
		int port = 7999;
		ServerSocket server = new ServerSocket(port);

		// wait for the socket
		Socket s = server.accept();

		// read the alice public key from the file.
		FileInputStream filein = new FileInputStream("RSA/alice.txt");
		ObjectInputStream objectin = new ObjectInputStream(filein);
		Key publickeyalice = (Key) objectin.readObject();
		objectin.close();

		// get ciphermessage
		ObjectInputStream objectin2 = new ObjectInputStream(s.getInputStream());
		byte[] ciphermessage = (byte[]) objectin2.readObject();
		objectin2.close();
		s.close();

		// decrypt ciphermessage
		byte[] deciphermessage;
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		// -decypt by alice public key
		cipher.init(Cipher.DECRYPT_MODE, publickeyalice);
		deciphermessage = decipher(ciphermessage, cipher);

		// print message
		String message = new String(deciphermessage, "UTF-8");
		System.out.println(message);

	}
}
