package com.cooltron.typec.fastSerialPort;

import org.springframework.util.StringUtils;

import com.cooltron.typec.fastSerialPort.protocol.decode.MsgPckDecode;
import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.processor.FrameProcessor;
import com.cooltron.typec.fastSerialPort.protocol.processor.RequestProcessor;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;

import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerialClient {

	private RequestProcessor firstRequestProcessor;

	public SerialClient() {
		firstRequestProcessor = new FrameProcessor();
	}

	public boolean connect(String com) {
		if (StringUtils.isEmpty(com)) {
			return false;
		}
		if (CommandUtil.online_ports.containsKey(com)) {
			return true;
		}
		try {
			SerialPort serialPort = new SerialPort(com);
			serialPort.openPort();
			serialPort.setParams(CommandUtil.BAUDRATE, 8, 1, CommandUtil.PARITYBIT);
			serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
			serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			if (serialPort.isOpened()) {
				CommandUtil.online_ports.put(com, serialPort);
				serialPort.addEventListener(serialPortEvent -> {
					try {
						FrameModel frameModel = MsgPckDecode.checkLenAndReadData(serialPort);
						if (frameModel != null) {
							firstRequestProcessor.processRequest(serialPort, frameModel);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				return false;
			}
			CommandUtil.sendDeviceInfo(serialPort);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean closeConn(String com) {
		SerialPort serialPort = CommandUtil.online_ports.remove(com);
		try {
			if (serialPort != null && serialPort.isOpened()) {
				serialPort.closePort();
				serialPort = null;
			}
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

}
