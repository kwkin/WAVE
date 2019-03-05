package wave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.FXThemeLoader;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.views.WaveWindow;

public class WaveApp extends Application
{
	private static Stage primaryStage;
	private static WaveSession session;

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
		try
		{
			FXThemeLoader.setIcon(WaveApp.primaryStage, Wave.MAIN_ICON);
		}
		catch (IOException e)
		{

		}
		WaveApp.session = new WaveSession();
		WaveWindow window = new WaveWindow(session);
		Scene primaryScene = new Scene(window, Wave.DEFAULT_WINDOW_WIDTH, Wave.DEFAULT_WINDOW_HEIGHT);
		WaveApp.primaryStage.setScene(primaryScene);
		WaveApp.primaryStage.show();
	}

	@Override
	public void stop()
	{
		WaveApp.session.shutdown();
		Platform.exit();
	}

	static
	{
		WorldWind.setOfflineMode(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		Path configFile = Wave.WAVE_CONFIG_FILE;
		Path wwConfigFile = Wave.WAVE_WW_CONFIG_FILE;

		// TODO when error, throw javafx error dialog
		try
		{
			PreferencesLoader.loadPreferences(configFile);
			if (Files.exists(wwConfigFile))
			{
				Configuration.insertConfigurationDocument(wwConfigFile.toAbsolutePath().toString());
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
