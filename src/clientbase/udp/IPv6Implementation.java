package clientbase.udp;

import clientbase.util.Functions;
import clientbase.util.Utility;

/**
 * @author alif
 */
public class IPv6Implementation {
    public short orginUdpPort;
    public int originAddress;
    public static byte ipVersion;
    public static short payloadLength;
    public static byte nextHeaderConfirmation;
    public static byte hopLimit;
    byte[] destData;
    byte[] srcData;
    
    static{
        ipVersion=0x60;
        payloadLength=0;
        nextHeaderConfirmation=0x3b;
        hopLimit=0x00;
    }
    
    
    public IPv6Implementation() {
        orginUdpPort=(short) Utility.random.nextInt();
        originAddress=Utility.random.nextInt();
        destData=new byte[8];
        srcData=new byte[8];
    }
    
    public int createPacket(byte [] data, int offset, int len){
        synchronized(this){
            if(data.length <= offset + len + 48)
                return len;
            for(int i = offset + len - 1; i >=offset+16; i--)
                data[i + 32] = data[i];
            int index=offset;
            for(int i=offset+15,j=7;i>=offset+8;i--,j--) destData[j]=data[i];

            for(int i=offset+7,j=7;i>=offset;i--,j--) srcData[j]=data[i];

            /***teredo started***/
            data[index++]=0x00; data[index++]=0x00;
            Functions.putShort2(data, index, orginUdpPort);
            index+=2;
            Functions.putInt4(data, index, originAddress);
            index+=4;

            /***teredo finished***/

            /**IPv6 started**/
            data[index++]=ipVersion;
            data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
            Functions.putShort2(data, index, payloadLength);
            index+=2;
            data[index++]=nextHeaderConfirmation;
            data[index++]=hopLimit;

    //        copy src
            data[index++]=(byte) 0xfe; data[index++]=(byte) 0x80; data[index++]=0x00; data[index++]=0x00;
            data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
            System.arraycopy(srcData, 0, data, index, 8);
            index+=8;
    //        copy dest
            data[index++]=0x20; data[index++]=0x01; data[index++]=0x00; data[index++]=0x00;
            data[index++]=0x41; data[index++]=0x37; data[index++]=(byte) 0x9e; data[index++]=0x50;
            System.arraycopy(destData, 0, data, index, 8);
            index+=8;

        /**IPv6 Finished**/
        
        }
        orginUdpPort=(short) Utility.random.nextInt();
        originAddress=Utility.random.nextInt();
        return len+32;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
//        orginUdpPort=Functions.getShort2(data, offset+2);
//        originAddress=Functions.getInt4(data, offset+4);
        
        for(int i=offset;i<offset+8;i++) data[i]=data[i+24];
        for(int i=offset+8;i<offset+len-32;i++) data[i]=data[i+32];
        return len-32;
    }
    
}