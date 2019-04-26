package wave.audio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.math3.util.MathArrays;

import wave.audio.wav.WavFile;
import wave.infrastructure.handlers.HRTFData;
import wave.infrastructure.preferences.Preferences;
import wave.infrastructure.preferences.PreferencesLoader;

public class SurveySounds extends WeatherAudio
{
	private double scaleFactor;
	private boolean isLoop;
	private PlaySound sound;
	
	public SurveySounds(Path soundPath)
	{
		super(soundPath);
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
	}
	
	public SurveySounds(Path soundPath, boolean isLoop)
	{
		super(soundPath);
		this.isLoop = isLoop;
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
	}

	public SurveySounds(int heading, int elevation, Path soundPath, double scaleFactor)
	{
		super(heading, elevation, soundPath);
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
	}

	public SurveySounds(int heading, int elevation, Path soundPath, double scaleFactor, int duration)
	{
		super(heading, elevation, soundPath);
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
		this.duration = duration;
	}

	public SurveySounds(int heading, int elevation, Path soundPath, double scaleFactor, boolean isLoop)
	{
		super(heading, elevation, soundPath);
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
		this.isLoop = isLoop;
	}

	public SurveySounds(int heading, int elevation, Path soundPath, double scaleFactor, int duration, boolean isLoop)
	{
		super(heading, elevation, soundPath);
		Preferences preferences = PreferencesLoader.preferences();
		double volume = preferences.getMasterVolume();
		this.scaleFactor = volume;
		this.duration = duration;
		this.isLoop = isLoop;
	}

	public boolean playAudio()
	{
		boolean hasStopped = false;
		if (sound != null)
		{
			hasStopped = this.sound.stop();
		}
		// @formatter:off
		this.sound = new PlaySound(
				this.getHeadingIndex(), 
				this.getElevationIndex(), 
				this.soundPath, 
				this.hrtf, 
				this.scaleFactor, 
				this.duration,
				this.isLoop);
		// @formatter:on
		Thread thread = new Thread(this.sound);
		thread.start();
		return hasStopped;
	}

	public boolean stopAudio()
	{
		boolean hasStopped = false;
		if (this.sound != null)
		{
			hasStopped = this.sound.stop();
		}
		return hasStopped;
	}

	public void setScaleFactor(double scaleFactor)
	{
		this.scaleFactor = scaleFactor;
	}
	
	public double getScaleFactor()
	{
		return scaleFactor;
	}
	
	public void setIsLoop(boolean isLoop)
	{
		this.isLoop = isLoop;
	}

	public boolean isLoop()
	{
		return this.isLoop;
	}
}

class PlaySound implements Runnable
{
	private final static File SOUND_FILE = new File("survey_sound.wav"); 
	
	public int aIndex;
	public int eIndex;
	public Path soundToPlay;
	public double scaleFactor;
	public int dur;
	private Clip clip;
	private HRTFData hrtf;
	private boolean isLoop;
	private File soundFile = SOUND_FILE;
	private boolean isStopped;

	public PlaySound(int aIndex, int eIndex, Path sound, HRTFData hrtf, double scaleFactor, int dur, boolean isLoop)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		
		processAudio();
	}
	
	public PlaySound(File soundFile, int aIndex, int eIndex, Path sound, HRTFData hrtf, double scaleFactor, int dur, boolean isLoop)
	{
		this.soundFile = soundFile;
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		
	}

	public void run()
	{
		processAudio();
		if (this.soundFile == null || this.soundToPlay == null || this.isStopped)
		{
			return;
		}
		try
		{
			resetClip();
			if (this.isLoop)
			{
				this.clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			this.clip.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void resetClip() throws LineUnavailableException, UnsupportedAudioFileException, IOException
	{
		AudioInputStream aStream = AudioSystem.getAudioInputStream(this.soundFile);
		this.clip = AudioSystem.getClip();
		this.clip.open(aStream);
	}
	
	public boolean stop()
	{
		this.isStopped = true;
		this.isLoop = false;
		if (this.clip != null)
		{
			this.clip.stop();
			this.clip.close();
			this.clip.drain();
		}
		return this.isStopped;
	}
	
	private void processAudio()
	{
		if (this.soundToPlay == null)
		{
			return;
		}
		try
		{
			File sourceSound = this.soundToPlay.toFile();

			// load a wav file
			WavFile wavFile = WavFile.openWavFile(sourceSound);

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// get total number of frames in source wav file
			int numFrames = (int) (wavFile.getNumFrames());

			// Create buffers for raw audio data frames
			double[] buffer = new double[numFrames * numChannels];
			double[] leftBuffer = new double[numFrames];
			double[] rightBuffer = new double[numFrames];

			// holds convolution values
			double[] lft;
			double[] rgt;

			// holds convolution results
			double[] convLft;
			double[] convRgt;

			// amount of samples to delay
			int delay;

			// final amount of frames
			int finalF;

			// construct final audio data array
			double[][] finalBuffer;

			// Indexing for individual channels
			int j;
			int k;

			// Read frames into buffer from wav file
			wavFile.readFrames(buffer, numFrames);

			// Close the wavFile
			wavFile.close();

			if (aIndex != -1 || eIndex != -1)
			{
				aIndex = Math.max(aIndex, 12);
				eIndex = Math.max(eIndex, 24);
				lft = this.hrtf.getLeftData(aIndex, eIndex);
				rgt = this.hrtf.getRightData(aIndex, eIndex);
				convLft = new double[lft.length + leftBuffer.length - 1];
				convRgt = new double[rgt.length + rightBuffer.length - 1];
				delay = (int) Math.round(hrtf.getDelay(aIndex, eIndex));

				j = 0;
				k = 0;

				// Write data for each channel, audio data is interlaced into buffer array
				// index 0 is the left channel, index 1 is the right channel
				for (int i = 0; i < buffer.length - 1; i++)
				{
					if (i % 2 == 0)
					{
						leftBuffer[j++] = buffer[i] * scaleFactor;
					}
					else
					{
						rightBuffer[k++] = buffer[i] * scaleFactor;
					}
				}

				// perform convolution
				if (aIndex != -1 || eIndex != -1)
				{
					convLft = MathArrays.convolve(lft, leftBuffer);
					convRgt = MathArrays.convolve(rgt, rightBuffer);
				}
				// final amount of frames
				finalF = delay + convLft.length;

				// construct final audio data array
				finalBuffer = new double[2][finalF];

				// delay channel based on azimuth
				if (aIndex < 12)
				{
					for (int i = 0; i < finalF - delay - 1; i++)
					{
						finalBuffer[0][i] = convLft[i];
						finalBuffer[1][i + delay] = convRgt[i];
					}
				}
				else
				{
					for (int i = 0; i < finalF - delay - 1; i++)
					{
						finalBuffer[0][i + delay] = convLft[i];
						finalBuffer[1][i] = convRgt[i];
					}
				}
			}
			else
			{
				finalF = buffer.length;
				finalBuffer = new double[2][finalF];
				j = 0;
				k = 0;
				for (int i = 0; i < buffer.length; i++)
				{
					if (i % 2 == 0)
					{
						leftBuffer[j++] = buffer[i] * scaleFactor;
					}
					else
					{
						rightBuffer[k++] = buffer[i] * scaleFactor;
					}
				}
				for (int i = 0; i < numFrames; i++)
				{
					finalBuffer[0][i] = leftBuffer[i];
					finalBuffer[1][i] = rightBuffer[i];
				}
			}
			long sampleRate = wavFile.getSampleRate();
			WavFile writeFile = WavFile.newWavFile(this.soundFile, numChannels, finalF, wavFile.getValidBits(), sampleRate);

			// write audio data to wav file
			writeFile.writeFrames(finalBuffer, numFrames);
			writeFile.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
