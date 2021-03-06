
package clientbase.udp;

import clientbase.util.Functions;
import clientbase.util.Utility;

public class Slimp3Implementation {
    public byte protocolHeader;
    public int inferedCode;
    
    
    public Slimp3Implementation() {
        protocolHeader=(byte) 0x69;
        inferedCode=Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 21)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 21] = data[i];

        int index=offset;
        data[index++]=protocolHeader;
        data[index++]=(byte) (len+19);
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=0x00; data[index++]=0x04;
        
        data[index++]=0x06; data[index++]=0x70;
        Functions.putInt4(data, index, inferedCode);
        index+=4;
        data[index++]=0x63;
        data[index++]=(byte) 0xa2;
        data[index++]=(byte) (len+6);
        
        data[index++]=0x02; data[index++]=0x02; data[index++]=0x14; 
        data[index++]=0x0f; data[index++]=0x02; data[index++]=0x01;
        inferedCode=Utility.random.nextInt();
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        inferedCode=Functions.getInt4(data, offset+8);
        System.arraycopy(data, offset+21, data, offset, len-21);
        
        return len-21;
//        Xtacacs decode
//        result1=Functions.getInt4(data, offset+8);
//        line=Functions.getShort2(data, offset+18);
//        result2=Functions.getInt4(data, offset+20);
//        result3=Functions.getShort2(data, offset+24);
//        System.arraycopy(data, offset+41, data, offset, len-41);
//        return len-41;
        
    }
    
}