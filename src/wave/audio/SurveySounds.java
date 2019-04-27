package wave.audio;

import java.io.File;
import java.nio.file.Path;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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

	public SurveySounds(int heading, int elevation, Path soundPath)
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

	public void playAudio()
	{
		// @formatter:off
		this.sound = new PlaySound(
				this.headingAngle, 
				this.elevationAngle, 
				this.soundPath, 
				this.hrtf, 
				this.scaleFactor, 
				this.duration,
				this.isLoop);
		// @formatter:on
		Thread thread = new Thread(this.sound);
		thread.start();
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

	public boolean fadeStop()
	{
		boolean hasStopped = false;
		if (this.sound != null)
		{
			FadeEffect fade = new FadeEffect(this.sound, 3000);
			Thread thread = new Thread(fade);
			thread.start();
			hasStopped = true;
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

	public double heading;
	public double elevation;
	public Path soundToPlay;
	public double scaleFactor;
	public int dur;
	private int framePosition;
	private Clip clip;
	private HRTFData hrtf;
	private boolean isLoop;
	private boolean is3D;
	private File soundFile = SOUND_FILE;
	private boolean isStopped;

	public PlaySound(File soundFile, Path sound, HRTFData hrtf, double scaleFactor, int dur, boolean isLoop)
	{
		this.soundFile = soundFile;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		this.is3D = true;
	}

	public PlaySound(Path sound, HRTFData hrtf, double scaleFactor, int dur, boolean isLoop)
	{
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		this.is3D = false;
	}

	public PlaySound(double heading, double elevation, Path sound, HRTFData hrtf, double scaleFactor, int dur,
			boolean isLoop)
	{
		this.heading = heading;
		this.elevation = elevation;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		this.is3D = true;
	}

	public PlaySound(File soundFile, double heading, double elevation, Path sound, HRTFData hrtf, double scaleFactor,
			int dur, boolean isLoop)
	{
		this.soundFile = soundFile;
		this.heading = heading;
		this.elevation = elevation;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.isLoop = isLoop;
		this.is3D = true;
	}

	public void run()
	{
		processAudio();
		if (this.soundFile == null || this.soundToPlay == null || this.isStopped || this.clip == null)
		{
			return;
		}
		try
		{
			if (this.framePosition < this.clip.getFrameLength())
			{
				this.clip.setFramePosition(framePosition);
			}
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

	public Path getSoundToPlay()
	{
		return this.soundToPlay;
	}

	public void setPosition(int framePosition)
	{
		this.framePosition = framePosition;
	}

	public int getPosition()
	{
		int position = 0;
		if (this.clip != null)
		{
			position = this.clip.getFramePosition();
		}
		return position;
	}

	public void setGain(float gain)
	{
		if (this.clip != null)
		{
			if (this.clip.isOpen())
			{
				FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(gain);
			}
		}
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
			int finalF = 1;

			// construct final audio data array
			double[][] finalBuffer = new double[2][finalF];

			// Indexing for individual channels
			int j;
			int k;

			// Read frames into buffer from wav file
			wavFile.readFrames(buffer, numFrames);

			// Close the wavFile
			wavFile.close();

			if (this.is3D)
			{
				int aIndex;
				int eIndex;
				double scale;
				CipicHRTF indicesHrtf = new CipicHRTF(this.heading, this.elevation);
				for (int crossFade = 0; crossFade < 2; ++crossFade)
				{
					if (crossFade == 0)
					{
						aIndex = indicesHrtf.lowerIndex();
						eIndex = indicesHrtf.lowerElIndex();
						scale = indicesHrtf.lowerScale();
					}
					else
					{
						aIndex = indicesHrtf.higherIndex();
						eIndex = indicesHrtf.higherElIndex();
						scale = indicesHrtf.higherScale();
					}

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
					convLft = MathArrays.convolve(lft, leftBuffer);
					convRgt = MathArrays.convolve(rgt, rightBuffer);

					// final amount of frames
					finalF = delay + convLft.length;

					// construct final audio data array
					finalBuffer = new double[2][finalF];

					// delay channel based on azimuth
					if (crossFade == 0)
					{
						if (aIndex < 12)
						{
							for (int i = 0; i < finalF - delay - 1; i++)
							{
								finalBuffer[0][i] = (convLft[i] * scale);
								finalBuffer[1][i + delay] = (convRgt[i] * scale);
							}
						}
						else
						{
							for (int i = 0; i < finalF - delay - 1; i++)
							{
								finalBuffer[0][i + delay] = convLft[i] * scale;
								finalBuffer[1][i] = convRgt[i] * scale;
							}
						}
					}
					else
					{
						if (aIndex < 12)
						{
							for (int i = 0; i < finalF - delay - 1; i++)
							{
								finalBuffer[0][i] += convLft[i] * scale;
								finalBuffer[1][i + delay] += convRgt[i] * scale;
							}
						}
						else
						{
							for (int i = 0; i < finalF - delay - 1; i++)
							{
								finalBuffer[0][i + delay] += convLft[i] * scale;
								finalBuffer[1][i] += convRgt[i] * scale;
							}
						}
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
			WavFile writeFile = WavFile.newWavFile(this.soundFile, numChannels, finalF, wavFile.getValidBits(),
					sampleRate);

			// write audio data to wav file
			writeFile.writeFrames(finalBuffer, numFrames);
			writeFile.close();

			AudioInputStream aStream = AudioSystem.getAudioInputStream(this.soundFile);
			this.clip = AudioSystem.getClip();
			this.clip.open(aStream);
		}
		catch (Exception e)
		{

		}
	}
}
