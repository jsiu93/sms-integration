package cn.chenzw.sms.core.protocol.cmpp.message;

import cn.chenzw.sms.core.protocol.cmpp.util.ByteUtils;
import cn.chenzw.sms.core.protocol.cmpp.util.Md5Utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 连接信息
 * @author chenzw
 */

public final class CMPPConnectMessage extends CMPPBaseMessage {

    /**
     * sp_id
     */
    private String sourceAddr = null;

    /**
     * authenticatorSource
     */
    private byte[] authenticatorSource = null;

    /**
     * password
     */
    private String sharedSecret = null;

    /**
     * version;
     */
    private int version = CMPPConstants.CMPP_VERSION;

    /**
     * timestamp
     */
    private int timestamp = 0;

    public CMPPConnectMessage() {
        super(CMPPConstants.CMPP_CONNECT, 27);
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public byte[] getAuthenticatorSource() {
        return authenticatorSource;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int value) {
        this.version = value;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int value) {
        this.timestamp = value;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }


    public boolean checkSharedSecret(String localSecret) {
        byte[] localAuthenticator = getAuthenticator(sourceAddr, localSecret, timestamp);
        if (Arrays.equals(localAuthenticator, authenticatorSource)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *
     * @return
     */
    @Override
    protected void setBody(byte[] bodyBytes) {
        int off = 0;
        byte abyte0[] = new byte[16];

        // sourceAddr
        sourceAddr = new String(bodyBytes, off, 6);
        off += 6;
        // authenticatorSource
        authenticatorSource = new byte[16];
        ByteUtils.bytesCopy(bodyBytes, authenticatorSource, off, off + 15, 0);
        off += 16;
        // version
        version = bodyBytes[off];
        off += 1;
        //timestamp
        ByteUtils.bytesCopy(bodyBytes, abyte0, off, off + 4 - 1, 0);
        timestamp = ByteUtils.Bytes4ToInt(abyte0);
        off += 4;
    }

    /**
     *
     * @return
     */
    @Override
    protected byte[] getBody() {

        // make bodybytes
        byte[] bodyBytes = new byte[getCommandLength()];
        Arrays.fill(bodyBytes, (byte) 0);

        // make sourceAddr
        if (sourceAddr == null) {
            sourceAddr = "";
        }
        // make timestamp
        if (timestamp == 0) { // need generate
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            timestamp = (calendar.get(Calendar.MONTH) + 1) * 0x5f5e100 + calendar.get(Calendar.DAY_OF_MONTH) * 0xf4240
                    + calendar.get(Calendar.HOUR) * 10000 + calendar.get(Calendar.MINUTE) * 100 + calendar
                    .get(Calendar.SECOND);
        }
        // make authenticatorSource
        authenticatorSource = getAuthenticator(sourceAddr, sharedSecret, timestamp);

        // make body
        int off = 0;
        ByteUtils.bytesCopy(sourceAddr.getBytes(), bodyBytes, 0, 5, off);
        off += 6;
        ByteUtils.bytesCopy(authenticatorSource, bodyBytes, 0, 15, off);
        off += 16;
        bodyBytes[off] = ByteUtils.intToByte(version);
        off += 1;
        ByteUtils.bytesCopy(ByteUtils.intToBytes4(timestamp), bodyBytes, 0, 3, off);
        off += 4;

        return bodyBytes;
    }

    /**
     *
     * @param addr
     * @param secret
     * @param timestamp
     * @return
     */
    private byte[] getAuthenticator(String addr, String secret, int timestamp) {

        String strTimestamp = Integer.toString(timestamp);
        while (strTimestamp.length() < 10) {
            strTimestamp = "0" + strTimestamp;
        }

        byte[] buffer = new byte[6 + 9 + secret.length() + 10];
        Arrays.fill(buffer, (byte) 0);

        int off = 0;
        ByteUtils.bytesCopy(addr.getBytes(), buffer, 0, 5, off);
        off += 6;
        off += 9;
        ByteUtils.bytesCopy(secret.getBytes(), buffer, 0, secret.length() - 1, off);
        off += secret.length();

        ByteUtils.bytesCopy(strTimestamp.getBytes(), buffer, 0, strTimestamp.length() - 1, off);
        off += 10;

        return Md5Utils.md5(buffer);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CMPPConnectMessage:[sequenceId=" + sequenceString() + ",");
        sb.append("sourceAddr=" + sourceAddr + ",");
        sb.append("authenticatorSource=" + authenticatorSource + ",");
        sb.append("version=" + version + ",");
        sb.append("timestamp=" + timestamp + "]");
        return sb.toString();
    }
}
