package com.lucene.webSearch.utility;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

//import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Cryptography {

	//final static Logger log = Logger.getLogger(Cryptography.class); 
	
	@SuppressWarnings("restriction")
	public static String encrypt(String str) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(str.getBytes());
	}

	@SuppressWarnings("restriction")
	public static String decrypt(String encstr) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				return new String(decoder.decodeBuffer(encstr));
			} catch (IOException e) {
				System.err.println("Exception: " + e);
				// throw new InvalidImplementationException(
				// Fail
		}
		return null;
	}
	
	public static String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    	System.err.println("Exception: " + e);
		    }
		    return null;
		}
	
	public static void main (String... args)
	{
		String username = "username";
		String password = "password";
		
//		System.out.println(MD5(username)swipedata);
		
		username = Cryptography.encrypt(username);
		password = Cryptography.encrypt(password);
		System.out.println(username);
		System.out.println(password);
		
		System.out.println(Cryptography.decrypt("encrypted_userName"));
		System.out.println(Cryptography.decrypt("encrypted_password"));
		
		for(String arg : args)
		{
			System.out.println(arg+"="+Cryptography.encrypt(arg));
		}
	
		System.out.println(MD5("test"));
		
	}

}
