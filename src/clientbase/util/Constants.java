/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase.util;

import clientbase.tcp.*;
import clientbase.udp.AutoRPImplementation;
import clientbase.udp.BFDImplementation;
import clientbase.udp.BVLCImplementation;
import clientbase.udp.CIGIImplementation;
import clientbase.udp.CLDAPImplementation;
import clientbase.udp.CoAPImplementation;
import clientbase.udp.DCP_PFTImplementation;
import clientbase.udp.DNPImplementation;
import clientbase.udp.DPPv2Implementation;
import clientbase.udp.DSPv2Implementation;
import clientbase.udp.DropboxImplementation;
import clientbase.udp.EDonkeyImplementation;
import clientbase.udp.ICMPv6Implementation;
import clientbase.udp.IPv4WithGPRSImplementation;
import clientbase.udp.IPv6Implementation;
import clientbase.udp.ISAKMPImplementation;
import clientbase.udp.L2TPImplementation;
import clientbase.udp.LTPSegmentImplementation;
import clientbase.udp.LoWPANImplementation;
import clientbase.udp.MIOPImplementation;
import clientbase.udp.MMSEImplementation;
import clientbase.udp.MOUNTImplementation;
import clientbase.udp.NFSImplementation;
import clientbase.udp.NTPImplementation;
import clientbase.udp.RDTImplementation;
import clientbase.udp.SNDCPImplementation;
import clientbase.udp.SNMPImplementation;
import clientbase.udp.STATImplementation;
import clientbase.udp.Slimp3Implementation;
import clientbase.udp.TCPWithGPRSImplementation;
import clientbase.udp.TEPv1Implementation;
import clientbase.udp.TFTP2Implementation;
import clientbase.udp.UAUDPImplementation;
import clientbase.udp.UDP100Implementation;
import clientbase.udp.UFTPImplementation;
import clientbase.udp.WSPImplementation;
import clientbase.udp.WireGuardImplementation;
import clientbase.udp.XTACACSImplementation;

/**
 *
 * @author User
 */
public class Constants {
    public static String filename="clientSetup.txt";
    
    public static String protocolType="protocolType";
    public static String protocolNumber="protocolNumber";
    public static String fixedClientIP="fixedClientIP";
    public static String fixedClientPort="fixedClientPort";
    public static String dataLen="dataLen";
    public static String delay="delay";
    public static String socketType="socketType";
    public static String numberOfPacketsPerSocket="numberOfPacketsPerSocket";
    public static String rtpHeader="rtpHeader";
    public static String multiUser="multiUser";
    public static String numberOfUser="numberOfUser";
    public static String minimumPortRange="minimumPortRange";
    public static String maximumPortRange="maximumPortRange";
    
    public static String loadConfigFromServer="loadConfigFromServer";
    public static String serverConfigIP="serverConfigIP";
    public static String serverConfigPort="serverConfigPort";
    public static String domain="domain";
    public static String dnsIP="dnsIP";
    public static String dnsPort="dnsPort";

    
    public static String[] protocolNameListUDP={"UDP 100","UFTP","CIGI","NFS","NTP","SNMP",
                                            "CLDAP","L2TP","BFD","WSP","MOUNT",
                                            "STAT","ICMPv6","6LoWPAN","DSPv2","TEPv1",
                                            "DPPv2", "CoAP",  "TFTP", "IPv6", "LTPSegment",
                                            "XTACACS", "ISAKMP","BVLC", "MMSE","Slimp3",
                                            "AutoRP", "MIOP","eDonkey", "UAUDP","Dropbox",
                                            "RDT", "MACTelnet","DCP-PFT","WireGuard", "DNP",
                                            "TCPWithGPRS","SNDCP","IPv4WithGPRS"
                                              };

    public static UFTPImplementation uftp=new UFTPImplementation();
    public static CIGIImplementation cigi=new CIGIImplementation();
    public static NFSImplementation nfs=new NFSImplementation();
    public static NTPImplementation ntp=new NTPImplementation();
    public static SNMPImplementation snmp=new SNMPImplementation(false);
    public static CLDAPImplementation cldap=new CLDAPImplementation();
    public static L2TPImplementation l2tp=new L2TPImplementation();
    public static BFDImplementation bfd=new BFDImplementation();
    public static WSPImplementation wsp=new WSPImplementation();
    public static MOUNTImplementation mount=new MOUNTImplementation();
    public static STATImplementation stat=new STATImplementation();
    public static ICMPv6Implementation icmpv6=new ICMPv6Implementation();
    public static LoWPANImplementation lowpan=new LoWPANImplementation();
    public static DSPv2Implementation dspv2=new DSPv2Implementation();
    public static TEPv1Implementation tepv1=new TEPv1Implementation();
    public static DPPv2Implementation dppv2=new DPPv2Implementation();
    public static CoAPImplementation coap=new CoAPImplementation();
    public static TFTP2Implementation tftp2=new TFTP2Implementation();
    public static IPv6Implementation ipv6=new IPv6Implementation();
    public static LTPSegmentImplementation ltp=new LTPSegmentImplementation();
    public static XTACACSImplementation xtacacs=new XTACACSImplementation(false);
    public static ISAKMPImplementation isakmp=new ISAKMPImplementation(false);
    public static BVLCImplementation bvlc=new BVLCImplementation();
    public static MMSEImplementation mmse= new MMSEImplementation();
    public static Slimp3Implementation slimp3=new Slimp3Implementation();
    public static AutoRPImplementation autorp=new AutoRPImplementation();
    public static MIOPImplementation miop=new MIOPImplementation();
    public static EDonkeyImplementation edonkey=new EDonkeyImplementation();
    public static UAUDPImplementation uaudp=new UAUDPImplementation(false);
    public static DropboxImplementation dropbox=new DropboxImplementation();
    public static UDP100Implementation udp100=new UDP100Implementation();
    public static RDTImplementation rdt=new RDTImplementation();
//    public static MACTelnetImplementation macTelnet=new MACTelnetImplementation();
    public static DCP_PFTImplementation dcp_pft=new DCP_PFTImplementation();
    public static WireGuardImplementation wireGuard=new WireGuardImplementation();
    public static DNPImplementation dnp=new DNPImplementation();
    public static TCPWithGPRSImplementation tcpWithGprs=new TCPWithGPRSImplementation();
    public static SNDCPImplementation sndcp=new SNDCPImplementation();
    public static IPv4WithGPRSImplementation ipv4WithGprs=new IPv4WithGPRSImplementation();

    
    public static final int UDP100=1000;
    public static final int UFTP=1001;
    public static final int CIGI=1002;
    public static final int NFS=1003;
    public static final int NTP=1004;
    public static final int SNMP=1005;
    public static final int CLDAP=1006;
    public static final int L2TP=1007;
    public static final int BFD=1008;
    public static final int WSP=1009;
    public static final int MOUNT=1010;
    public static final int STAT=1011;
    public static final int ICMPV6=1012;
    public static final int LOWPAN=1013;
    public static final int DSPV2=1014;
    public static final int TEPV1=1015;
    public static final int DPPV2=1016;
    public static final int COAP=1017;
    public static final int TFTP2=1018;
    public static final int IPV6=1019;
    public static final int LTP=1020;
    public static final int XTACACS=1021;
    public static final int ISAKMP=1022;
    public static final int BVLC=1023;
    public static final int MMSE=1024;
    public static final int SLIMP3=1025;
    public static final int AUTORP=1026;
    public static final int MIOP=1027;
    public static final int EDONKEY=1028;
    public static final int UAUDP=1029;
    public static final int DROPBOX=1030;
    public static final int RDT=1031;
    public static final int MAC_TELNET=1032;
    public static final int DCP_PFT=1033;
    public static final int WIREGUARD=1034;
    public static final int DNP=1035;
    public static final int TCP_WITH_GPRS=1036;
    public static final int SNDCP=1037;
    public static final int IPv4_WITH_GPRS=1038;

    
    public static String[] protocolNameListTCP={"TCP 100","NineP2000","COPS","EXEC","BasicTcp","IMAP",
                                            "SMTP","IPA","CQL","BGP","SDNS"
                                              };
    
    public static NineP2000Implementation nineP2000=new NineP2000Implementation(false); //201
    public static COPSImplementation cops=new COPSImplementation(); //202
    public static EXECImplementation exec=new EXECImplementation();//203
    public static BasicTcpImplementation tcp=new BasicTcpImplementation();
    public static IMAPImplementation imap=new IMAPImplementation();
    public static SMTPImplementation smtp=new SMTPImplementation();
    public static IPAImplementation ipa=new IPAImplementation();
    public static CQLImplementation cql=new CQLImplementation();
    public static BGPImplementation bgp=new BGPImplementation();
    public static DNSImplementation sdns=new DNSImplementation();
    
    public static final int NINEP2000=2001;
    public static final int COPS=2002;
    public static final int EXEC=2003;
    public static final int TCP=2004;
    public static final int IMAP=2005;
    public static final int SMTP=2006;
    public static final int IPA=2007;
    public static final int CQL=2008;
    public static final int BGP=2009;
    public static final int SDNS_SSL=2010;
}
