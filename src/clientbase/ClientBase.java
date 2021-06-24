package clientbase;


import clientbase.util.Utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientBase {

    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean check=true;
    static int offset=0;
    static InetAddress sia,ia;
    static int minimumPortRange=1, maximumPortRange=65000;
    static int sequenceNumber=0;
    
    
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException, IOException {
        
        int i=0;
        System.out.println("Udp Client Started...........");
        
        boolean isread=ConfigCreator.readConfiguration();
        ConfigCreator.showConfig(ConfigCreator.configuration);
        if(isread) ConfigCreator.loadConfig(ConfigCreator.configuration);
        ConfigCreator.showConfigAfter();
        
        ConfigReceiver.receiveConfig(ConfigCreator.serverConfigIP, ConfigCreator.serverConfigPort);
        
        ConfigCreator.showConfigAfter();
        
        
        ia=InetAddress.getByName(ConfigCreator.ip);
        sia=InetAddress.getByName("10.0.0.2");
        check=true;
        DatagramSocket ds;
        Socket skt;
        MySenderTCP mySenderTcp;
        if(ConfigCreator.protocolType==0){
            switch (ConfigCreator.socketType) {
                case -1:
                    if(ConfigCreator.multiUser==1){
                        check=false;
                        ConfigCreator.numberOfPackets=99999;
                        ds=new DatagramSocket();
                        MySenderUDP mySender=new MySenderUDP(ds);
                        mySender.init();
                    }else{
                        check=false;
                        ConfigCreator.numberOfPackets=99999;
                        ds=new DatagramSocket();
                        MySenderUDP mySender=new MySenderUDP(ds);
                        mySender.init();
                    }
                    i++;
                    break;
                case 0:
                    ConfigCreator.serverSocketPort=minimumPortRange-1;
                    while(true){
                        ds=new DatagramSocket();
                        MySenderUDP mySender = new MySenderUDP(ds);
                        mySender.init();
                    }    
                    
                default:
                    while(true){

                        ds=new DatagramSocket();
                        MySenderUDP mySender = new MySenderUDP(ds);
                        mySender.init();
                        i++;
                    }  
                
                }
        }else if(ConfigCreator.protocolType==1){
            switch(ConfigCreator.socketType){
                case -1:
                    ConfigCreator.numberOfPackets=99999;
                    skt=new Socket(ConfigCreator.ip,ConfigCreator.serverSocketPort);
                    mySenderTcp=new MySenderTCP(skt);
                    mySenderTcp.init();
                    break;
                default:
                    while(true){
                        skt=new Socket(ConfigCreator.ip,ConfigCreator.serverSocketPort);
                        mySenderTcp=new MySenderTCP(skt);
                        mySenderTcp.init();
                    }
                    
            }
        }
    }

    private static class MySenderUDP{
        DatagramSocket ds;
        public MySenderUDP(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0,j=0;
                
                Thread myReceiver=new MyReceiverUDP(ds);
                myReceiver.start();
                int len2=0;
                while(i<ConfigCreator.numberOfPackets){

                    offset=0;
                    
                    byte[] newdata=new byte[offset+ConfigCreator.dataLen+1000];
                    int sendDataLen=Utility.getRandomData(newdata, offset, ConfigCreator.dataLen);
                    if(sequenceNumber==256) sequenceNumber=0;
                    newdata[sendDataLen]=(byte) sequenceNumber++;
                    String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
//                    System.out.println("--------------> "+(sendDataLen+1));
//                    System.out.println(m1);


                    switch(ConfigCreator.protocolNumber){
                        case Constants.UDP100:
                            len2=Constants.udp100.createPacket(newdata, offset, sendDataLen, ConfigCreator.header, ConfigCreator.headerLen);
                            break;
                        case Constants.UFTP:
                            len2=Constants.uftp.createPacket(newdata, offset, sendDataLen+1,ia,sia);
                            break;
                        case Constants.CIGI:
                            len2=Constants.cigi.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.NFS:
                            len2=Constants.nfs.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.NTP:
                            len2=Constants.ntp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.SNMP:
                            len2=Constants.snmp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.CLDAP:
                            len2=Constants.cldap.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.L2TP:
                            len2=Constants.l2tp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.BFD:
                            len2=Constants.bfd.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.WSP:
                            len2=Constants.wsp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.MOUNT:
                            len2=Constants.mount.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.STAT:
                            len2=Constants.stat.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.ICMPV6:
                            len2=Constants.icmpv6.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.LOWPAN:
                            len2=Constants.lowpan.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.DSPV2:
                            len2=Constants.dspv2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.TEPV1:
                            len2=Constants.tepv1.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.DPPV2:
                            len2=Constants.dppv2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.COAP:
                            len2=Constants.coap.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.TFTP2:
                            len2=Constants.tftp2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.IPV6:
                            len2=Constants.ipv6.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.LTP:
                            len2=Constants.ltp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.XTACACS:
                            len2=Constants.xtacacs.createPacket(newdata, offset, sendDataLen+1, (Inet4Address) ia,ConfigCreator.serverSocketPort);
                            break;
                        case Constants.ISAKMP:
                            len2=Constants.isakmp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.BVLC:
                            len2=Constants.bvlc.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.MMSE:
                            len2=Constants.mmse.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.SLIMP3:
                            len2=Constants.slimp3.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.AUTORP:
                            len2=Constants.autorp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.MIOP:
                            len2=Constants.miop.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.EDONKEY:
                            len2=Constants.edonkey.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.UAUDP:
                            len2=Constants.uaudp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.DROPBOX:
                            len2=Constants.dropbox.createPacket(newdata, offset, sendDataLen+1,ConfigCreator.serverSocketPort);
                            break;
                        case Constants.RDT:
                            len2=Constants.rdt.createPacket(newdata, offset, sendDataLen+1);
                            break;
//                        case Constants.MAC_TELNET:
//                            len2=Constants.macTelnet.createPacket(newdata, offset, sendDataLen+1);
//                            break;
                        case Constants.DCP_PFT:
                            len2=Constants.dcp_pft.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case Constants.WIREGUARD:
                            len2=Constants.wireGuard.createPacket(newdata, offset, sendDataLen+1);
                        case Constants.DNP: 
                            len2=Constants.dnp.createPacket(newdata, offset, sendDataLen+1, ds.getPort(), ConfigCreator.serverSocketPort);
                    }
                    
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("sending len ================================>          "+ len2);
//                    System.out.println(m);
                  
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName(ConfigCreator.ip);
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,ConfigCreator.serverSocketPort);
                    ds.send(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
                    
                    totalSend+=1;
                    System.out.println(ConfigCreator.protocolName+"  Total Packet Sent "+(sendDataLen+1)+" bytes-----> "+ totalSend);
                    Thread.sleep(ConfigCreator.delay);
                    i++;
                    
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static class MyReceiverUDP extends Thread {
        DatagramSocket ds;
        public MyReceiverUDP(DatagramSocket ds) {
            this.ds=ds;
        }

        @Override
        public void run() {
            try {
                int startTime=(int)System.nanoTime();
                int countreceive=0,len2=0;

                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==ConfigCreator.numberOfPackets){
                        ds.close();
                        System.out.println("------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
//                    String received= new String(dp1.getData(),0,b1.length);
                    switch(ConfigCreator.protocolNumber){
                        case Constants.UDP100:
                            len2=Constants.udp100.decodePacket(b1, 0, dp1.getLength(),ConfigCreator.headerLen);
                            break;
                        case Constants.UFTP:
                            len2=Constants.uftp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.CIGI:
                            len2=Constants.cigi.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.NFS:
                            len2=Constants.nfs.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.NTP:
                            len2=Constants.ntp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.SNMP:
                            len2=Constants.snmp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.L2TP:
                            len2=Constants.l2tp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.BFD:
                            len2=Constants.bfd.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.WSP:
                            len2=Constants.wsp.decodePacket(b1, 0, dp1.getLength());
                            break;   
                        case Constants.MOUNT:
                            len2=Constants.mount.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.STAT:
                            len2=Constants.stat.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.ICMPV6:
                            len2=Constants.icmpv6.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.LOWPAN:
                            len2=Constants.lowpan.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.DSPV2:
                            len2=Constants.dspv2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.TEPV1:
                            len2=Constants.tepv1.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.DPPV2:
                            len2=Constants.dppv2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.COAP:
                            len2=Constants.coap.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.TFTP2:
                            len2=Constants.tftp2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.IPV6:
                            len2=Constants.ipv6.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.LTP:
                            len2=Constants.ltp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.XTACACS:
                            len2=Constants.xtacacs.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.ISAKMP:
                            len2=Constants.isakmp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.BVLC:
                            len2=Constants.bvlc.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.MMSE:
                            len2=Constants.mmse.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.SLIMP3:
                            len2=Constants.slimp3.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.AUTORP:
                            len2=Constants.autorp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.MIOP:
                            len2=Constants.miop.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.EDONKEY:
                            len2=Constants.edonkey.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.UAUDP:
                            len2=Constants.uaudp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.DROPBOX:
                            len2=Constants.dropbox.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.RDT:
                            len2=Constants.rdt.decodePacket(b1, 0, dp1.getLength());
                            break;
//                        case Constants.MAC_TELNET:
//                            len2=Constants.macTelnet.decodePacket(b1, 0, dp1.getLength());
//                            break;
                        case Constants.DCP_PFT:
                            len2=Constants.dcp_pft.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case Constants.WIREGUARD:
                            len2=Constants.wireGuard.decodePacket(b1, offset, dp1.getLength());
                        case Constants.DNP:
                            len2=Constants.dnp.decodePacket(b1, offset, dp1.getLength());
                                
                            
                    }
                    
                    
//                    System.out.println("=======================> "+len2);
//                    String ack=Utility.bytesToHex(b1, 0, len2);                   
//                    System.out.println(ack);
//                    
                    
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received Len= "+len2+" at client -----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
    public static class MySenderTCP{
        Socket skt;
        
        public MySenderTCP(Socket skt) {
            this.skt=skt;
        }
        
        
            
            public void init() {
                try {
                    
                    /***
                     * These swtich are for handshaking checking
                     * **/
                    boolean isHandShake=false,handShakeReq=false;
                    switch (ConfigCreator.protocolNumber){

                        case Constants.IMAP:
                            isHandShake=Constants.imap.imapHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case Constants.SMTP:
                            isHandShake=Constants.smtp.smtpHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case Constants.IPA:
                            isHandShake=Constants.ipa.ipaHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case Constants.CQL:
                            isHandShake=Constants.cql.cqlHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                    }

                    if(handShakeReq && !isHandShake) throw new Exception("HandShaking Failed!!!");

                    OutputStream os = skt.getOutputStream();
                    InputStream is = skt.getInputStream();

                    Thread myReceiver=new MyReceiverTCP(skt,is);
                    myReceiver.start();

                    int i=0,len2=0,sendDataLen;
                    int countsend=0;
                    while (i<ConfigCreator.numberOfPackets){
                        byte[] newdata=new byte[offset+ConfigCreator.dataLen+500];
                        sendDataLen=Utility.getRandomData(newdata, offset, ConfigCreator.dataLen);
                        if(sequenceNumber==256) sequenceNumber=0;
                        newdata[sendDataLen]=(byte) sequenceNumber++;
                        
                        String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
//                        System.out.println("---> sendting multi pkt --> \n"+m1);
                        
                        switch (ConfigCreator.protocolNumber){
                            case  Constants.NINEP2000:
                                len2=Constants.nineP2000.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.COPS:
                                len2=Constants.cops.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.EXEC:
                                len2=Constants.exec.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case Constants.TCP:
                                len2=Constants.tcp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.IMAP:
                                len2=Constants.imap.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.SMTP:
                                len2=Constants.smtp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.IPA:
                                len2=Constants.ipa.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.CQL:
                                len2=Constants.cql.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  Constants.BGP:
                                len2=Constants.bgp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                        }

                        String m=Utility.bytesToHex(newdata,offset,len2);
//                        System.out.println(m);
                        byte[] data=Utility.hexStringToByteArray(m);
                        os.write(data);

                        countsend++;
                        
                        totalSend+=1;
                        System.out.println(ConfigCreator.protocolName+ " Packet Send "+(sendDataLen+1)+"------------------------> "+ totalSend);

                        Thread.sleep(ConfigCreator.delay);
                        i++;

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    public static class MyReceiverTCP extends Thread{
            Socket skt;
            InputStream is;
            MyReceiverTCP(Socket skt, InputStream is){
                this.skt=skt;
                this.is=is;
            }

            @Override
            public void run() {
                try {
                    int startTime=(int)System.nanoTime();
                    int countreceive=0;
                    byte[] data= new byte[2048];
                    while(true){
                        int currentTime=(int)System.nanoTime();
                        int checkTime=currentTime-startTime;
                        if((checkTime>=receiverTime || countreceive==ConfigCreator.numberOfPackets) && ConfigCreator.socketType!=-1){
                            skt.close();
                            System.out.println("-------------------Socket closed-----> "+skt);
                            break;
                        }

                        countreceive+=1;
                        int len2=0;
                        switch (ConfigCreator.protocolNumber){
                            case Constants.NINEP2000:
                                len2=Constants.nineP2000.decodePacket(data,offset,is);
                                break;
                            case Constants.COPS:
                                len2=Constants.cops.decodePacket(data,offset,is);
                                break;
                            case Constants.EXEC:
                                len2=Constants.exec.decodePacket(data,offset,is);
                                break;
                            case Constants.TCP:
                                len2=Constants.tcp.decodePacket(data,offset,is);
                                break;
                            case Constants.IMAP:
                                len2=Constants.imap.decodePakcetAtClient(data,offset,is);
                                break;
                            case Constants.SMTP:
                                len2=Constants.smtp.decodePacket(data,offset,is);
                                break;
                            case Constants.IPA:
                                len2=Constants.ipa.decodePacketAtClient(data,offset,is);
                                break;
                            case Constants.CQL:
                                len2=Constants.cql.decodePacketAtClient(data,offset,is);
                                break;
                            case Constants.BGP:
                                len2=Constants.bgp.decodePacket(data,offset,is);
                                break;

                        }
                        if(len2<0){
                            System.out.println("-------------------------------------------> "+len2);
                            break;
                        }
//                        System.out.println("==============================> "+len2);
//                        String ack=Utility.bytesToHex(data, offset, len2);
//                        System.out.println(ack);
//                        
                        totalReceive+=1;
                        
                        System.out.println("Total Received Len= "+len2+" at client ---------------> "+ totalReceive);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    
}
