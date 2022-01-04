package com.cooltron.typec.swing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.stream.Collectors;

import com.cooltron.typec.fastSerialPort.protocol.util.BusinessData;
import com.cooltron.typec.fastSerialPort.protocol.util.ByteCountData;
import com.cooltron.typec.fastSerialPort.protocol.util.CommandUtil;
import com.cooltron.typec.fastSerialPort.protocol.util.DeviceData;
import com.cooltron.typec.fastSerialPort.protocol.util.ExternalDeviceData;
import com.cooltron.typec.swing.bean.Device;
import com.cooltron.typec.swing.bean.ExternalDevice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataMode implements Observer {

	public DataMode() {

	}

	@FXML
	ListView<String> listView;
	@FXML
	Label connDevice;
	@FXML
	Label serial = new Label();
	@FXML
	Label version = new Label();
	@FXML
	Label deviceType = new Label();
	@FXML
	Label deviceStatus = new Label();
	@FXML
	Label serialPortName = new Label();
	@FXML
	Label speak;
	@FXML
	Label receiveCount;
	@FXML
	Label sendCount;
	@FXML
	Circle circle;
	@FXML
	Arc circleProgress;
	@FXML
	Pane devInfo;
	@FXML
	Pane connedStatus = new Pane();
	@FXML
	Pane connedDevice = new Pane();
	@FXML
	Pane linedInDevice = new Pane();
	@FXML
	Pane devInfoControl = new Pane();
	@FXML
	public Button conn = new Button("CONNECT");
	@FXML
	Circle devStatus = new Circle();

	public static String selectedPort;

	Integer speakNum = 0;

	ObservableList<String> obList;

	Boolean isLinein = false;
	Boolean isPowerOn = false;

	public void operateConn() {
		if (selectedPort != null) {
			if (conn.getText().equals("CONNECT")) {
				isLinein = true;
				showDeviceStatus();
			} else {
				isLinein = null;
				hideDeviceStatus();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		try {
			Platform.runLater(() -> {
				refreshView(o, arg);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshView(Observable o, Object arg) {
		if (o instanceof BusinessData) {

		} else if (o instanceof DeviceData) {
			LinkedHashSet<String> obSet = CommandUtil.online_ports.keySet().stream().filter(portName -> {
				return ((Map<String, Device>) arg).keySet().contains(portName);
			}).collect(Collectors.toCollection(LinkedHashSet::new));
			obList = FXCollections.observableArrayList(obSet);
			listView.setItems(obList);
			listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
				@Override
				public ListCell<String> call(ListView<String> param) {
					Label label = new Label();
					ListCell<String> cell = new ListCell<String>() {
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (empty == false) {
								label.setText(((Map<String, Device>) arg).get(item).getDeviceType());
								label.setTextFill(Color.WHITE);
								this.setGraphic(label);
							}
						}
					};
					return cell;
				}
			});
			if (selectedPort == null) {
				if (!obList.isEmpty()) {
					selectedPort = obList.get(0);
					listView.getSelectionModel().select(0);
				}
			}
			showCom();
		} else if (o instanceof ExternalDeviceData) {
			initExternalDeviceType();
		} else if (o instanceof ByteCountData) {
			showDeviceDataCount();
		}
	}

	public void showCom() {
		System.out.println("显示信息面板");
		devInfo.setVisible(true);
		showDeviceDetail();
		showDeviceDataCount();
	}

	public void hideCom() {
		System.out.println("隐藏信息面板");
		devInfo = new Pane();
		devInfo.setVisible(false);
	}

	public void showDeviceDetail() {
		System.out.println("设备信息--显示");
		initDeviceDeatil();
		initExternalDeviceType();
	}

	public void hideDeviceDetail() {
		System.out.println("设备信息--隐藏");
	}

	public void showDeviceDataCount() {
		System.out.println("设备流量--显示");
		if (selectedPort != null) {
			String count = ByteCountData.getData().get(selectedPort);
			if (count == null) {
				receiveCount.setText("RECEIVED: 0 BYTES");
				sendCount.setText("SENT: 0 BYTES");
			} else {
				String read = count.split("#")[0];
				String write = count.split("#")[1];
				receiveCount.setText("RECEIVED: " + read + " BYTES");
				sendCount.setText("SENT: " + write + " BYTES");
			}
		} else {
			receiveCount.setText("RECEIVED: 0 BYTES");
			sendCount.setText("SENT: 0 BYTES");
		}
	}

	public void initDeviceDeatil() {
		System.out.println("-----加载设备信息-----");
		if (selectedPort != null) {
			Device device = DeviceData.getData().get(selectedPort);
			if (device == null) {
				isLinein = false;
				serial.setText("");
				version.setText("");
				deviceType.setText("");
			} else {
				if (isLinein == null) {
					isLinein = null;
				} else {
					isLinein = true;
				}
				serial.setText(device.getSerial());
				version.setText(device.getVersion());
				deviceType.setText(device.getDeviceType());
			}
		} else {
			isLinein = false;
			serial.setText("");
			version.setText("");
			deviceType.setText("");
		}
		if (isLinein == null) {
			if (selectedPort != null) {
				hideDeviceStatus();
			} else {
				clearDeviceStatus();
			}
		} else if (isLinein) {
			showDeviceStatus();
		} else {
			if (selectedPort != null) {
				hideDeviceStatus();
			} else {
				clearDeviceStatus();
			}
		}
	}

	public void initExternalDeviceType() {
		System.out.println("-----加载设备运行状态-----");
		if (selectedPort != null) {
			ExternalDevice externalDevice = ExternalDeviceData.getData().get(selectedPort);
			if (externalDevice == null || externalDevice.getFeed() <0 || externalDevice.getFeed() > 10  ) {
				isPowerOn = false;
				speakNum = 0;
				speak.setText(speakNum + "");
				CircleTools.showCircle(speakNum, circle, circleProgress);
				hideExternalDeviceType();
			} else {
				isPowerOn = true;
				speakNum = externalDevice.getFeed();
				speak.setText(speakNum + "");
				CircleTools.showCircle(speakNum, circle, circleProgress);
				showExternalDeviceType();
			}
		} else {
			isPowerOn = false;
			hideExternalDeviceType();
		}

	}

	public void showExternalDeviceType() {
		System.out.println("设备运行状态--正常");
		devStatus.setFill(Color.GREEN);
		int i = (deviceType.getText().length() > 8 ? 8 : deviceType.getText().length()) * 10;
		System.out.println("====="+i);
		devStatus.setLayoutX(80 + i);

	}

	private void hideExternalDeviceType() {
		System.out.println("设备运行状态--异常");
		int i = (deviceType.getText().length() > 8 ? 8 : deviceType.getText().length()) * 10;
		System.out.println("====="+i);
		devStatus.setFill(Color.RED);
		devStatus.setLayoutX(80 + i);
	}

	public void showDeviceStatus() {
		System.out.println("设备连接状态--正常");
		deviceStatus.setText("CONNECTED");
		deviceStatus.setTextFill(Color.GREEN);
		conn.setText("DISCONNECT");
	}

	public void clearDeviceStatus() {
		System.out.println("设备连接状态--闲置");
		deviceStatus.setText("");
		conn.setText("CONNECT");
	}

	public void hideDeviceStatus() {
		System.out.println("设备连接状态--异常");
		deviceStatus.setText("DISCONNECT");
		deviceStatus.setTextFill(Color.RED);
		conn.setText("CONNECT");
	}
}
