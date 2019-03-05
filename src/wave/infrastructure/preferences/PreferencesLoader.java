package wave.infrastructure.preferences;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
				JAXBContext contextWrite = JAXBContext.newInstance(Preferences.class);
				Marshaller marshaller = contextWrite.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(preferences, configFile.toFile());
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
