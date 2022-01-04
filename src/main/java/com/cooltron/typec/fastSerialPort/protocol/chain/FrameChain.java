package com.cooltron.typec.fastSerialPort.protocol.chain;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import jssc.SerialPort;

public interface FrameChain {
    boolean handler(SerialPort channel, FrameModel frameModel, String port);
}
