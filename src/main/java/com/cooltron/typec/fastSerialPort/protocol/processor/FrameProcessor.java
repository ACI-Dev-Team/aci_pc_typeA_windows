package com.cooltron.typec.fastSerialPort.protocol.processor;

import com.cooltron.typec.fastSerialPort.protocol.chain.DevFeedChain;
import com.cooltron.typec.fastSerialPort.protocol.chain.DevInfoChain;
import com.cooltron.typec.fastSerialPort.protocol.chain.FrameChain;
import com.cooltron.typec.fastSerialPort.protocol.chain.NotificationChain;
import com.cooltron.typec.fastSerialPort.protocol.entity.FrameModel;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class FrameProcessor implements RequestProcessor {
    public FrameProcessor() {
        initChain();
    }
    private List<FrameChain> frameChains = new ArrayList();
    private void initChain() {
        frameChains.add(new DevInfoChain());
        frameChains.add(new DevFeedChain());
        frameChains.add(new NotificationChain());
    }

    @Override
    public boolean processRequest(SerialPort channel, Object msg) {
        String port = (channel!=null? channel.getPortName():null);
        if(port==null){
            return true;
        }
        if (msg instanceof FrameModel) {
            FrameModel frameModel = (FrameModel) msg;
            for (FrameChain frameChain : frameChains) {
                if (frameChain.handler(channel, frameModel, port)) {
                    return true;
                }
            }
        }
        return false;
    }
}
