package cn.chenzw.sms.core.protocol.cmpp.util;

import java.io.UnsupportedEncodingException;

public class ByteUtils {

    /**
     * @param i
     * @return
     */
    public static byte intToByte(int i) {
        return (byte) i;
    }

    /**
     *
     * @param i
     * @return
     */
    public static byte[] intToBytes(int i) {
        byte abyte0[] = new byte[2];
        abyte0[1] = (byte) (0xff & i);
        abyte0[0] = (byte) ((0xff00 & i) >> 8);
        return abyte0;
    }

    /**
     *
     * @param i
     * @param abyte0
     */
    public static void intToBytes(int i, byte abyte0[]) {
        abyte0[1] = (byte) (0xff & i);
        abyte0[0] = (byte) ((0xff00 & i) >> 8);
    }

    /**
     *
     * @param i
     * @return
     */
    public static byte[] intToBytes4(int i) {
        byte abyte0[] = new byte[4];
        abyte0[3] = (byte) (0xff & i);
        abyte0[2] = (byte) ((0xff00 & i) >> 8);
        abyte0[1] = (byte) ((0xff0000 & i) >> 16);
        abyte0[0] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }

    /**
     *
     * @param i
     * @param abyte0
     */
    public static void intToBytes4(int i, byte abyte0[]) {
        abyte0[3] = (byte) (0xff & i);
        abyte0[2] = (byte) ((0xff00 & i) >> 8);
        abyte0[1] = (byte) ((0xff0000 & i) >> 16);
        abyte0[0] = (byte) (int) ((0xffffffffff000000L & (long) i) >> 24);
    }

    /**
     *
     * @param i
     * @return
     */
    public static byte[] longToBytes8(long i) {
        byte abyte0[] = new byte[8];
        abyte0[7] = (byte) (0xffL & i);
        abyte0[6] = (byte) ((0xff00L & i) >> 8);
        abyte0[5] = (byte) ((0xff0000L & i) >> 16);
        abyte0[4] = (byte) ((0xff000000L & i) >> 24);
        abyte0[3] = (byte) ((0xff00000000L & i) >> 32);
        abyte0[2] = (byte) ((0xff0000000000L & i) >> 40);
        abyte0[1] = (byte) ((0xff000000000000L & i) >> 48);
        abyte0[0] = (byte) ((0xff00000000000000L & i) >> 56);
        return abyte0;
    }

    /**
     * byte to integer
     * @param byte0
     * @return
     */
    public static int byteToInt(byte byte0) {
        return byte0;
    }

    /**
     * byte to integer
     * @param abyte0
     * @return
     */
    public static int BytesToInt(byte abyte0[]) {
        return ((0xff & abyte0[0]) << 8) + abyte0[1];
    }

    /**
     * 4 bytes to integer
     * @param abyte0
     * @return
     */
    public static int Bytes4ToInt(byte abyte0[]) {
        return (0xff & abyte0[0]) << 24 | (0xff & abyte0[1]) << 16 | (0xff & abyte0[2]) << 8 | 0xff & abyte0[3];
    }

    /**
     * 8 bytes to long
     * @param abyte0
     * @return
     */
    public static long Bytes8ToLong(byte abyte0[]) {
        long ret = 0;

        ret = (0xffL & abyte0[0]) << 56 | (0xffL & abyte0[1]) << 48 | (0xffL & abyte0[2]) << 40
                | (0xffL & abyte0[3]) << 32 | (0xffL & abyte0[4]) << 24 | (0xffL & abyte0[5]) << 16
                | (0xffL & abyte0[6]) << 8 | 0xffL & abyte0[7];

        return ret;
    }

    /**
     *
     * @param srcAbyte
     * @param destAbyte
     * @param srcFrom: srcindex
     * @param srcTo
     * @param destFrom
     */
    public static void bytesCopy(byte srcAbyte[], byte destAbyte[], int srcFrom, int srcTo, int destFrom) {
        // check null
        if (srcAbyte == null || destAbyte == null) {
            return;
        }
        // copy
        int i1 = 0;
        for (int l = srcFrom; l <= srcTo; l++) {
            if (destFrom + i1 >= destAbyte.length) {
                break;
            }
            if (l >= srcAbyte.length) {
                break;
            }
            destAbyte[destFrom + i1] = srcAbyte[l];
            i1++;
        }
    }

    /**
     * 字节数组转换成16进制可打印字符串
     * @param b
     * @param charset
     * @return
     */
    public static String byteToHexString(byte[] b, String charset) {
        String str = null;

        if (b == null) {
            return null;
        }
        if (charset != null) {
            try {
                str = new String(b, charset);
            } catch (UnsupportedEncodingException e1) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < b.length; i++) {        // con't change, change to binary
                    char chHi = Character.forDigit((b[i] & 0xF0) >> 4, 16);
                    char chLow = Character.forDigit((b[i] & 0x0F), 16);
                    sb.append(chHi);
                    sb.append(chLow);
                    sb.append(' ');
                }
                str = sb.toString().toUpperCase();
            }
        } else {
            str = new String(b);
        }
        return str;
    }

    /**
     * to protableString
     * @param b
     * @return
     */
    public static String toPrintableString(byte[] b) {

        if (b == null)
            return null;

        StringBuffer sb = new StringBuffer();
        byte[] t = new byte[1];
        for (int i = 0; i < b.length; i++) {        // con't change, change to binary
            if (b[i] >= 0x20 && b[i] <= 0x7e) {  // printable char
                t[0] = b[i];
                sb.append(new String(t));
            } else {                            //non-pritable char
                sb.append(".");
            }
        }
        return sb.toString();
    }

}
