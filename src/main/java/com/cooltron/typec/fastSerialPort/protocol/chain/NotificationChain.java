package com.cooltron.typec.fastSerialPort.protocol.chain;

import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.fastSerialPort.protocol.util.DeviceData;
import com.cooltron.typec.util.ByteUtils;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class NotificationChain implements FrameChain {

    @Override
    public boolean handler(SerialPort channel, FrameModel frameModel, String port) {
        if (!ByteUtils.toHexStr(frameModel.getDataType()).startsWith(CommandUtil.dataType_notification_prefix)) {
            return false;
        }
        if(ByteUtils.toHexStr(frameModel.getDataType()).equals("A5")){
        }
        if(DeviceData.hadDevice(port)){
            CommandUtil.sendDeviceStatus(channel);
        }
        return true;
    }


}
