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
	public static final Path CLOSE = Paths.get("data", "audio", "close_thunder.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "medium_thunder.wav");
	public static final Path FAR = Paths.get("data", "audio", "far_thunder.wav");
	public static final Path HRTF = Paths.get("data", "hrtf", "hrir_final.mat");

	private HRTFData hrtf;
	private int aIndex;
	private int eIndex;
	private Path soundToPlay;
	private double scaleFactor;
	private int dur;

	public SurveySounds(int aIndex, int eIndex, Path soundToPlay, double scaleFactor, int dur)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = soundToPlay;
		this.scaleFactor = scaleFactor;
		this.dur = dur;

		try
		{
			this.hrtf = HRTFData.LoadHRTF(HRTF);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public void play()
	{
		Thread obj = new Thread(new PlaySound(aIndex, eIndex, soundToPlay, hrtf, scaleFactor, dur));
		obj.start();
	}
}

class PlaySound implements Runnable
{

	public volatile int aIndex;
	public volatile int eIndex;
	public volatile Path soundToPlay;
	public volatile double scaleFactor;
	public volatile int dur;
	private HRTFData hrtf;

	public PlaySound(int aIndex, int eIndex, Path sound, HRTFData hrtf, double scaleFactor, int dur)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.soundToPlay = sound;
		this.hrtf = hrtf;
		this.scaleFactor = scaleFactor;
		this.dur = dur;
	}

	public void run()
	{
		try
		{
			// get paths of the wav and mat file
			File sourceSound = soundToPlay.toFile();

			AudioInputStream aStream;
			Clip clip = AudioSystem.getClip();

			if (aIndex == -1 && eIndex == -1)
			{
				// load AudioInputStream from wav file
				aStream = getAudioInputStream(sourceSound);

				// play back the AudioInputStream
				clip.open(aStream);
				clip.start();

				Thread.sleep(this.dur);
				clip.close();
			}

			else
			{
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

				lft = this.hrtf.getLeftData(aIndex, eIndex);
				rgt = hrtf.getRightData(aIndex, eIndex);

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
						leftBuffer[j++] = buffer[i] * scaleFactor;
					else rightBuffer[k++] = buffer[i] * scaleFactor;
				}

				// perform convolution
				convLft = MathArrays.convolve(lft, leftBuffer);
				convRgt = MathArrays.convolve(rgt, rightBuffer);

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

				WavFile writeFile = WavFile.newWavFile(sound, wavFile.getNumChannels(), finalF, wavFile.getValidBits(),
						wavFile.getSampleRate());

				// write audio data to wav file
				writeFile.writeFrames(finalBuffer, numFrames);

				// load AudioInputStream from wav file
				aStream = getAudioInputStream(sound);

				// play back the AudioInputStream
				clip.open(aStream);
				clip.start();

				Thread.sleep(dur);
				clip.close();

				writeFile.close();
			}

		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
}
