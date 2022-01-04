package com.cooltron.typec.swing;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.*;
import com.cooltron.typec.fastSerialPort.ChannelUtil;
import com.cooltron.typec.fastSerialPort.SerialClient;
import com.cooltron.typec.fastSerialPort.protocol.util.BusinessData;
import com.cooltron.typec.fastSerialPort.protocol.util.ByteCountData;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.fastSerialPort.protocol.util.DeviceData;
import com.cooltron.typec.fastSerialPort.protocol.util.ExternalDeviceData;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Controller extends DataMode implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ExternalDeviceData.addListen(this);
		DeviceData.addListen(this);
		BusinessData.addListen(this);
		ByteCountData.addListen(this);
		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (listView.getSelectionModel().getSelectedItem() == null)
					return;
				String name = listView.getSelectionModel().getSelectedItem().toString();
				changeSelectPort(name);
			}
		});
		circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				double sceneX = e.getSceneX();
				double sceneY = e.getSceneY();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						sendFeedByPosition(sceneX, sceneY);
					}
				});
			}
		});
		scanPortTimer();
	}

	private void sendFeedByPosition(double sceneX, double sceneY) {
		if (isLinein != null && isLinein && isPowerOn) {
			int calFeed = CircleTools.calFeedByPosition(sceneX, sceneY);
			if (speakNum == calFeed) {
				return;
			}
			speakNum = calFeed;
			setFeed();
		}
	}

	public void scanPortTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				scanPort();
//				clearPort();
			}
		}, 1000, 4000);
	}

	// 打开串口
	public void scanPort() {
		List<String> portList = ChannelUtil.findComPort();
		if (portList == null || portList.size() == 0) {
			return;
		}
		for (String port : portList) {
			if (CommandUtil.online_ports.containsKey(port)) {
				continue;
			} else {
				new Thread(() -> {
					new SerialClient().connect(port);
				}).start();
			}
		}
		for (String port : DeviceData.getData().keySet()) {
			if (portList.contains(port)) {
				continue;
			} else {
				if (port.equals(selectedPort)) {
					selectedPort = null;
					speakNum = 0;
					Platform.runLater(() -> {
						speak.setText(speakNum + "");
						CircleTools.showCircle(speakNum, circle, circleProgress);
					});
				}
				BusinessData.clearCacheWiteLineout(port);
			}
		}
	}

	public void changeSelectPort(String name) {
		hideCom();
		selectedPort = name;
		isLinein = false;
		showCom();
	}

	public void subFeed() {
		if (isLinein != null && isLinein && isPowerOn) {
			if (speakNum == 0) {
				return;
			}
			speakNum--;
			setFeed();
		}
	}

	public void addFeed() {
		if (isLinein != null && isLinein && isPowerOn) {
			if (speakNum == 10) {
				return;
			}
			speakNum++;
			setFeed();
		}
	}

	private void setFeed() {
		speak.setText(speakNum + "");
		CircleTools.showCircle(speakNum, circle, circleProgress);
		SerialPort ctx = CommandUtil.online_ports.get(selectedPort);
		if (ctx != null) {
			CommandUtil.sendControlFeed(ctx, speakNum);
		}
	}

	public void clickCircle(MouseEvent e) {
		double sceneX = e.getSceneX();
		double sceneY = e.getSceneY();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				sendFeedByPosition(sceneX, sceneY);
			}
		});
	}

	@FXML
	public void aboutUs(Event event) {
		try {
			Desktop.getDesktop().browse(new URI("https://www.acinfinity.com/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
