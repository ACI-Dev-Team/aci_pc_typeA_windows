package com.cooltron.typec;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
@EnableAsync
public class TypeCApplication extends Application {


	public static void main(String[] args)  {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(TypeCApplication.class.getResource("/control.fxml"));
		loader.load();
		stage.setResizable(false);
		stage.setScene(new Scene(loader.<ScrollPane>getRoot(), 950, 530));
		stage.setTitle("AC Infinity");
		stage.getIcons().add(new Image(TypeCApplication.class.getResourceAsStream("/ico.png")));
		stage.show();
		stage.setOnCloseRequest(event -> {
			Alert alert = new Alert(Alert.AlertType.NONE, "正在释放资源，请稍等片刻...");
			alert.setWidth(300);
			alert.setTitle("提示");
			alert.initStyle(StageStyle.UTILITY);
			alert.show();
			try {
				Runtime runtime = Runtime.getRuntime();
				System.exit(0);
				runtime.exec("taskkill /F /T /IM \"AC Infinity for Windows 32bit.exe\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
		});
		stage.show();
	}

}
