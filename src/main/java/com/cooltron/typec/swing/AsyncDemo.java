package com.cooltron.typec.swing;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



@Slf4j
public class AsyncDemo extends DataMode  {


    Timer timer = null;

    static String[] portNames;

    static volatile boolean isflag = true;

    static HashMap<Integer,String> map = new HashMap<>();

    static {
      portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            map.put(i,portNames[i]);
        }
    }

    public  void openCon() throws SerialPortException, InterruptedException {
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isflag&&conn.getText().equals("CONNECT")){
                    for (int i = 0; i < portNames.length; i++) {
                        try {
                            if(map.get(i).equals(portNames[i])){
                                isCooltron(map.get(i));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        selectedPort="123";
                        timer.cancel();
                    }
                }
            };
        },1000,1000);
    }

    public  void isCooltron(String name) throws SerialPortException, InterruptedException {
        if(name.equals("")||name==null) return;
        SerialPort port = new SerialPort(name);
        port.openPort();
        byte[] newData=new byte[6];
        port.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        byte[] bytes = {(byte)0x80,0,(byte)0x80};
        port.addEventListener(serialPortEvent -> {
            try {
                byte[] resultByte = port.readBytes();
                if(resultByte==null){
                    return;
                }
                System.arraycopy(resultByte,1,newData,0,6);
                String str= new String(newData);
                System.out.println(name+"=====返回数据位====="+str);
                if(!str.equals("") || str!=null){
                    isflag=false;
                    Controller controller = new Controller();
                    controller.changeSelectPort(name);
                    try {
                        port.closePort();
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                    operateConn();
                }
            } catch (SerialPortException  e) {
                e.printStackTrace();
            }
        });
        port.writeBytes(bytes);
        Thread.sleep(5000);
        if(port.isOpened()){
            port.closePort();
        }
    }

    public static String bytes2HexString(byte[] bytes){
        StringBuilder ret = new StringBuilder();
        if (bytes != null) {
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(aByte & 0xFF);
                if (hex.length() == 1) {
                    hex = "0x0" + hex + ",";
                } else {
                    hex = "0x" + hex + ",";
                }
                ret.append(hex);
            }
        }
        return ret.toString();
    }

}
