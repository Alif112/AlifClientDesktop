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
 * @author User
 */
public class DCP_PFTImplementation {
    private short sequenceNumber,fragmentIndex,FragmentCount,srcAddr,destAddr;
    
    
    
    public DCP_PFTImplementation() {
        sequenceNumber=(short) Utility.random.nextInt();
        fragmentIndex=(short) Utility.random.nextInt();
        FragmentCount=1;
        srcAddr=(short) Utility.random.nextInt();
        destAddr=(short) Utility.random.nextInt();
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 20)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 20] = data[i];

        int index=offset;
        data[index++]=0x50; data[index++]=0x46;  //sync PF
        Functions.putShort2(data, index, sequenceNumber);
        index+=2;
        data[index++]=0x00; 
        Functions.putShort2(data, index, fragmentIndex);
        index+=2;
        data[index++]=0x00; 
        Functions.putShort2(data, index, FragmentCount);
        index+=2;
        data[index++]=(byte) 0xff; data[index++]=(byte) 0xff; 
        data[index++]=(byte) Utility.random.nextInt();
        data[index++]=(byte) Utility.random.nextInt();
        Functions.putShort2(data, index, srcAddr);
        index+=2;
        Functions.putShort2(data, index, destAddr);
        index+=2;
        data[index++]=0x41; data[index++]=0x41;
        
        sequenceNumber=(short) Utility.random.nextInt();
        fragmentIndex=(short) Utility.random.nextInt();
        FragmentCount+=1;
        
        return 20+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        System.arraycopy(data, offset+20, data, offset, len-20);
        
        return len-20;
    }
    
}