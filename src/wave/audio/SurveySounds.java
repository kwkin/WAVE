package wave.audio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.math3.util.MathArrays;

import wave.audio.wav.WavFile;
import wave.infrastructure.handlers.HRTFData;

import javax.sound.sampled.*;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class SurveySounds
{
	public static final Path RAIN_0 = Paths.get("data", "audio", "rain_light_fade.wav");
	public static final Path RAIN_1 = Paths.get("data", "audio", "rain_medium_fade.wav");
	public static final Path RAIN_2 = Paths.get("data", "audio", "rain_heavy_fade.wav");

	public static final Path THUNDER_0 = Paths.get("data", "audio", "thunder_close.wav");
	public static final Path THUNDER_1 = Paths.get("data", "audio", "thunder_medium.wav");
	public static final Path THUNDER_2 = Paths.get("data", "audio", "thunder_far.wav");

	public static final Path WIND_0 = Paths.get("data", "audio", "wind_lightest_fade.wav");
	public static final Path WIND_1 = Paths.get("data", "audio", "wind_light_fade.wav");
	public static final Path WIND_2 = Paths.get("data", "audio", "wind_medium_fade.wav");
	public static final Path WIND_3 = Paths.get("data", "audio", "wind_heavy_fade.wav");
	public static final Path WIND_4 = Paths.get("data", "audio", "wind_heaviest_fade.wav");
	
	public static final Path HRTF = Paths.get("data", "hrtf", "hrir_final.mat");

	private HRTFData hrtf;
	private int aIndex;
	private int eIndex;
	private Path soundToPlay;
	private double scaleFactor;
	private int dur;
	private double speed;
	private PlaySound sound;

	public SurveySounds(int aIndex, int eIndex, Path soundToPlay, double scaleFactor)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = soundToPlay;
		this.scaleFactor = scaleFactor;
		this.speed = 1;

		try
		{
			this.hrtf = HRTFData.LoadHRTF(HRTF);

			File sourceSound = soundToPlay.toFile();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sourceSound);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			this.dur = (int) ((frames + 0.0) / format.getFrameRate() * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public SurveySounds(int aIndex, int eIndex, Path soundToPlay, double scaleFactor, double speed)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = soundToPlay;
		this.scaleFactor = scaleFactor;
		this.speed = speed;

		try
		{
			this.hrtf = HRTFData.LoadHRTF(HRTF);

			File sourceSound = soundToPlay.toFile();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sourceSound);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			this.dur = (int) ((frames + 0.0) / format.getFrameRate() * 1000);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public SurveySounds(int aIndex, int eIndex, Path soundToPlay, double scaleFactor, int dur)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = soundToPlay;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.speed = 1;

		try
		{
			this.hrtf = HRTFData.LoadHRTF(HRTF);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	public double getSpeed()
	{
		return this.speed;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public void play()
	{
		if (sound != null)
		{
			this.sound.stop();
		}
		this.sound = new PlaySound(aIndex, eIndex, soundToPlay, hrtf, scaleFactor, dur, speed);
		Thread obj = new Thread(this.sound);
		obj.start();
	}
	
	public void stop()
	{
		if (this.sound != null)
		{
			this.sound.stop();
		}
	}
}

class PlaySound implements Runnable
{

	public volatile int aIndex;
	public volatile int eIndex;
	public volatile Path soundToPlay;
	public volatile double scaleFactor;
	public volatile int dur;
	public volatile double speed;
	private Clip clip; 
	private HRTFData hrtf;

	public PlaySound(int aIndex, int eIndex, Path sound, HRTFData hrtf, double scaleFactor, int dur, double speed)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
		this.speed = speed;
	}

	public void run()
	{
		try
		{
			// get paths of the wav and mat file
			File sourceSound = soundToPlay.toFile();

			AudioInputStream aStream;
			this.clip = AudioSystem.getClip();

			// used to play back sound during runtime
			File sound;

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

			// sound to be played
			sound = new File("sound.wav");

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
				for (int i = 0; i < (buffer.length - 1) / speed; i++)
				{
					int index = (int) (i * speed);
					if (i % 2 == 0)
					{
						leftBuffer[j++] = buffer[index] * scaleFactor;
					}
					else
					{
						rightBuffer[k++] = buffer[index] * scaleFactor;
					}
				}
				for (int i = 0; i < numFrames; i++)
				{
					finalBuffer[0][i] = leftBuffer[i];
					finalBuffer[1][i] = rightBuffer[i];
				}
			}

			long sampleRate = wavFile.getSampleRate();
			WavFile writeFile = WavFile.newWavFile(sound, numChannels, finalF, wavFile.getValidBits(), sampleRate);

			// write audio data to wav file
			writeFile.writeFrames(finalBuffer, numFrames);

			// load AudioInputStream from wav file
			aStream = getAudioInputStream(sound);

			// play back the AudioInputStream
			this.clip.open(aStream);
			this.clip.start();

			Thread.sleep(dur);
			this.clip.close();

			writeFile.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		if (this.clip != null)
		{
			this.clip.close();
			this.clip.stop();
			this.clip.drain();
			this.clip.flush();
		}
	}
}
