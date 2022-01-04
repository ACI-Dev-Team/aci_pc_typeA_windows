package com.cooltron.typec.fastSerialPort.protocol.util;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import com.cooltron.typec.swing.DataMode;
import com.cooltron.typec.swing.bean.Device;

public class DeviceData extends Observable {

	private static DeviceData deviceData = new DeviceData();

	public static void addListen(DataMode dataMode) {
		deviceData.addObserver(dataMode);
	}

	private static Map<String, Device> serialPort_deviceInfo = new ConcurrentHashMap<>();

	public static boolean hadDevice(String port) {
		return serialPort_deviceInfo.containsKey(port);
	}

	public static void updateDevice(String port, String serial, String version) {
		Device device = serialPort_deviceInfo.get(port);
		if (device == null) {
			device = new Device();
		}
		device.setSerial(serial);
		device.setVersion(version);
		serialPort_deviceInfo.put(port, device);
		deviceData.setChanged();
		deviceData.notifyObservers(serialPort_deviceInfo);
	}

	public static void updateDeviceType(String port, String typeName) {
		Device device = serialPort_deviceInfo.get(port);
		if (device != null) {
			device.setDeviceType(typeName);
			serialPort_deviceInfo.put(port, device);
			deviceData.setChanged();
			deviceData.notifyObservers(serialPort_deviceInfo);
		}
	}

	public static void clearData(String port) {
		serialPort_deviceInfo.remove(port);
		deviceData.setChanged();
		deviceData.notifyObservers(serialPort_deviceInfo);
	}



	public static Map<String, Device> getData() {
		return serialPort_deviceInfo;
	}

}
