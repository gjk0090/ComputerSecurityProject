package messagedigest;

import java.security.*;

public class MD5
{
	public static String MD5(final String s) {
	    try {
	     byte[] btInput = s.getBytes();
	     MessageDigest mdInst = MessageDigest.getInstance("MD5");
	     mdInst.update(btInput);
	     byte[] md = mdInst.digest();
	     StringBuffer sb = new StringBuffer();
	     for (int i = 0; i < md.length; i++) {
	      int val = ((int) md[i]) & 0xff;
	      if (val < 16)
	       sb.append("0");
	      sb.append(Integer.toHexString(val));	    
	     }
	     return sb.toString();
	    } catch (Exception e) {
	     return null;
	    }
	}
}
