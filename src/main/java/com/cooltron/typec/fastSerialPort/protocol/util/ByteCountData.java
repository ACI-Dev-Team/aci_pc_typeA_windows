package com.cooltron.typec.fastSerialPort.protocol.util;

import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import com.cooltron.typec.swing.DataMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteCountData extends Observable {

	private static ByteCountData byteCountData = new ByteCountData();

	public static void addListen(DataMode dataMode) {
		byteCountData.addObserver(dataMode);
	}

	private static Map<String, String> serialPort_count = new ConcurrentHashMap<>();

	public static void updateByteCount(String port, int addReadCount, int addWriteCount) {
		if (port == null) {
			return;
		}
		String count = serialPort_count.get(port);
		if (count == null) {
			count = "0#0";
		}
		int read = Integer.parseInt(count.split("#")[0]) + addReadCount;
		int write = Integer.parseInt(count.split("#")[1]) + addWriteCount;
		count = read + "#" + write;
		serialPort_count.put(port, count);
		byteCountData.setChanged();
		byteCountData.notifyObservers(serialPort_count);
	}

	public static void clearData(String port) {
		serialPort_count.remove(port);
		byteCountData.setChanged();
		byteCountData.notifyObservers(serialPort_count);
	}

	public static Map<String, String> getData() {
		return serialPort_count;
	}

}
