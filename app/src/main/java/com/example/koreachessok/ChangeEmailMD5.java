package com.example.koreachessok;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangeEmailMD5 {
    public static String encrypt(String str){
        final String MD5 = "MD5";
        try{
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(str.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }
}
