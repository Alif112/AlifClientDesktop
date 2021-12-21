package clientbase.socket;

import clientbase.config.ConfigCreator;
import clientbase.tcp.DNSImplementation;
import clientbase.util.Constants;
import clientbase.util.Functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


public class TCPReceiver extends Thread{
    private static final int BUFFER_SIZE = 4096;
    private Socket socket;
    private InetSocketAddress socketAddress;
    private int extension;
    byte[] header;
    private TCPSocketManager tcpSocketManager;
    private int messageTobeSent;
    private String domain;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean running;
    private ByteArray receiveBuffer;
    private ByteArray sendBuffer;
    private boolean receiveOnly=false;

    public TCPReceiver(TCPSocketManager tcpSocketManager, InetSocketAddress socketAddress,
                       int extension, byte[] header, int messageTobeSent, String domain){
        this.tcpSocketManager=tcpSocketManager;
        this.socketAddress=socketAddress;
        this.extension=extension;
        this.header=header;
        this.messageTobeSent=messageTobeSent;
        this.domain=domain;
        this.receiveBuffer = new ByteArray(BUFFER_SIZE);
        this.sendBuffer = new ByteArray(BUFFER_SIZE);
    }


    @Override
    public void run() {
        try {
            running=true;
            initializeSocket();
            while (running){
                receiveOnePacket();
            }

        }catch (Exception e){e.printStackTrace();}
    }

    private void receiveOnePacket() throws Exception{
        receiveBuffer.reset();
        int headerLen = 0;
        if(header != null)
            headerLen = header.length;

        /**
         * Read according to the extension
         * */
        switch (extension){
            case Constants.SDNS_SSL:
                receiveBuffer.length = Functions.readByte(inputStream, headerLen, receiveBuffer.arr, receiveBuffer.offset);
                receiveBuffer.length = DNSImplementation.parseDNSResponse(receiveBuffer.arr, receiveBuffer.offset, receiveBuffer.length);
                break;

        }

//        if(signalReceiveBuffer.length > 0){
//            tcpSocketManager.processReceivedMessgage(signalReceiveBuffer);
//        }
    }

    private void initializeSocket() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        switch (ConfigCreator.protocolNumber){
            case Constants.SDNS_SSL:
                socket=ClientWebsocket.createSSLSocket(InetAddress.getByName(ConfigCreator.dnsIp),ConfigCreator.dnsPort);
                break;
        }

        socket.setSoTimeout(120000); /// 2 minute
        socket.setTcpNoDelay(true);  /// send message instanly
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }


    public boolean sendMessage(byte[] data, int offset, int len) throws IOException {

        synchronized (this){
            encodeMessage(data, offset, len); ////shared buffer used here. that's why need to put inside synchronized

            outputStream.write(sendBuffer.arr, sendBuffer.offset, sendBuffer.length);
        }

        return true;
    }

    private void encodeMessage(byte[] data, int offset, int len) {
        sendBuffer.length=0;
        sendBuffer.offset=0;

        switch (extension){
            case Constants.SDNS_SSL:
                sendBuffer.length= DNSImplementation.prepareDNSRequest(data,offset,len,sendBuffer.arr,sendBuffer.offset+2,this.domain,3);//1 = CNAME, 2 = MX, 3/default = TXT
                Functions.putInt(sendBuffer.arr, sendBuffer.offset, sendBuffer.length);
                sendBuffer.length+=2;
                break;
        }
    }

    public boolean isAvailableForSending(){
        return !isReceiveOnly() && running;
    }

    private boolean isReceiveOnly() {
        return receiveOnly;
    }
}
