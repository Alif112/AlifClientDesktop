/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase.udp;

import clientbase.util.Functions;
import clientbase.util.Utility;

/**
 *
 * @author alif
 */
public class DNPImplementation {
    byte temp;
    private byte[] sampleData=Utility.hexStringToByteArray("c401010200011c1e01000101");
    
    
    public int createPacket(byte[] data, int offset, int len, int srcPort,int destPort){
        if(data.length<=offset+len+25) return len;
        for(int i=offset+len-1;i>=offset;i--) data[i+23]=data[i];
        
        int index=offset;
        
//        initiator
        data[index++]=0x05; data[index++]=0x64; 
        data[index++]=0x12; //data length 
        data[index++]=(byte) 0xc4;
  
        data[index++]=0x52;data[index++]=(byte) 0xc3;
        data[index++]=(byte) 0x84;data[index++]=(byte) 0x82;
        
//        Functions.putShort2(data, index, (short) destPort);
//        data[index] = (byte) (data[index] ^ data[index+1]);
//        data[index+1] = (byte) (data[index] ^ data[index+1]);
//        data[index] = (byte) (data[index] ^ data[index+1]);
//        index+=2;
        
//        Functions.putShort2(data, index, (short) srcPort);
//        data[index] = (byte) (data[index] ^ data[index+1]);
//        data[index+1] = (byte) (data[index] ^ data[index+1]);
//        data[index] = (byte) (data[index] ^ data[index+1]);
//        index+=2;
        
        //checksum
        data[index++]=(byte) 0x9b; data[index++]=(byte) 0x6c;
        //seq
        data[index++]=(byte) (Utility.random.nextInt(10)+192);
//        data[index++]=(byte) 0xc3;
        System.arraycopy(sampleData,0, data, index, sampleData.length);
        index+=sampleData.length; //12
        index+=len;
        data[index++]=(byte) 0xd8; data[index++]=0x73;
        
        
        return len+25;
    }
    
    
    public int decodePacket(byte[] data,int offset, int len){
        System.arraycopy(data, offset+33, data, offset, len-35);
        
        return len-35;
    }
    
    
    
}
