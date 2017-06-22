/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.crypto;

import java.io.IOException;

/**
 *
 * @author Naruto
 */
public class Do {
    
    public static String toEncryptedString(String text){
        String enc = Codec.encode(text);
        byte[] bytes = Blowfish.crypt(enc);
        String b64 = Base64.encodeBytes(bytes);
        return b64;
    }
    
    public static String fromEncryptedString(String b64) throws IOException{
        byte[] bytes = Base64.decode(b64);
        String enc = Blowfish.decryptInString(bytes);
        String s = Codec.decode(enc);
        return s;
    }
    
    public static String toEncryptedByte(byte[] stream){
        String text = Base64.encodeBytes(stream);
        String enc = Codec.encode(text);
        byte[] bytes = Blowfish.crypt(enc);
        String b64 = Base64.encodeBytes(bytes);
        return b64;
    }
    
    public static byte[] fromEncryptedByte(String b64) throws IOException{
        byte[] bytes = Base64.decode(b64);
        String enc = Blowfish.decryptInString(bytes);
        String s = Codec.decode(enc);
        byte[] b = Base64.decode(s);
        return b;
    }
}
