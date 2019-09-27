package Lib;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateCheckSumSHA {
	
    
    public static String checkFile(String dataFile) throws NoSuchAlgorithmException, IOException {
    	
    	
        MessageDigest md = MessageDigest.getInstance("SHA1");/* SHA1, MD5, ... */
        FileInputStream fis = new FileInputStream(dataFile);
        byte[] dataBytes = new byte[1024];
        int nRead = 0;
        while ((nRead = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nRead);
        }
        fis.close();
        byte[] mdBytes = md.digest();
        // convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdBytes.length; i++) {
            sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16))
                    .substring(1);
        }
        String a = sb.toString().trim();
        //System.out.print(a);
        return a;
    }


}
