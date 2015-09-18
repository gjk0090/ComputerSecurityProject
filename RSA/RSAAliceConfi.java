package RSA;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class RSAAliceConfi {
	
	protected static byte[] encipher(byte[] msg, Cipher cipher)throws IllegalBlockSizeException, BadPaddingException, IOException {
		ByteArrayOutputStream byte_stream = new ByteArrayOutputStream();
		byte[] temp;
		int blocksize = 117;
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

		byte[] ciphermessage = byte_stream.toByteArray();
		byte_stream.close();
		return ciphermessage;
	}
	
	public static void main(String[] args) throws Exception {
		
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;

		// read the bob public key file.
		FileInputStream filein = new FileInputStream("RSA/bob.txt");
		ObjectInputStream objectin = new ObjectInputStream(filein);
		Key publickeybob = (Key) objectin.readObject();
		objectin.close();

		// encrypt message
		byte[] ciphermessage;
		byte[] bytemessage = message.getBytes();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// -encrypt by bob public key
		cipher.init(Cipher.ENCRYPT_MODE, publickeybob);
		ciphermessage = encipher(bytemessage, cipher);

		// send ciphermessage
		Socket s = new Socket(host, port);
		ObjectOutputStream objectout2 = new ObjectOutputStream(s.getOutputStream());
		objectout2.writeObject(ciphermessage);
		objectout2.flush();
		objectout2.close();
		s.close();

	}
}
