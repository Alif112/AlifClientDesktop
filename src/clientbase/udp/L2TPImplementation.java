
package clientbase.udp;

import clientbase.util.Functions;

/**
 * @author alif
 */

public class L2TPImplementation {
    public int protocolLen;
    public byte numberOfZeros;
    public int numberOfBlock,newLen,dataIndex,mod,extraSize,shift;
    
    public int createPacket(byte [] data, int offset, int len){
        newLen=len-7;
        dataIndex=offset;
        mod=newLen%8;
        if(mod==0){
            numberOfBlock=newLen/8;
            numberOfZeros=0;
        }else{
            numberOfBlock=newLen/8+1;
            numberOfZeros=(byte) (8-mod);
//            System.out.println("NOZ=============> "+numberOfZeros);
        } 
        extraSize=numberOfBlock*2+12;
//        shift=(numberOfBlock*2+12)-numberOfZeros;
        
        if(data.length <= offset + len + extraSize) return len;
        
        protocolLen=numberOfBlock*10+12;
        byte[] tempData=new byte[protocolLen];
        
        int index=offset;
        tempData[index++]=(byte) 0xc8;
        tempData[index++]=0x02;
        Functions.putInt(tempData, index, protocolLen);
        index+=2;
        tempData[index++]=numberOfZeros;
        System.arraycopy(data, offset, tempData, index, 7);
        index+=7;
        dataIndex+=6;
        for(int i=0;i<numberOfBlock-1;i++){
            tempData[index++]=(byte) 0x80;
            tempData[index++]=0x0a;
            System.arraycopy(data, dataIndex+1, tempData, index, 8);
            index+=8;
            dataIndex+=8;
        }
        
        tempData[index++]=(byte) 0x80;
        tempData[index++]=0x0a;
        
        for(int i=dataIndex+1;i<len;i++){
            tempData[index++]=data[i];
        }
        
        int loop=numberOfZeros;
        while(loop!=0){
            tempData[index++]=0x00;
            loop--;
        }
        System.arraycopy(tempData, offset, data, offset, protocolLen);
        return 12+numberOfBlock*10;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        for(int i=offset;i<offset+8;i++){
            data[i]=data[i+4];
        }
        
        for(int i=offset+15;i<offset+len;i++)
            data[i-7]=data[i];
        return len-7;
    }
    
}
