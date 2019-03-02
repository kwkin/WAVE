package wave.infrastructure.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.scene.Scene;

public class FXThemeLoader
{
	/**
	 * Loads the css stylesheet for the scene
	 * 
	 * @param scene The scene to apply the stylesheet to
	 * @param stylesheet The path to the stylesheet
	 * @throws IOException If the file does not exist
	 */
	public static void loadTheme(Scene scene, Path stylesheet) throws IOException
	{
		if (Files.exists(stylesheet))
		{
			scene.getStylesheets().clear();
			scene.getStylesheets().add("file:///" + stylesheet.toAbsolutePath().toString().replace("\\", "/"));
		}
		else
		{
			StringBuilder message = new StringBuilder("File ");
			message.append(stylesheet.toAbsolutePath());
			message.append(" does not exist.");
			throw new IOException(message.toString());
		}
	}
}
