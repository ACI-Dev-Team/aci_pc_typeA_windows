package com.cooltron.typec.fastSerialPort.protocol.util;

import com.cooltron.typec.swing.DataMode;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;

import java.util.Observable;

@Slf4j
public class BusinessData extends Observable {
	private static BusinessData businessData = new BusinessData();

	public static void addListen(DataMode dataMode) {
		businessData.addObserver(dataMode);
	}



	public static void clearCacheWiteLineout(String port) {
		clearPort(port);
		businessData.setChanged();
		businessData.notifyObservers(CommandUtil.online_ports);
	}


	private static void clearPort(String port) {
		if (port != null) {
			DeviceData.clearData(port);
			ExternalDeviceData.clearData(port);
			ByteCountData.clearData(port);
			CommandUtil.online_ports.remove(port);
		}
	}
}
