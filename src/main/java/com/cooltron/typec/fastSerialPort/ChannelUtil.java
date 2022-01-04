package com.cooltron.typec.fastSerialPort;

import com.cooltron.typec.fastSerialPort.protocol.util.BusinessData;
import jssc.SerialPortList;

import java.util.Arrays;
import java.util.List;

public class ChannelUtil {


	public static List<String> findComPort() {
		String[] portsArr = SerialPortList.getPortNames();
		return Arrays.asList(portsArr);
	}


	public static boolean checkComPort(String comm) {
		String[] portsArr = SerialPortList.getPortNames();
		for (String port : portsArr) {
			if (port.equals(comm)) {
				return true;
			}
		}
		BusinessData.clearCacheWiteLineout(comm);
		return false;
	}

}
