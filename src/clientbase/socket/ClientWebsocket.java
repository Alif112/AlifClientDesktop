package clientbase.socket;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ClientWebsocket {
    Socket tlsSocket;
    public static Socket createSSLSocket(InetAddress address, int port) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());


        final SSLSocketFactory delegate = sslContext.getSocketFactory();
        SocketFactory factory = new SSLSocketFactory() {
            @Override
            public Socket createSocket(String host, int port)
                    throws IOException {

                InetAddress addr = InetAddress.getByName(host);
                injectHostname(addr, host);
                return delegate.createSocket(addr, port);
            }

            @Override
            public Socket createSocket(InetAddress host, int port)
                    throws IOException {

                injectHostname(host, host.getHostAddress());
                return delegate.createSocket(host, port);
            }

            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                    throws IOException {
                InetAddress addr = InetAddress.getByName(host);
                injectHostname(addr, host);
                return delegate.createSocket(host, port, localHost, localPort);
            }

            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                    throws IOException {
                injectHostname(address, address.getHostAddress());
                return delegate.createSocket(address, port, localAddress, localPort);
            }

            private void injectHostname(InetAddress address, String host) {
                try {
                    Field field = InetAddress.class.getDeclaredField("hostName");
                    field.setAccessible(true);
                    field.set(address, host);
                } catch (Exception ignored) {
                }
            }

            @Override
            public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {

                injectHostname(s.getInetAddress(), host);
                return delegate.createSocket(s, host, port, autoClose);
            }

            @Override
            public String[] getDefaultCipherSuites() {
                return delegate.getDefaultCipherSuites();
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return delegate.getSupportedCipherSuites();
            }
        };

        SSLSocket sslSocket = (SSLSocket) factory.createSocket(address, port);
//            System.out.println("Starting ssl handshake");
        sslSocket.startHandshake();
        return sslSocket;
    }

    @Override
    public String toString() {
        if(tlsSocket == null){
            return "null";
        }
        return tlsSocket.toString();
    }
}
