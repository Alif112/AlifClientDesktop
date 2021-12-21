package clientbase.socket;

import clientbase.config.ConfigCreator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class TCPSocketManager extends Thread{
    private final ArrayList<TCPReceiver> receiverList;
    public boolean running;
    private InetSocketAddress serverAddress;

    public TCPSocketManager(ArrayList<TCPReceiver> receiverList) {
        this.receiverList = receiverList;
    }

    @Override
    public void run() {
        try {
            while (running){
                int receiverCount=1;
                while (receiverList.size() < receiverCount) {
                    TCPReceiver receiver = null;

                    ///if social bypass is enabled set the social packet sending limit to the receiver
                    if (ConfigCreator.enableSocialBypass == 1) {
                        receiver = new TCPReceiver(this, serverAddress,
                                ConfigCreator.protocolNumber, ConfigCreator.header,ConfigCreator.numberOfPackets, ConfigCreator.domain);
                    } else {
                        receiver = new TCPReceiver(this,
                                serverAddress, ConfigCreator.protocolNumber, ConfigCreator.header,ConfigCreator.HUGE_NUMBER, ConfigCreator.domain);
                    }
                    receiver.start();
                    receiverList.add(receiver);
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean sendMessage(byte [] data, int offset, int len) throws IOException, InterruptedException {
        synchronized (receiverList) {
            for (TCPReceiver receiver : receiverList) {
                if (receiver.sendMessage(data, offset, len)) {
                    return true;
                }
            }
        }
        return false;
    }

}
