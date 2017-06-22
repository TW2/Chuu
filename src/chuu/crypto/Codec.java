/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chuu.crypto;

/**
 *
 * @author Naruto
 */
public class Codec {

    public Codec() {
    }
    
    public static String encode(String str){
        String result = "";
        for(char c : str.toCharArray()){
            switch(c){
                case '0': result += "A"; break;
                case '1': result += "F"; break;
                case '2': result += "B"; break;
                case '3': result += "G"; break;
                case '4': result += "C"; break;
                case '5': result += "H"; break;
                case '6': result += "D"; break;
                case '7': result += "I"; break;
                case '8': result += "E"; break;
                case '9': result += "J"; break;
                    
                case 'A': result += "0"; break;
                case 'B': result += "z"; break;
                case 'C': result += "1"; break;
                case 'D': result += "y"; break;
                case 'E': result += "2"; break;
                case 'F': result += "x"; break;
                case 'G': result += "3"; break;
                case 'H': result += "w"; break;
                case 'I': result += "4"; break;
                case 'J': result += "v"; break;
                case 'K': result += "5"; break;
                case 'L': result += "u"; break;
                case 'M': result += "6"; break;
                case 'N': result += "t"; break;
                case 'O': result += "7"; break;
                case 'P': result += "s"; break;
                case 'Q': result += "8"; break;
                case 'R': result += "r"; break;
                case 'S': result += "9"; break;
                case 'T': result += "q"; break;
                case 'U': result += "K"; break;
                case 'V': result += "p"; break;
                case 'W': result += "L"; break;
                case 'X': result += "o"; break;
                case 'Y': result += "M"; break;
                case 'Z': result += "n"; break;
                    
                case 'a': result += "N"; break;
                case 'b': result += "m"; break;
                case 'c': result += "O"; break;
                case 'd': result += "l"; break;
                case 'e': result += "P"; break;
                case 'f': result += "k"; break;
                case 'g': result += "Q"; break;
                case 'h': result += "j"; break;
                case 'i': result += "R"; break;
                case 'j': result += "i"; break;
                case 'k': result += "S"; break;
                case 'l': result += "h"; break;
                case 'm': result += "T"; break;
                case 'n': result += "g"; break;
                case 'o': result += "U"; break;
                case 'p': result += "f"; break;
                case 'q': result += "V"; break;
                case 'r': result += "e"; break;
                case 's': result += "W"; break;
                case 't': result += "d"; break;
                case 'u': result += "X"; break;
                case 'v': result += "c"; break;
                case 'w': result += "Y"; break;
                case 'x': result += "b"; break;
                case 'y': result += "Z"; break;
                case 'z': result += "a"; break;
                    
                default : result += c; break;
            }
        }
        return result;
    }
    
    public static String decode(String str){
        String result = "";
        for(char c : str.toCharArray()){
            switch(c){
                case 'A': result += "0"; break;
                case 'F': result += "1"; break;
                case 'B': result += "2"; break;
                case 'G': result += "3"; break;
                case 'C': result += "4"; break;
                case 'H': result += "5"; break;
                case 'D': result += "6"; break;
                case 'I': result += "7"; break;
                case 'E': result += "8"; break;
                case 'J': result += "9"; break;
                    
                case '0': result += "A"; break;
                case 'z': result += "B"; break;
                case '1': result += "C"; break;
                case 'y': result += "D"; break;
                case '2': result += "E"; break;
                case 'x': result += "F"; break;
                case '3': result += "G"; break;
                case 'w': result += "H"; break;
                case '4': result += "I"; break;
                case 'v': result += "J"; break;
                case '5': result += "K"; break;
                case 'u': result += "L"; break;
                case '6': result += "M"; break;
                case 't': result += "N"; break;
                case '7': result += "O"; break;
                case 's': result += "P"; break;
                case '8': result += "Q"; break;
                case 'r': result += "R"; break;
                case '9': result += "S"; break;
                case 'q': result += "T"; break;
                case 'K': result += "U"; break;
                case 'p': result += "V"; break;
                case 'L': result += "W"; break;
                case 'o': result += "X"; break;
                case 'M': result += "Y"; break;
                case 'n': result += "Z"; break;
                    
                case 'N': result += "a"; break;
                case 'm': result += "b"; break;
                case 'O': result += "c"; break;
                case 'l': result += "d"; break;
                case 'P': result += "e"; break;
                case 'k': result += "f"; break;
                case 'Q': result += "g"; break;
                case 'j': result += "h"; break;
                case 'R': result += "i"; break;
                case 'i': result += "j"; break;
                case 'S': result += "k"; break;
                case 'h': result += "l"; break;
                case 'T': result += "m"; break;
                case 'g': result += "n"; break;
                case 'U': result += "o"; break;
                case 'f': result += "p"; break;
                case 'V': result += "q"; break;
                case 'e': result += "r"; break;
                case 'W': result += "s"; break;
                case 'd': result += "t"; break;
                case 'X': result += "u"; break;
                case 'c': result += "v"; break;
                case 'Y': result += "w"; break;
                case 'b': result += "x"; break;
                case 'Z': result += "y"; break;
                case 'a': result += "z"; break;
                    
                default : result += c; break;
            }
        }
        return result;
    }
    
}
