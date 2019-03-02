package wave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.views.WaveWindow;

public class WaveApp extends Application
{
	private static Stage primaryStage;

	public static void main(String[] args)
	{
		launch(args);
	}

	public static Stage getStage()
	{
		return WaveApp.primaryStage;
	}

	@Override
	public void start(Stage primaryStage)
	{
		WaveApp.primaryStage = primaryStage;
		WaveApp.primaryStage.setTitle(Wave.APPLICATION_NAME);
		WaveSession session = new WaveSession();
		WaveWindow window = new WaveWindow(session);
		Scene primaryScene = new Scene(window, Wave.DEFAULT_WINDOW_WIDTH, Wave.DEFAULT_WINDOW_HEIGHT);
		WaveApp.primaryStage.setScene(primaryScene);
		WaveApp.primaryStage.setOnCloseRequest(event ->
		{
			session.shutdown();
		});
		WaveApp.primaryStage.show();
	}

	/**
	 * Set default configuration
	 */
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
