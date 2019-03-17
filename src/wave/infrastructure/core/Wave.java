package wave.infrastructure.core;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Wave
{
	
	public final static String APPLICATION_NAME = "Weather Auditory and Visual Environment";
	public final static Path MAIN_ICON = Paths.get("data", "icons", "wave_logo-128x128.png");
	public final static Path COMPASS_ICON = Paths.get("data", "icons", "notched_compass_dark.png");
	public final static Path PAUSE_ICON = Paths.get("data", "icons", "pause_light.png");
	public final static Path PLAY_ICON = Paths.get("data", "icons", "play_light.png");
	public final static Path WAVE_CONFIG_FILE = Paths.get("data", "config", "wave.xml");
	public final static Path WAVE_WW_CONFIG_FILE = Paths.get("data", "config", "wave_ww.xml");
	public final static Path WAVE_CSS_FILE = Paths.get("data", "css", "wave.css");
	public final static String VERSION = "0.1";
	public final static int DEFAULT_WINDOW_WIDTH = 1280;
	public final static int DEFAULT_WINDOW_HEIGHT = 700;
}
