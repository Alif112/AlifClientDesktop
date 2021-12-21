package clientbase.config;

import clientbase.util.Constants;
import clientbase.util.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alif
 */
public class ConfigReceiver {
    static Socket socket;
    public static int dataLen=0,offset=0;
    public static int loadConfigFromServer;
    
    
    public static void receiveConfig(String ip, int port) throws IOException{
        System.out.println("serverConfigIP:port= "+ip+":"+port);
        byte[] data=new byte[2048];
        socket=new Socket(ip,port); 
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        
        byte[] sData="req\r\n".getBytes();
        os.write(sData);
        os.flush();
        int len=Utility.buildLen2(is);
        is.read(data, offset, len);
        
        decodeConfigPacket(data,offset,len);
        
    }
    public static HashMap configNew=new HashMap<String,String> ();
    private static void decodeConfigPacket(byte[] data, int offset, int len) {
        String str=new String(data,offset,len);
        System.out.println(str);
        String[] s1=str.split("\n");
        for(int i=0;i<s1.length;i++){
            if(s1[i].length()<2) continue;
            String[] snew=s1[i].split("=");
            configNew.put(snew[0].toLowerCase(), snew[1]);
        }
        
        String receive=(String) configNew.get(Constants.loadConfigFromServer.toLowerCase());
        if(receive!=null){
            loadConfigFromServer=Integer.parseInt(receive.trim());
        }
        if(loadConfigFromServer==0) return;
        
        ConfigCreator.loadConfig(configNew);
        
    }

}
