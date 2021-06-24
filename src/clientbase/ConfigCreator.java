/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase;


import clientbase.util.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author alif
 */
public class ConfigCreator {
    public static HashMap configuration=new HashMap<String,String> ();
    public static int protocolNumber;
    public static int protocolType;
    public static String protocolName;
    public static String ip;
    public static int serverSocketPort;
    public static int dataLen;
    public static int delay;
    public static int socketType;
    public static int numberOfPackets;
    public static byte[] header;
    public static int headerLen;
    public static int multiUser;
    public static int numberOfUser;
    public static int minimumPortRange;
    public static int maximumPortRange;
    public static String serverConfigIP;
    public static int serverConfigPort;
    
    
    
    
    
    public static boolean readConfiguration(){
        File file = new File(Constants.filename);
        if(!file.exists())return false;
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader br=new BufferedReader(fileReader);
            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("#"))continue;
                String [] str = line.split("=");
                if(str.length < 2)continue;
                configuration.put(str[0].toLowerCase(), str[1]);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void showConfig(HashMap<String, String> config){
        config.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }
    
        public static void loadConfig(HashMap<String, String> config) {
        String str=config.get(Constants.protocolNumber.toLowerCase());
        if(str!=null) protocolNumber=Integer.parseInt(str.trim());
        str = config.get(Constants.protocolType.toLowerCase());
        if(str != null){
            protocolType=Integer.parseInt(str.trim());
            if(protocolType==0) protocolName=Constants.protocolNameListUDP[protocolNumber-1000];
            else protocolName=Constants.protocolNameListTCP[protocolNumber-2000];
        }
        str=config.get(Constants.fixedClientIP.toLowerCase());
        if(str!=null) ip=str.trim();
        str=config.get(Constants.fixedClientPort.toLowerCase());
        if(str!=null) serverSocketPort=Integer.parseInt(str.trim());
        str=config.get(Constants.dataLen.toLowerCase());
        if(str!=null) dataLen=Integer.parseInt(str.trim());
        str=config.get(Constants.delay.toLowerCase());
        if(str!=null) delay=Integer.parseInt(str.trim());
        
        str=config.get(Constants.socketType.toLowerCase());
        if(str!=null) socketType=Integer.parseInt(str.trim());
        str=config.get(Constants.numberOfPacketsPerSocket.toLowerCase());
        if(str!=null) numberOfPackets=Integer.parseInt(str.trim());
        str=config.get(Constants.rtpHeader.toLowerCase());
        if(str!=null) {
            header=Utility.hexStringToByteArray(str.trim());
            headerLen=header.length;
        }
        
        str=config.get(Constants.multiUser.toLowerCase());
        if(str!=null) multiUser=Integer.parseInt(str.trim());
        str=config.get(Constants.numberOfUser.toLowerCase());
        if(str!=null) numberOfUser=Integer.parseInt(str.trim());
        
        str=config.get(Constants.minimumPortRange.toLowerCase());
        if(str!=null) minimumPortRange=Integer.parseInt(str.trim());
        str=config.get(Constants.maximumPortRange.toLowerCase());
        if(str!=null) maximumPortRange=Integer.parseInt(str.trim());
        
        str=config.get(Constants.serverConfigIP.toLowerCase());
        if(str!=null) serverConfigIP=str.trim();
        
        str=config.get(Constants.serverConfigPort.toLowerCase());
        if(str!=null) serverConfigPort=Integer.parseInt(str.trim());
        
    }


    public static void showConfigAfter(){
        System.out.println("-------------------------------------------------------");
        System.out.println("protocol number = "+protocolNumber);
        System.out.println("protocol name = "+protocolName);
        System.out.println("protocol Type = "+protocolType);
        System.out.println("IP = "+ip);
        System.out.println("Port = "+serverSocketPort);
        System.out.println("data len = "+dataLen);
        System.out.println("delay = "+delay);
        System.out.println("socket type = "+socketType);
        System.out.println("Number of packet per socket = "+numberOfPackets);
        System.out.println("rtp Header = "+header);
        System.out.println("Header Length = "+headerLen);
        System.out.println("Multi User = "+multiUser);
        System.out.println("Number of User = "+numberOfUser);
        System.out.println("minimum port range = "+minimumPortRange);
        System.out.println("maximum port range = "+maximumPortRange);
        System.out.println("-------------------------------------------------------\n\n");
    }
    
        
        
    
}
