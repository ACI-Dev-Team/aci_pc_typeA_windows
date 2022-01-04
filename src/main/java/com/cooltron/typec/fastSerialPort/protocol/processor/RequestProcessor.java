package com.cooltron.typec.fastSerialPort.protocol.processor;

import jssc.SerialPort;

public interface RequestProcessor {
    boolean processRequest(SerialPort channel, Object msg);
}
