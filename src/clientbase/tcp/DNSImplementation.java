package clientbase.tcp;

import clientbase.util.Base64;
import clientbase.util.Functions;

import java.util.ArrayList;

public class DNSImplementation {
    private static final int CNAME = 1;
    private static final int MX = 2;
    private static final int TXT = 3;
    private static byte [] CNAMEfooter = {0x00, 0x00, 0x05, 0x00, 0x01};
    private static byte [] MXfooter = {0x00, 0x00, 0x0f, 0x00, 0x01};
    private static byte [] TXTfooter = {0x00, 0x00, 0x10, 0x00, 0x01};

    /**
     * Take input and prepare a CNAME record
     * @param inputData byte array that represents data to be sent
     * @param inputOffset input byte array offset
     * @param inputLen input byte array length
     * @param output output byte array. CNAME formatted byte array
     * @param outputOffset output byte array offset
     * @param dnsDomain subdomain.domain.com that should be used to create the dns request
     * @return int the length of the output byte array
     */
    public static int prepareCNAMERecord(byte [] inputData, int inputOffset, int inputLen, byte [] output, int outputOffset, String dnsDomain){
        return prepareDNSRequest(inputData, inputOffset, inputLen, output, outputOffset, dnsDomain, CNAME);
    }

    /**
     * Take input and prepare a MX record
     * @param inputData byte array that represents data to be sent
     * @param inputOffset input byte array offset
     * @param inputLen input byte array length
     * @param output output byte array. MX formatted byte array
     * @param outputOffset output byte array offset
     * @param dnsDomain subdomain.domain.com that should be used to create the dns request
     * @return int the length of the output byte array
     */
    public static int prepareMXRecord(byte [] inputData, int inputOffset, int inputLen, byte [] output, int outputOffset, String dnsDomain) {
        return prepareDNSRequest(inputData, inputOffset, inputLen, output, outputOffset, dnsDomain, MX);
    }

    /**
     * Take input and prepare a TXT record
     * @param inputData byte array that represents data to be sent
     * @param inputOffset input byte array offset
     * @param inputLen input byte array length
     * @param output output byte array. TXT formatted byte array
     * @param outputOffset output byte array offset
     * @param dnsDomain subdomain.domain.com that should be used to create the dns request
     * @return int the length of the output byte array
     */
    public static int prepareTXTRecord(byte [] inputData, int inputOffset, int inputLen, byte [] output, int outputOffset, String dnsDomain) {
        return prepareDNSRequest(inputData, inputOffset, inputLen, output, outputOffset, dnsDomain, TXT);
    }

    public static int prepareDNSRequest(byte [] inputData, int inputOffset, int inputLen, byte [] output, int outputOffset, String dnsDomain, int type){
        int remainingData = Base64.base64Encode(inputData, inputOffset, inputLen);


        int index = outputOffset, addedDataLen = 0;
        ////transaction id
        Functions.getRandomData(output, index, 2);
        index += 2;

        ////flags 0x0100
        output[index++] = 0x01;
        output[index++] = 0x00;

        ////questions count 0x0001
        output[index++] = 0x00;
        output[index++] = 0x01;

        /////answers 0x0000 authorities 0x0000 additional rrs 0x0000
        for(int i=0; i<6; i++)
            output[index++] = 0x00;

        ///////add data and . where necessary
        while(remainingData > 0){
            int currentLen = Math.min(remainingData, 63);
            remainingData -= currentLen;
            output[index++] = (byte) currentLen;
            for(int i=0; i<currentLen; i++)
                output[index++] = inputData[inputOffset + addedDataLen++];
        }

        /////add the subdomain.domain.com
        if(dnsDomain.startsWith("."))
            dnsDomain = dnsDomain.substring(1); /////remove the first .

        String [] parts = dnsDomain.split("\\.");
        for(String part: parts){
            int len = part.length();
            if(len > 0){
                output[index++] = (byte)len;
                System.arraycopy(part.getBytes(), 0, output, index, len);
                index += len;
            }
        }

        ////add the type specific end signature
        byte [] footer = null;
        switch(type){
            case MX:
                footer = MXfooter;
                break;
            case CNAME:
                footer = CNAMEfooter;
                break;
            case TXT:
            default:
                footer = TXTfooter;
                break;
        }
        for(int i=0; i<footer.length; i++){
            output[index++] = footer[i];
        }

        return index - outputOffset;
    }

    /**
     * Parse the dns response coming form server
     * @param data, byte array containing the whole dns packet
     * @param offset, offset of input byte array
     * @param len, length of the data
     * @return the length of the parsed data
     */
    public static int parseDNSResponse(byte [] data, int offset, int len){
        ////first 12 bytes are headers need to be ignored
        int index = offset + 12;

        ////get the indices of the quesiton parts
        ArrayList<Integer> indexList = decodeIndices(data, index, len - 12);
        index = indexList.get(indexList.size() - 1); ////last element is the index i have parsed
        indexList.remove(indexList.size() - 1); ///remove the last element

        ////there is 4 bytes extra at the end of the quesiton parts
        ////which defines the TYPE and CLASS
        index += 4;

        ////left shift the array to remove all the question parts
        int iterationCount = offset + len - index;
        for(int i=0;i<iterationCount;i++){
            data[offset + i] = data[index + i];
        }

        ////Now the data array is CNAME/MX/TXT HEADER + DATA
        ////reduce the len
        len = len - (index - offset);
//        System.out.println(Functions.bytesToHex(data, offset, len));

        switch (data[offset + 3]) {
            /////found CNAME response
            case 0x05:
                return parseCNAMEAnswerData(data, offset, len);
            /////found MX resonse
            case 0x0f:
                return parseMXAnswerData(data, offset, len);
            /////found TXT response
            case 0x10:
                return parseTXTAnswerData(data, offset, len);
        }

        return 0;
    }

    /**
     *
     * Parse the answer to retrieve actual data from server
     * @param data, byte array that contains only ans part of the DNS response
     * @param offset, offset of the input byte array
     * @param len, length of the ans part
     * @return the length of the parsed data.
     */
    private static int parseCNAMEAnswerData(byte [] data, int offset, int len){
        int index = offset + 12, currentLen, outputIndex = offset;
        /////here first 12 bytes are header. after that indices of length starts.
        ArrayList<Integer> indexList = decodeIndices(data, index, len - 12);
        for(int i=0; i<indexList.size() - 1; i++){ /////last index is the index till it has parsed. so it need to be ignored
            currentLen = indexList.get(i);
            index++; ////length byte
            for(int j=0; j<currentLen; j++){
                data[outputIndex++] = data[index++];
            }
        }
        return outputIndex - offset;
    }
    /**
     *
     * Parse the answer to retrieve actual data from server
     * @param data, byte array that contains only ans part of the DNS response
     * @param offset, offset of the input byte array
     * @param len, length of the ans part
     * @return the length of the parsed data.
     */
    private static int parseMXAnswerData(byte [] data, int offset, int len){
        int index = offset + 14, currentLen, outputIndex = offset;
        /////here first 12 bytes are header. after that indices of length starts.
        ArrayList<Integer> indexList = decodeIndices(data, index, len - 14);
        for(int i=0; i<indexList.size() - 1; i++){ /////last index is the index till it has parsed. so it need to be ignored
            currentLen = indexList.get(i);
            index++; ////length byte
            for(int j=0; j<currentLen; j++){
                data[outputIndex++] = data[index++];
            }
        }
        return outputIndex - offset;
    }

    /**
     *
     * Parse the answer to retrieve actual data from server
     * @param data, byte array that contains only ans part of the DNS response
     * @param offset, offset of the input byte array
     * @param len, length of the ans part
     * @return the length of the parsed data.
     */
    private static int parseTXTAnswerData(byte [] data, int offset, int len){
        /////actually txt response are comprised of multple response similar to CNAME record
        int index = offset, currentLen, outputIndex = offset;
        while(index < offset + len){
            index += 12;  ///// 12 byte txt header (10 header + 2 len )

            currentLen = data[index++] & 0xff;

            for(int j=0; j<currentLen; j++){
                data[outputIndex++] = data[index++];
            }
        }

        ////txt response are base64 encoded. need to decode them.
        len = outputIndex - offset;
        len = Base64.base64Decode(data, offset, len);

        return len;
    }

    /**
     * Get the arraylist of the indices, where lenghts are located.
     * */
    private static ArrayList<Integer> decodeIndices(byte [] data, int index, int length){
        ArrayList<Integer> indexList = new ArrayList<>();
        int currentDataLen;
        int endIndex = index + length;
        while (index < endIndex) {
            currentDataLen = (int) data[index++];
            if (currentDataLen <= 0) {
                break;
            }
            indexList.add(currentDataLen); /////add the parts length
            index += currentDataLen;
        }
        indexList.add(index); /////add the last index of the parts

        return indexList;
    }

}