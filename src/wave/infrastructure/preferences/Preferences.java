package wave.infrastructure.preferences;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

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

	private Preferences()
	{
		this.lengthUnitDisplayProperty = new SimpleObjectProperty<MeasurementSystem>(MeasurementSystem.METRIC);
		this.angleFormatDisplayProperty = new SimpleObjectProperty<AngleFormat>(AngleFormat.DMS);
		this.masterVolumeProperty = new SimpleDoubleProperty(100);
		this.rainVolumeProperty = new SimpleDoubleProperty(100);
		this.windVolumeProperty = new SimpleDoubleProperty(100);
		this.thunderVolumeProperty = new SimpleDoubleProperty(100);
		this.enableWeatherModulatorsProperty = new SimpleBooleanProperty(true);
		this.enableNetworkProperty = new SimpleBooleanProperty(false);
		this.enablePerformancePanelProperty = new SimpleBooleanProperty(true);
		this.enableLayerPanelProperty = new SimpleBooleanProperty(true);
	}

	public static Preferences loadDefault()
	{
		return new Preferences();
	}

	public static Preferences load(Path configFile) throws IOException
	{
		Preferences preferences = null;
		try(InputStream inStream = new FileInputStream(configFile.toFile()))
		{
			JAXBContext contextRead = JAXBContext.newInstance(Preferences.class);
			Unmarshaller unmarshaller = contextRead.createUnmarshaller();
			preferences = (Preferences) unmarshaller.unmarshal(inStream);
			inStream.close();
		}
		catch(IOException | JAXBException e)
		{
			throw new IOException(e.getMessage());
		}
		return preferences;
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
		return this.masterVolumeProperty.getValue();
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

	public DoubleProperty RainVolumeProperty()
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

	public DoubleProperty WindVolumeProperty()
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

	public DoubleProperty ThunderVolumeProperty()
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

	public boolean getEnablePerformancePanelProperty()
	{
		return this.enablePerformancePanelProperty.getValue();
	}

	public void setEnablePerformancePanelProperty(boolean enablePerformancePanel)
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
}
