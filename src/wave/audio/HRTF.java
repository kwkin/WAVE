package wave.audio;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum HRTF
{
	CIPIC_58(Paths.get("data", "hrtf", "hrir_final.mat"));
	
	private Path path;
	
	HRTF(Path path)
	{
		this.path = path;
	}
	
	public Path getPath()
	{
		return this.path;
	}
}
