package wave.infrastructure.preferences;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

public class PreferencesLoader
{
	private static Preferences currentPreferences;

	public static void loadPreferences(Path configFile) throws IOException
	{
		Preferences preferences = null;
		try
		{
			if (Files.notExists(configFile))
			{
				preferences = Preferences.loadDefault();
				preferences.writePreferences(configFile);
			}
			else
			{
				preferences = Preferences.load(configFile);
			}
		}
		catch (JAXBException | IOException e)
		{
			preferences = Preferences.loadDefault();
		}
		PreferencesLoader.currentPreferences = preferences;
	}

	public static Preferences preferences()
	{
		return PreferencesLoader.currentPreferences;
	}
	
}
