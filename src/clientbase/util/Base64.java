package clientbase.util;

public class Base64 {

    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-/=";
    static int mod_table[] = {0, 2, 1};

    public static int base64Decode(byte[] data, int offset, int datalen) {


        String input = new String(data, offset, datalen);

        //System.out.println("input data for decoding: " + input);
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        //byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        byte [] decoded = new byte[2048];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        //   System.out.println("decoded data: " + new String(decoded, 0, decoded.length));
        System.arraycopy(decoded, 0, data, offset, j);

        return j;
    }

    public static int base64Encode(byte[] data, int offset, int len) {

        StringBuilder out = new StringBuilder((len * 4) / 3);
        int b;
        for (int i = 0; i < len; i += 3) {
            b = (data[i + offset] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (data[i + offset] & 0x03) << 4;
            if (i + 1 < len) {
                b |= (data[i + offset + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (data[i + offset + 1] & 0x0F) << 2;
                if (i + 2 < len) {
                    b |= (data[i + offset + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = data[i + offset + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }
        byte[] output = String.valueOf(out).getBytes();

        System.arraycopy(output, 0, data, offset, output.length);

        return output.length;
    }

    public static int base64Encode(byte[] data, int inoffset, int outoffset, int inlen) {

        StringBuilder out = new StringBuilder((inlen * 4) / 3);
        int b;
        for (int i = 0; i < inlen; i += 3) {
            b = (data[i + inoffset] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (data[i + inoffset] & 0x03) << 4;
            if (i + 1 < inlen) {
                b |= (data[i + inoffset + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (data[i + inoffset + 1] & 0x0F) << 2;
                if (i + 2 < inlen) {
                    b |= (data[i + inoffset + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = data[i + inoffset + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }
        byte[] output = String.valueOf(out).getBytes();

        System.arraycopy(output, 0, data, outoffset, output.length);

        return output.length;
    }

    public static byte [] encode(byte[] in) {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3) {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length) {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length) {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }

        return out.toString().getBytes();
    }

    public static int base64EncodeMedia(byte[] data, int offset, int input_length) {

        StringBuilder out = new StringBuilder(((input_length+2) * 4) / 3);
        int b,j=0;
        for (int i = 0; i < input_length; i ++) {
            byte octet_a = i < input_length ? (byte)data[i++] : 0;
            byte octet_b = i < input_length ? (byte)data[i++] : 0;
            byte octet_c = i < input_length ? (byte)data[i++] : 0;

            int triple = (octet_a << 0x10) + (octet_b << 0x08) + octet_c;

            out.append(CODES.charAt((triple >> 18) & 0x3F));
            out.append(CODES.charAt((triple >> 12) & 0x3F));
            out.append(CODES.charAt((triple >> 6) & 0x3F));
            out.append(CODES.charAt(triple  & 0x3F));

        }
        for (int i = 0; i < mod_table[input_length % 3]; i++)
            out.append('=');

        byte[] output = String.valueOf(out).getBytes();

        System.arraycopy(output, 0, data, offset, output.length);

        return output.length;
    }




    //

    public static int taha(byte[] data, int offset, int datalen) {
        String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        //  logger.debug("Got offset:"+offset+" datalen:"+datalen);

        String input = new String(data, offset, datalen);

        //System.out.println("input data for decoding: " + input);
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf("=") > 0 ? (input.length() - input.indexOf("=")) : 0)];
        char[] inChars = input.toCharArray();
        //  logger.debug("decoded.len:"+decoded.length+" inChars.len:"+inChars.length +" input.len: "+input.length() + " input.indexOf(\"=\"): "+input.indexOf("="));
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        //   System.out.println("decoded data: " + new String(decoded, 0, decoded.length));
        System.arraycopy(decoded, 0, data, offset, decoded.length);

        return decoded.length;
    }

    public static String encodeToString(byte[] in) {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3) {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length) {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length) {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }

        return out.toString();
    }
    public static byte[] decodeToByteArray(String input) {
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return decoded;
    }



   /* public static byte[] base64Decode(String input) {
        if (input.length() % 4 != 0) {
            throw new IllegalArgumentException("Invalid base64 input");
        }
        byte decoded[] = new byte[((input.length() * 3) / 4) - (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
        char[] inChars = input.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4) {
            // This could be made faster (but more complicated) by precomputing these index locations.
            b[0] = CODES.indexOf(inChars[i]);
            b[1] = CODES.indexOf(inChars[i + 1]);
            b[2] = CODES.indexOf(inChars[i + 2]);
            b[3] = CODES.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64) {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64) {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                }
            }
        }

        return decoded;
    }*/

}
