package com.cooltron.typec.fastSerialPort.protocol.decode;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.util.ByteCountData;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.util.ByteUtils;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MsgPckDecode {

	public static FrameModel checkLenAndReadData(SerialPort ctx) throws SerialPortException {
		byte[] bytes = ctx.readBytes();
		if (bytes == null) {
			return null;
		}
		ByteCountData.updateByteCount(ctx.getPortName(), bytes.length, 0);
		int totalLen = bytes.length;
		if (totalLen < 3) {
			return null;
		}
		short dataLen = bytes[1];
		if (dataLen + 3 > totalLen) {
			return null;
		}
		int checkSum = bytes[2 + dataLen];
		if (checkSum != ByteUtils.checksum(bytes, dataLen + 2)) {
			return null;
		}
		FrameModel fm = new FrameModel();
		fm.setDataType(bytes[0]);
		fm.setDataLen(bytes[1]);
		byte[] data = new byte[dataLen];
		System.arraycopy(bytes, 2, data, 0, dataLen);// data
		fm.setData(data);
		return fm;
	}

}
