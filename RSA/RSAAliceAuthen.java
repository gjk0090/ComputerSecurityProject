package RSA;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class RSAAliceAuthen {
	
	private static PublicKey publickey;
	private static PrivateKey privatekey;
	
	protected static void generate_key() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024, new SecureRandom());
		KeyPair keypair = generator.generateKeyPair();
		publickey = keypair.getPublic();
		privatekey = keypair.getPrivate();
	}
	
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

		// generate key.
		generate_key();

		// store public key in a file.
		FileOutputStream fileout = new FileOutputStream("RSA/alice.txt");
		ObjectOutputStream objectout = new ObjectOutputStream(fileout);
		objectout.writeObject(publickey);
		objectout.close();

		// encrypt message
		byte[] ciphermessage;
		byte[] bytemessage = message.getBytes();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		// -encrypt by alice private key
		cipher.init(Cipher.ENCRYPT_MODE, privatekey);
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
