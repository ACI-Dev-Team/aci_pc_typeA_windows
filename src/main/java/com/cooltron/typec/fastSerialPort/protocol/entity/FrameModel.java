package com.cooltron.typec.fastSerialPort.protocol.entity;

import com.cooltron.typec.util.ByteUtils;
import lombok.Data;

@Data
public class FrameModel {
    private byte dataType;
    private byte dataLen;
    private byte[] data;
    private byte checkSum;

    public FrameModel() {
    }

    public FrameModel(byte dataType, byte[] data) {
        this.dataType = dataType;
        this.data = data;
    }

    @Override
    public String toString() {
        return "FrameModel{" +
                "dataType=" + dataType +
                ", data=" + ByteUtils.toHexStrWithSpace(data) +
                '}';
    }
}
