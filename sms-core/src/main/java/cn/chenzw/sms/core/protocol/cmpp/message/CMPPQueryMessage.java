package cn.chenzw.sms.core.protocol.cmpp.message;

import cn.chenzw.sms.core.protocol.cmpp.util.ByteUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 查询信息
 * @author chenzw
 */

public class CMPPQueryMessage extends CMPPBaseMessage {

    private String time = null;
    private int queryType = 0;
    private String queryCode = null;
    private String reserve = null;

    public CMPPQueryMessage() {
        super(CMPPConstants.CMPP_QUERY, 27);
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     */
    @Override
    protected void setBody(byte[] bodyBytes) {

        byte[] abyte0 = new byte[21];
        int off = 0;

        Arrays.fill(abyte0, (byte) 0);
        ByteUtils.bytesCopy(bodyBytes, abyte0, off, off + 7, 0);
        time = new String(abyte0, 0, 8);
        off += 8;

        Arrays.fill(abyte0, (byte) 0);
        ByteUtils.bytesCopy(bodyBytes, abyte0, off, off, 0);
        queryType = ByteUtils.byteToInt(abyte0[0]);
        off += 1;

        Arrays.fill(abyte0, (byte) 0);
        ByteUtils.bytesCopy(bodyBytes, abyte0, off, off + 9, 0);
        queryCode = new String(abyte0, 0, 10);
        off += 10;

        Arrays.fill(abyte0, (byte) 0);
        ByteUtils.bytesCopy(bodyBytes, abyte0, off, off + 7, 0);
        reserve = new String(abyte0, 0, 8);
        off += 8;
    }

    /**
     *
     */
    @Override
    protected byte[] getBody() {

        // make bodybytes
        int bodyLength = getCommandLength();
        byte[] bodyBytes = new byte[bodyLength];
        Arrays.fill(bodyBytes, (byte) 0);

        // make parameter
        if (time == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int timestamp = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar
                    .get(Calendar.DAY_OF_MONTH);
            time = String.valueOf(timestamp);
        }
        if (queryCode == null) {
            queryCode = "";
        }
        if (reserve == null) {
            reserve = "";
        }

        // make body
        int off = 0;
        ByteUtils.bytesCopy(time.getBytes(), bodyBytes, 0, 7, off);
        off += 8;
        bodyBytes[off] = ByteUtils.intToByte(queryType);
        off += 1;
        ByteUtils.bytesCopy(queryCode.getBytes(), bodyBytes, 0, 9, off);
        off += 10;
        ByteUtils.bytesCopy(reserve.getBytes(), bodyBytes, 0, 7, off);
        off += 8;

        return bodyBytes;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CMPPQueryMessage:[sequenceId=" + sequenceString() + ",");
        sb.append("time=" + time + ",");
        sb.append("queryType=" + queryType + ",");
        sb.append("queryCode=" + queryCode + ",");
        sb.append("reserve=" + reserve + "]");

        return sb.toString();
    }
}