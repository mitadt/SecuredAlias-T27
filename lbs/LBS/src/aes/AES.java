package aes;


import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {//Advanced Encryption Standard 
    private static final String ALGO = "AES";
    private static byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private static SecretKeySpec secretKey ;
    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(byte[] data,String secretkey) throws Exception {
        //Key key = generateKey();
    	setKey(secretkey);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, secretKey);//key
        byte[] encVal = c.doFinal(data);
        //NO_WRAP is important as was getting \n at the end
        return Base64.getEncoder().encodeToString(encVal);
    }
    public static String encrypt1(String data,String secretkey) throws Exception {
        //Key key = generateKey();
    	setKey(secretkey);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, secretKey);//key
        byte[] encVal = c.doFinal(data.getBytes());
        //NO_WRAP is important as was getting \n at the end
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static byte[] decrypt(String encryptedData,String secretkey) throws Exception {
        //Key key = generateKey();
    	setKey(secretkey);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, secretKey);//key
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return decValue;
    }
    public static String decrypt1(String encryptedData) throws Exception {
        //Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, secretKey);//key
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
    private static void setKey(String myKey){
        
    	   
        MessageDigest sha = null;
        try {
        	keyValue = myKey.getBytes("UTF-8");
            //System.out.println(keyValue.length);
            sha = MessageDigest.getInstance("SHA-1");//creating hash value //sha is used for hashing.
            keyValue = sha.digest(keyValue);//converting hash value to bytes array
            keyValue = Arrays.copyOf(keyValue, 16); // use only first 128 bit
            //System.out.println(keyValue.length);
            //System.out.println(new String(keyValue,"UTF-8"));
            secretKey = new SecretKeySpec(keyValue, "AES");                   
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }           
    }
    public static void main(String[] args) {
		try {
			String emsg = AES.encrypt("asda".getBytes(), "123456789");
			System.out.println("emsg "+emsg);
			String dmsg = new String(AES.decrypt("ewIjB2qLri0H924yDcE4lQ==", "123456789"));
			System.out.println("dmsg "+dmsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}