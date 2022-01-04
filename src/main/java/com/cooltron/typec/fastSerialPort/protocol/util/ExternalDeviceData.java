package com.cooltron.typec.fastSerialPort.protocol.util;

import com.cooltron.typec.swing.DataMode;
import com.cooltron.typec.swing.bean.ExternalDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

public class ExternalDeviceData extends Observable {
	private static ExternalDeviceData externalDeviceData = new ExternalDeviceData();

	public static void addListen(DataMode dataMode) {
		externalDeviceData.addObserver(dataMode);
	}

	private static Map<String, ExternalDevice> serialport_externaldevice = new ConcurrentHashMap<>();

	public static void updateFee(String port, Integer feed, Integer speed) {
		ExternalDevice externalDevice = serialport_externaldevice.get(port);
		if (externalDevice == null) {
			externalDevice = new ExternalDevice();
		}
		if (feed != null)
			externalDevice.setFeed(feed);
		if (speed != null)
			externalDevice.setSpeed(speed);
		serialport_externaldevice.put(port, externalDevice);
		externalDeviceData.setChanged();
		externalDeviceData.notifyObservers(serialport_externaldevice);
	}

	public static void clearData(String port) {
		serialport_externaldevice.remove(port);
		externalDeviceData.setChanged();
		externalDeviceData.notifyObservers(serialport_externaldevice);
	}

	public static Map<String, ExternalDevice> getData() {
		return serialport_externaldevice;
	}
}
