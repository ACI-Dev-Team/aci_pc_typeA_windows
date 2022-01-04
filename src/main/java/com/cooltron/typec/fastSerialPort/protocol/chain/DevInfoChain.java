package com.cooltron.typec.fastSerialPort.protocol.chain;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.fastSerialPort.protocol.util.DeviceData;
import com.cooltron.typec.fastSerialPort.protocol.util.ExternalDeviceData;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;


@Slf4j
public class DevInfoChain implements FrameChain {

	@Override
	public boolean handler(SerialPort channel, FrameModel frameModel, String port) {
		byte dataType = frameModel.getDataType();
		byte[] data = frameModel.getData();
		switch (dataType) {
		case CommandUtil.dataType_devinfo_ask:
			String serial = new String(data, 0, 6, StandardCharsets.US_ASCII);
			String version = data[6] + "." + data[7] + "-" + data[8] + "." + data[9];
			DeviceData.updateDevice(port, serial, version);
			CommandUtil.sendDeviceStatus(channel);
			break;
		case CommandUtil.dataType_devstatus_ask:
			int deviceType =data[0]*256+(data[1] & 0xff);
			String typeName = CommandUtil.getTypeName(deviceType);
			DeviceData.updateDeviceType(port, typeName);
			if (typeName != null && !typeName.equals("--")) {
				CommandUtil.sendLineDeviceFeed(channel);
			}
			break;
		case CommandUtil.dataType_controlFeed_ask11:
			int i = data[0] & 0xff;
			int devType =data[1]*256+(data[2] & 0xff);
			String typeName1 = CommandUtil.getTypeName(devType);
			DeviceData.updateDeviceType(port, typeName1);
			if(devType < 0){
				ExternalDeviceData.updateFee(port, 0, 0);
				ExternalDeviceData.clearData(port);
				break;
			}
			CommandUtil.sendLineDeviceFeed(channel);
			break;
		default:
			return false;
		}
		return true;
	}
}
