package wave.infrastructure.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
	
	/**
	 * Sets the desktop and window icons
	 * 
	 * @param stage The primary stage
	 * @param icon The path to the icon
	 * @throws IOException If the file does not exist
	 */
	public static void setIcon(Stage stage, Path icon) throws IOException
	{
		if (Files.exists(icon))
		{
			stage.getIcons().add(new Image("file:///" + icon.toAbsolutePath().toString().replace("\\", "/")));
		}
		else
		{
			StringBuilder message = new StringBuilder("File ");
			message.append(icon.toAbsolutePath());
			message.append(" does not exist.");
			throw new IOException(message.toString());
		}
	}
}
