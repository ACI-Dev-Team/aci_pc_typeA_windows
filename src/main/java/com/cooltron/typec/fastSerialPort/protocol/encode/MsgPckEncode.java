package com.cooltron.typec.fastSerialPort.protocol.encode;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.util.ByteCountData;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.util.ByteUtils;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MsgPckEncode {


	public static void encode(SerialPort ctx, FrameModel frameModel) {
		try {
			byte[] data = frameModel.getData();
			int dataLen = data != null ? data.length : 0;
			byte[] body = new byte[dataLen + 3];
			body[0] = frameModel.getDataType();
			body[1] = (byte) (dataLen & 0xff);
			if (dataLen > 0 && data != null && data.length > 0) {
				System.arraycopy(data, 0, body, 2, dataLen);// data
			}
			byte checkSum = ByteUtils.checksum(body, dataLen + 2);
			body[body.length - 1] = checkSum;
			ctx.writeBytes(body);
			ByteCountData.updateByteCount(ctx.getPortName(), 0, body.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
