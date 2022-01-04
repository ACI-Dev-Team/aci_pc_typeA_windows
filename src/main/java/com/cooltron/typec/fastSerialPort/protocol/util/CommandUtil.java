package com.cooltron.typec.fastSerialPort.protocol.util;

import com.cooltron.typec.fastSerialPort.protocol.encode.MsgPckEncode;
import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class CommandUtil {

	public static final int BAUDRATE = 57600;

	public static final int PARITYBIT = 0;

	public static final Map<Integer, String> deviceTypeMap = new HashMap<>();

	public static String getTypeName(int num){
		Integer name=0;
		if(3135 <= num && num <= 3465){
			name=1;
		}
		if(4845 <= num && num <= 5355){
			name=2;
		}
		if(5890 <= num && num <= 6510){
			name=3;
		}
		if(7125 <= num && num <= 7875){
			name=4;
		}
		if(9500 <= num && num <= 10500){
			name=5;
		}
		if(11400 <= num && num <= 12600){
			name=6;
		}
		return deviceTypeMap.get(name);
	}

	static {
		deviceTypeMap.put(0, "--");
		deviceTypeMap.put(1, "Dimmer");
		deviceTypeMap.put(2, "Fan");
		deviceTypeMap.put(3, "Fan");
		deviceTypeMap.put(4, "Fan");
		deviceTypeMap.put(5, "Humidifier");
	}

	private static final byte dataType_devinfo = (byte) 0x80;

	private static final byte dataType_devstatus = (byte) 0x81;

	private static final byte dataType_lineDevFeed = (byte) 0x93;

	private static final byte dataType_controlFeed = (byte) 0x92;

	public static final byte dataType_devinfo_ask = (byte) 0x40;

	public static final byte dataType_devstatus_ask = (byte) 0x41;

	public static final byte dataType_lineDevFeed_ask = (byte) 0x33;

	public static final byte dataType_controlFeed_ask = (byte) 0x32;

	public static final String dataType_notification_prefix = "A";

	public static final byte dataType_controlFeed_ask11 = (byte) 0xA1;

	public static Map<String, SerialPort> online_ports = new LinkedHashMap<>();

	public static void sendDeviceInfo(SerialPort ctx) {
		sendFrameModel(ctx, dataType_devinfo, null);
	}

	public static void sendDeviceStatus(SerialPort ctx) {
		sendFrameModel(ctx, dataType_devstatus, null);
	}

	public static void sendLineDeviceFeed(SerialPort ctx) {
		sendFrameModel(ctx, dataType_lineDevFeed, null);
	}

	public static void sendControlFeed(SerialPort ctx, int feed) {
		sendFrameModel(ctx, dataType_controlFeed, new byte[] { (byte) feed });
	}

	private static void sendFrameModel(SerialPort ctx, byte dataType, byte[] data) {
		FrameModel frameModel = new FrameModel(dataType, data);
		if (ctx != null && ctx.isOpened()) {
			MsgPckEncode.encode(ctx, frameModel);
		}
	}
}
