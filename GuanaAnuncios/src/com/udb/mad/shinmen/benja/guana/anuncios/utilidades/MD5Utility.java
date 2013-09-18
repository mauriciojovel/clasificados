package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utility {
	
	public static String md5(String s) 
	{
	    MessageDigest digest;
	    try 
	    {
	        digest = MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes(),0,s.length());
	        String hash = new BigInteger(1, digest.digest()).toString(16);
	        return hash;
	    } 
	    catch (NoSuchAlgorithmException e) 
	    {
	        e.printStackTrace();
	    }
	    return "";
	}
}
