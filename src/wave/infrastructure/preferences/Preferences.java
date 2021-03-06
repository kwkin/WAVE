package wave.infrastructure.preferences;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import gov.nasa.worldwind.WorldWind;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import wave.infrastructure.core.AngleFormat;
import wave.infrastructure.core.AudioListener;
import wave.infrastructure.core.MeasurementSystem;

@XmlRootElement(name = "preferences")
public class Preferences implements Serializable
{
	private static final long serialVersionUID = 1L;

	// Display settings
	private ObjectProperty<MeasurementSystem> lengthUnitDisplayProperty;
	private ObjectProperty<AngleFormat> angleFormatDisplayProperty;
	private BooleanProperty enablePerformancePanelProperty;
	private BooleanProperty enableLayerPanelProperty;
	private BooleanProperty enableNetworkProperty;

	// Audio settings
	private DoubleProperty masterVolumeProperty;
	private DoubleProperty rainVolumeProperty;
	private DoubleProperty windVolumeProperty;
	private DoubleProperty thunderVolumeProperty;
	private BooleanProperty enableWeatherModulatorsProperty;
	private ObjectProperty<AudioListener> audioListenerProperty;

	// Marker Settings
	private BooleanProperty showAllWeatherProperty;
	private BooleanProperty showAnnotationProperty;
	private BooleanProperty showPositionProperty;
	
	private Preferences()
	{
		this.lengthUnitDisplayProperty = new SimpleObjectProperty<MeasurementSystem>(MeasurementSystem.METRIC);
		this.angleFormatDisplayProperty = new SimpleObjectProperty<AngleFormat>(AngleFormat.DMS);
		this.masterVolumeProperty = new SimpleDoubleProperty(1);
		this.rainVolumeProperty = new SimpleDoubleProperty(1);
		this.windVolumeProperty = new SimpleDoubleProperty(1);
		this.thunderVolumeProperty = new SimpleDoubleProperty(1);
		this.enableWeatherModulatorsProperty = new SimpleBooleanProperty(true);
		this.enableNetworkProperty = new SimpleBooleanProperty(false);
		this.enablePerformancePanelProperty = new SimpleBooleanProperty(true);
		this.enableLayerPanelProperty = new SimpleBooleanProperty(true);
		this.audioListenerProperty = new SimpleObjectProperty<AudioListener>(AudioListener.MARKER);
		this.showAnnotationProperty = new SimpleBooleanProperty(true);
		this.showPositionProperty = new SimpleBooleanProperty(false);
		this.showAllWeatherProperty = new SimpleBooleanProperty(true);

		this.enableNetworkProperty.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (oldValue != newValue)
				{
					WorldWind.setOfflineMode(!newValue);
				}
			}
		});
		WorldWind.setOfflineMode(true);
	}

	public static Preferences loadDefault()
	{
		return new Preferences();
	}

	public static Preferences load(Path configFile) throws IOException
	{
		Preferences preferences = null;
		try (InputStream inStream = new FileInputStream(configFile.toFile()))
		{
			JAXBContext contextRead = JAXBContext.newInstance(Preferences.class);
			Unmarshaller unmarshaller = contextRead.createUnmarshaller();
			preferences = (Preferences) unmarshaller.unmarshal(inStream);
			inStream.close();
		}
		catch (IOException | JAXBException e)
		{
			throw new IOException(e.getMessage());
		}
		return preferences;
	}

	/**
	 * Writes the current preferences to the config file
	 * 
	 * If the file already exists, it will be overwritten
	 * 
	 * @param configFile Path to the config file
	 * @throws JAXBException
	 */
	public void writePreferences(Path configFile) throws JAXBException
	{
		JAXBContext contextWrite = JAXBContext.newInstance(Preferences.class);
		Marshaller marshaller = contextWrite.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, configFile.toFile());
	}

	/**
	 * Sets all preferences to the specified preferences
	 * 
	 * @param preferences The preferencs to copy form
	 */
	public void setPreferences(Preferences preferences)
	{
		this.lengthUnitDisplayProperty.setValue(preferences.getLengthUnitDisplay());
		this.angleFormatDisplayProperty.setValue(preferences.getAngleUnitDisplay());
		this.masterVolumeProperty.setValue(preferences.getMasterVolume());
		this.rainVolumeProperty.setValue(preferences.getRainVolume());
		this.windVolumeProperty.setValue(preferences.getWindVolume());
		this.thunderVolumeProperty.setValue(preferences.getThunderVolume());
		this.enableWeatherModulatorsProperty.setValue(preferences.getEnableWeatherModulators());
		this.enableNetworkProperty.setValue(preferences.getEnableNetwork());
		this.enablePerformancePanelProperty.setValue(preferences.getEnablePerformancePanel());
		this.enableLayerPanelProperty.setValue(preferences.getEnableLayerPanel());
		this.audioListenerProperty.setValue(preferences.getAudioListener());
	}

	public boolean getShowAllWeather()
	{
		return this.showAllWeatherProperty.getValue();
	}

	public void setShowAllWeather(boolean showAllWeather)
	{
		this.showAllWeatherProperty.setValue(showAllWeather);
	}

	public BooleanProperty showAllWeatherProperty()
	{
		return this.showAllWeatherProperty;
	}

	public boolean getShowAnnotation()
	{
		return this.showAnnotationProperty.getValue();
	}

	public void setShowAnnotation(boolean enableLayerPanel)
	{
		this.showAnnotationProperty.setValue(enableLayerPanel);
	}

	public BooleanProperty showAnnotationProperty()
	{
		return this.showAnnotationProperty;
	}

	public boolean getShowPosition()
	{
		return this.showPositionProperty.getValue();
	}

	public void setShowPosition(boolean enableLayerPanel)
	{
		this.showPositionProperty.setValue(enableLayerPanel);
	}

	public BooleanProperty showPositionProperty()
	{
		return this.showPositionProperty;
	}

	/**
	 * Returns the length unit display
	 * 
	 * @return The length unit display
	 */
	public MeasurementSystem getLengthUnitDisplay()
	{
		return this.lengthUnitDisplayProperty.getValue();
	}

	/**
	 * Sets the length unit display
	 * 
	 * @param measurementSystem The measurement system to set
	 */
	public void setLengthUnitDisplay(MeasurementSystem measurementSystem)
	{
		this.lengthUnitDisplayProperty.setValue(measurementSystem);
	}

	/**
	 * Returns the length unit display property
	 * 
	 * @return The length unit display property
	 */
	public ObjectProperty<MeasurementSystem> lengthUnitDisplayProperty()
	{
		return this.lengthUnitDisplayProperty;
	}

	/**
	 * Returns the angle format display
	 * 
	 * @return The angle unit display
	 */
	public AngleFormat getAngleUnitDisplay()
	{
		return this.angleFormatDisplayProperty.getValue();
	}

	/**
	 * Sets the angle unit display
	 * 
	 * @param angleFormat The angle format to set
	 */
	public void setAngleUnitDisplay(AngleFormat angleFormat)
	{
		this.angleFormatDisplayProperty.setValue(angleFormat);
	}

	/**
	 * Returns the angle format display property
	 * 
	 * @return The angle display property
	 */
	public ObjectProperty<AngleFormat> angleUnitDisplayProperty()
	{
		return this.angleFormatDisplayProperty;
	}

	public double getMasterVolume()
	{
		double volume = this.masterVolumeProperty.getValue();
		volume = Math.min(volume, 1);
		return volume;
	}

	public void setMasterVolume(double masterVolume)
	{
		this.masterVolumeProperty.setValue(masterVolume);
	}

	public DoubleProperty masterVolumeProperty()
	{
		return this.masterVolumeProperty;
	}

	public double getRainVolume()
	{
		return this.rainVolumeProperty.getValue();
	}

	public void setRainVolume(double rainVolume)
	{
		this.rainVolumeProperty.setValue(rainVolume);
	}

	public DoubleProperty rainVolumeProperty()
	{
		return this.rainVolumeProperty;
	}

	public double getWindVolume()
	{
		return this.windVolumeProperty.getValue();
	}

	public void setWindVolume(double windVolume)
	{
		this.windVolumeProperty.setValue(windVolume);
	}

	public DoubleProperty windVolumeProperty()
	{
		return this.windVolumeProperty;
	}

	public double getThunderVolume()
	{
		return this.thunderVolumeProperty.getValue();
	}

	public void setThunderVolume(double thunderVolume)
	{
		this.thunderVolumeProperty.setValue(thunderVolume);
	}

	public DoubleProperty thunderVolumeProperty()
	{
		return this.thunderVolumeProperty;
	}

	public boolean getEnableWeatherModulators()
	{
		return this.enableWeatherModulatorsProperty.getValue();
	}

	public void setEnableWeatherModulators(boolean useWeatherModulators)
	{
		this.enableWeatherModulatorsProperty.setValue(useWeatherModulators);
	}

	public BooleanProperty enableWeatherModulatorsProperty()
	{
		return this.enableWeatherModulatorsProperty;
	}

	public boolean getEnableNetwork()
	{
		return this.enableNetworkProperty.getValue();
	}

	public void setEnableNetwork(boolean enableNetwork)
	{
		this.enableNetworkProperty.setValue(enableNetwork);
	}

	public BooleanProperty enableNetworkProperty()
	{
		return this.enableNetworkProperty;
	}

	public boolean getEnablePerformancePanel()
	{
		return this.enablePerformancePanelProperty.getValue();
	}

	public void setEnablePerformancePanel(boolean enablePerformancePanel)
	{
		this.enablePerformancePanelProperty.setValue(enablePerformancePanel);
	}

	public BooleanProperty enablePerformancePanelProperty()
	{
		return this.enablePerformancePanelProperty;
	}

	public boolean getEnableLayerPanel()
	{
		return this.enableLayerPanelProperty.getValue();
	}

	public void setEnableLayerPanel(boolean enableLayerPanel)
	{
		this.enableLayerPanelProperty.setValue(enableLayerPanel);
	}

	public BooleanProperty enableLayerPanelProperty()
	{
		return this.enableLayerPanelProperty;
	}

	public AudioListener getAudioListener()
	{
		return this.audioListenerProperty.getValue();
	}

	public void setAudioListener(AudioListener audioListener)
	{
		this.audioListenerProperty.setValue(audioListener);
	}

	public ObjectProperty<AudioListener> audioListenerProperty()
	{
		return this.audioListenerProperty;
	}
}
