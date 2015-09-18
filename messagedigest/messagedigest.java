package messagedigest;

import java.util.*;

public class messagedigest 
{
   public static void main(String[] args){
	   
	   System.out.println("input something!\n");

	   Scanner reader=new Scanner(System.in);
	
	   String x=reader.nextLine();
	
	   System.out.println("\nMD5: "+MD5.MD5(x));
	   System.out.println("\nSHA: "+SHA.SHA(x)+"\n");

  }
}
