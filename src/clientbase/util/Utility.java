package clientbase.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class Utility {
    
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static Random random = new Random();
    public static int getRandomData(byte [] array,int offset,int len) {
            for(int i=0;i<len;i++){
                array[offset+i] = (byte) random.nextInt(256);
            }
            return offset+len;
    }
    public static byte[] getRandomData(byte [] array,int len) {
            
            for(int i=0;i<len;i++){
                array[i] = (byte) random.nextInt(256);
            }
            return array;
    }
    
    
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
//            if(data[i/2]<0) data[i/2]*=-1;
        }
        return data;
    }
    
    public static String byteToHex(byte bytes) {
        char[] hexChars = new char[2];
            int v = bytes & 0xFF;
            hexChars[0] = hexArray[v >>> 4];
            hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }
    

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String bytesToHex(byte[] bytes, int offset, int len) {
        char[] hexChars = new char[len * 2];
        for (int j = 0; j < len; j++) {
            int v = bytes[j+offset] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    static byte[] l1=new byte[4];
    public static int buildLen4(InputStream is) throws IOException {
        for(int j=0;j<4;j++)
            l1[j]=(byte) is.read();
        int createLen=Functions.getInt4(l1, 0);
        return createLen;
    }
    
    public static short buildLen2(InputStream is) throws IOException {
        return (short) (((is.read() & 0xff) << 8) | (is.read() & 0xff));
    }
    
    public static int readLen2(InputStream is) throws IOException{
        return ((is.read() & 0xff) << 8) | (is.read() & 0xff);
    }
    
    public static int readLen4(InputStream is) throws IOException{
        return ((is.read() & 0xff) << 24) |
                ((is.read() & 0xff)<<16) |
                ((is.read() & 0xff)<<8) |
                (is.read() & 0xff);
    }
    
}