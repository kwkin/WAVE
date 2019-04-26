package wave.audio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import wave.infrastructure.handlers.HRTFData;
import wave.infrastructure.util.PointUtil;

public abstract class WeatherAudio
{
	protected double headingAngle;
	protected double elevationAngle; 
	protected int headingIndex;
	protected int elevationIndex;
	protected Path soundPath;
	protected int duration;
	protected HRTFData hrtf;

	public WeatherAudio()
	{
		this.headingAngle = -1;
		this.elevationAngle = -1;
		try
		{
			Path hrtfPath = HRTF.CIPIC_58.getPath();
			this.hrtf = HRTFData.LoadHRTF(hrtfPath);
		}
		catch (Exception e)
		{
			
		}
		this.updateAngles();
	}
	
	public WeatherAudio(Path soundPath)
	{
		this.headingAngle = -1;
		this.elevationAngle = -1;
		this.soundPath = soundPath;
		this.duration = this.calculateDuration();
		try
		{
			Path hrtfPath = HRTF.CIPIC_58.getPath();
			this.hrtf = HRTFData.LoadHRTF(hrtfPath);
		}
		catch (Exception e)
		{
			
		}
		this.updateAngles();
	}
	
	public WeatherAudio(double headingAngle, double elevationAngle, Path soundPath)
	{
		this.headingAngle = headingAngle;
		this.elevationAngle = elevationAngle;
		this.soundPath = soundPath;
		this.duration = this.calculateDuration();
		try
		{
			Path hrtfPath = HRTF.CIPIC_58.getPath();
			this.hrtf = HRTFData.LoadHRTF(hrtfPath);
		}
		catch (Exception e)
		{
			
		}
		this.updateAngles();
	}
	
	public WeatherAudio(double headingAngle, double elevationAngle, Path soundPath, HRTF hrtfFile)
	{
		this.headingAngle = headingAngle;
		this.elevationAngle = elevationAngle;
		this.soundPath = soundPath;
		this.duration = this.calculateDuration();
		try
		{
			Path hrtfPath = hrtfFile.getPath();
			this.hrtf = HRTFData.LoadHRTF(hrtfPath);
		}
		catch (Exception e)
		{
			
		}
		this.updateAngles();
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public Path getSoundFile()
	{
		return this.soundPath;
	}
	
	public void setSoundFile(Path soundPath)
	{
		this.soundPath = soundPath;
	}

	public void setHeading(double heading)
	{
		this.headingAngle = heading;
		this.updateAngles();
	}
	
	public double getHeading()
	{
		return this.headingAngle;
	}

	public int getHeadingIndex()
	{
	    return (int) Math.round(this.getHeadingIndexPrecise());
	}
	
	public double getHeadingIndexPrecise()
	{
		double index = -1;
		if (this.headingAngle != -1)
		{
			if (this.headingAngle >= -80 && this.headingAngle <= -65)
			{
				index = PointUtil.map(this.headingAngle, -80, -65, 0, 1);
			}
			else if (this.headingAngle >= -65 && this.headingAngle <= -45)
			{
				index = PointUtil.map(this.headingAngle, -65, -45, 1, 3);
			}
			else if (this.headingAngle >= -45 && this.headingAngle <= 45)
			{
				index = PointUtil.map(this.headingAngle, -45, 45, 3, 21);
			}
			else if (this.headingAngle >= 45 && this.headingAngle <= 65)
			{
				index = PointUtil.map(this.headingAngle, 45, 65, 21, 23);
			}
			else if (this.headingAngle >= 65 && this.headingAngle <= 80)
			{
				index = PointUtil.map(this.headingAngle, 65, 80, 23, 24);
			}
		}
	    return index;
	}
	
	public void setElevation(double elevation)
	{
		this.elevationAngle = elevation;
		this.updateAngles();
	}
	
	public double getElevation()
	{
		return this.elevationAngle;
	}
	
	public int getElevationIndex()
	{
	    return (int) Math.round(this.getElevationIndexPrecise());
	}
	
	public double getElevationIndexPrecise()
	{
		double eIndex = -1;
		if (this.elevationAngle != -1)
		{
			eIndex = ((this.elevationAngle + 45) / 5.625);
		}
	    return eIndex;
	}
	
	
	public HRTFData getHRTF()
	{
		return this.hrtf;
	}
	
	private void updateAngles()
	{
		double radHeading = Math.toRadians(this.headingAngle);
		double normalized = Math.toDegrees(Math.atan2(Math.sin(radHeading), Math.cos(radHeading)));
		
		if ((normalized > 90 || normalized < -90))
		{
			if (normalized > 90)
			{
				normalized = 180 - normalized;
			}
			else
			{
				normalized = -180 - normalized;
			}
			this.headingAngle = normalized;
			
			if (this.elevationAngle < 90)
			{
				this.elevationAngle = 180 - this.elevationAngle;
			}
		}
		else if (this.elevationAngle > 90)
		{
			this.elevationAngle = 180 - this.elevationAngle;
		}
	}
	
	private int calculateDuration()
	{

		File sourceSound = this.soundPath.toFile();
		int duration = this.duration;
		AudioInputStream audioInputStream;
		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(sourceSound);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			duration = (int) ((frames + 0.0) / format.getFrameRate() * 1000);
		}
		catch (UnsupportedAudioFileException | IOException e)
		{
		}
		return duration;
	}
	
	public abstract boolean stopAudio();
	public abstract boolean playAudio();
	
//	public static void main(String[] args)
//	{
//		double heading = 0;
//		double elevation = 0;
//		Path sound = Rain.HEAVY;
//		WeatherAudio audio = new WeatherAudio(heading, elevation, sound);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(-20);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(-45);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(-60);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(-90);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(-110);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(-135);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(-160);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(180);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(0);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(45);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//
//		audio.setHeading(60);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(90);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(110);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(135);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//		
//		audio.setHeading(160);
//		System.out.println("Heading Angle: " + audio.getHeading() + "    Heading Index: " + audio.getHeadingIndex());
//		System.out.println("Elevation Angle: " + audio.getElevation() + "    Elevation Index: " + audio.getElevationIndex());
//	}
}
