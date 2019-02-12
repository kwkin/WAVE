package wave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.models.WaveSession;
import wave.views.WaveWindow;

public class Wave extends Application
{
	private static String APP_NAME = "Weather Auditory and Visual Environment";

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle(APP_NAME);
		WaveSession session = new WaveSession();
		WaveWindow window = new WaveWindow(session);
		primaryStage.setScene(new Scene(window, 1280, 720));
		primaryStage.setOnCloseRequest(event ->
		{
			session.shutdown();
		});
		primaryStage.show();
	}

	static
	{
		WorldWind.setOfflineMode(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		Path configFile = Paths.get("data", "config", "wave.xml");
		if (Files.exists(configFile))
		{
			Configuration.insertConfigurationDocument(configFile.toAbsolutePath().toString());
		}
	}
}
