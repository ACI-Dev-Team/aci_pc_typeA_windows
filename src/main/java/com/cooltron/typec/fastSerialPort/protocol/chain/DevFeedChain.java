package com.cooltron.typec.fastSerialPort.protocol.chain;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.fastSerialPort.protocol.util.ExternalDeviceData;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DevFeedChain implements FrameChain {

	@Override
	public boolean handler(SerialPort channel, FrameModel frameModel, String port) {
		byte dataType = frameModel.getDataType();
		byte[] data = frameModel.getData();
		switch (dataType) {
		case CommandUtil.dataType_lineDevFeed_ask:
			int feed = -1;
			int speed = -1;
			byte feedByte = data[0];
			if (feedByte != 0) {
				feed = data[1];
				speed = (0xff00 & data[3] << 8 | (0xff & data[2]));
			}
			ExternalDeviceData.updateFee(port, feed, speed);
			break;
		case CommandUtil.dataType_controlFeed_ask:
			if (data[0] == 0) {
			} else {
			}
			CommandUtil.sendLineDeviceFeed(channel);
			break;
		default:
			return false;
		}
		return true;
	}

}
