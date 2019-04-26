package wave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import gov.nasa.worldwind.Configuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wave.infrastructure.WaveSession;
import wave.infrastructure.core.Wave;
import wave.infrastructure.handlers.ErrorDialog;
import wave.infrastructure.handlers.FXThemeLoader;
import wave.infrastructure.preferences.PreferencesLoader;
import wave.views.WaveWindow;

// TODO:
// - Crossfading
// - Varying Intensity
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
		if (initializeSettings())
		{
			WaveApp.session = new WaveSession();
			WaveWindow window = new WaveWindow(session);
			Scene primaryScene = new Scene(window, Wave.DEFAULT_WINDOW_WIDTH, Wave.DEFAULT_WINDOW_HEIGHT);
			WaveApp.primaryStage.setTitle(Wave.APPLICATION_NAME);
			WaveApp.primaryStage.setScene(primaryScene);
			FXThemeLoader.applyDefaultTheme(primaryStage);
			WaveApp.primaryStage.show();
		}
	}

	@Override
	public void stop()
	{
		if (WaveApp.session != null)
		{
			WaveApp.session.shutdown();
		}
		Platform.exit();
	}

	private boolean initializeSettings()
	{
		System.setProperty("sun.awt.noerasebackground", "true");
		Path configFile = Wave.WAVE_CONFIG_FILE;
		Path wwConfigFile = Wave.WAVE_WW_CONFIG_FILE;

		boolean isInitialized = false;
		try
		{
			PreferencesLoader.loadPreferences(configFile);
			if (Files.exists(wwConfigFile))
			{
				Configuration.insertConfigurationDocument(wwConfigFile.toAbsolutePath().toString());
			}
			isInitialized = true;
		}
		catch (IOException e)
		{
			StringBuilder errorMessage = new StringBuilder("Error: Unable to initialize settings. \n\n");
			errorMessage.append(e.getMessage());
			ErrorDialog.show(null, errorMessage.toString());
		}
		return isInitialized;
	}
}
