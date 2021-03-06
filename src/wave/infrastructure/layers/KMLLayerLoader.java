package wave.infrastructure.layers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import javax.xml.stream.XMLStreamException;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.ogc.kml.KMLAbstractFeature;
import gov.nasa.worldwind.ogc.kml.KMLRoot;
import gov.nasa.worldwind.ogc.kml.impl.KMLController;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;
import javafx.application.Platform;
import wave.infrastructure.WaveSession;

public class KMLLayerLoader extends Thread
{
	protected File kmlSource;
	protected WaveSession session;
	protected boolean isLoadImmediately;
	protected String name;

	public KMLLayerLoader(Path kmlSource, WaveSession session, boolean isLoadImmediately, String name)
	{
		this.kmlSource = kmlSource.toFile();
		this.session = session;
		this.isLoadImmediately = isLoadImmediately;
		this.name = name;
	}

	public KMLLayerLoader(Path kmlSource, WaveSession session, boolean isLoadImmediately)
	{
		this.kmlSource = kmlSource.toFile();
		this.session = session;
		this.isLoadImmediately = isLoadImmediately;
		this.name = null;
	}

	public KMLLayerLoader(Path kmlSource, WaveSession session)
	{
		this.kmlSource = kmlSource.toFile();
		this.session = session;
		this.isLoadImmediately = false;
		this.name = null;
	}

	public KMLLayerLoader(File kmlSource, WaveSession session, boolean isLoadImmediately, String name)
	{
		this.kmlSource = kmlSource;
		this.session = session;
		this.isLoadImmediately = isLoadImmediately;
		this.name = name;
	}

	public KMLLayerLoader(File kmlSource, WaveSession session, boolean isLoadImmediately)
	{
		this.kmlSource = kmlSource;
		this.session = session;
		this.isLoadImmediately = isLoadImmediately;
		this.name = null;
	}

	public KMLLayerLoader(File kmlSource, WaveSession session)
	{
		this.kmlSource = kmlSource;
		this.session = session;
		this.isLoadImmediately = false;
		this.name = null;
	}

	public void setIsloadImmediately(boolean isLoadImmediately)
	{
		this.isLoadImmediately = isLoadImmediately;
	}

	public void run()
	{
		loadKML();
	}

	public KMLLayer loadKML()
	{
		KMLLayer layer = new KMLLayer();
		try
		{
			KMLRoot kmlRoot = this.parse();

			// Set the document's display name
			kmlRoot.setField(AVKey.DISPLAY_NAME, formName(this.kmlSource, kmlRoot));

			KMLController kmlController = new KMLController(kmlRoot);
			layer.setName((String) kmlRoot.getField(AVKey.DISPLAY_NAME));
			layer.addRenderable(kmlController);
			if (!this.isLoadImmediately)
			{
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						session.addKMLLayer(layer);
					}
				});
			}
			else
			{
				session.addKMLLayer(layer);
			}
		}
		catch (IOException | XMLStreamException e)
		{
			e.printStackTrace();
		}
		return layer;
	}

	protected KMLRoot parse() throws IOException, XMLStreamException
	{
		return KMLRoot.createAndParse(this.kmlSource);
	}

	protected String formName(Object kmlSource, KMLRoot kmlRoot)
	{
		KMLAbstractFeature rootFeature = kmlRoot.getFeature();
		String layerName = "KML Name";
		if (this.name != null)
		{
			layerName = this.name;
		}
		else if (rootFeature != null && !WWUtil.isEmpty(rootFeature.getName()))
		{
			layerName = rootFeature.getName();
		}
		else if (kmlSource instanceof File)
		{
			layerName = ((File) kmlSource).getName();
		}
		else if (kmlSource instanceof URL)
		{
			layerName = ((URL) kmlSource).getPath();
		}
		else if (kmlSource instanceof String && WWIO.makeURL((String) kmlSource) != null)
		{
			layerName = WWIO.makeURL((String) kmlSource).getPath();
		}
		return layerName;
	}
}
