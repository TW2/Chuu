/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Naruto
 */
public class Blowfish {
    
    private static Key secretKey = new SecretKeySpec(new byte[]{'0','/','5','%','*',';','.','!'}, "Blowfish");

    public Blowfish() {
    }
    
    public Key getSecretKey() {
        return secretKey;
    }
    
    public byte[] getSecretKeyInBytes() {
        return secretKey.getEncoded();
    }
 
 
    public void setSecretKey(Key secretKey) {
        Blowfish.secretKey = secretKey;
    }
    
    public void setSecretKey(byte[] keyData) {
        secretKey = new SecretKeySpec(keyData, "Blowfish");    
    }
    
    public static byte[] crypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plaintext);    
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {System.out.println(e);} 
        return null;
    }
 
 
    public static byte[] crypt(String plaintext) {
        return crypt(plaintext.getBytes());
    }
 
 
    public static byte[] decryptInBytes(byte[] ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(ciphertext);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {System.out.println(e);} 
        return null;
    }
 
    public static String decryptInString(byte[] ciphertext) {
        return new String(decryptInBytes(ciphertext));
    }
    
    
}
