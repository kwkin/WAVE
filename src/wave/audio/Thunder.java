package wave.audio;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.commons.math3.util.MathArrays;

import wave.audio.wav.WavFile;
import wave.infrastructure.handlers.HRTFData;

public class Thunder
{
	public static final Path CLOSE = Paths.get("data", "audio", "thunder_close.wav");
	public static final Path MEDIUM = Paths.get("data", "audio", "thunder_medium.wav");
	public static final Path FAR = Paths.get("data", "audio", "thunder_far.wav");

	private int aIndex;
	private int eIndex;
	private int dist;
	private Path sound;

	public Thunder(int aIndex, int eIndex, int dist, Path sound)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.dist = dist;
		this.sound = sound;
	}

	public void play()
	{

		// perform calculations for aIndex and eIndex here

		// determine which sound to play here

		Thread obj = new Thread(new playThunder(this.aIndex, this.eIndex, this.dist, this.sound));
		obj.start();
	}
}

class playThunder implements Runnable
{
	public static final Path HRTF = Paths.get("data", "hrtf", "hrir_final.mat");

	public volatile int aIndex;
	public volatile int eIndex;
	public volatile int dist;
	public volatile Path wavePath;

	public playThunder(int aIndex, int eIndex, int dist, Path sound)
	{
		this.aIndex = aIndex;
		this.eIndex = eIndex;
		this.dist = dist;
		this.wavePath = sound;
	}

	public void run()
	{
		try
		{
			// get paths of the wav and mat file
			Path hrtfPath = HRTF;
			File sourceSound = this.wavePath.toFile();

			// used to play back sound during runtime
			File sound;
			AudioInputStream aStream;
			Clip clip = AudioSystem.getClip();
			;

			// load hrtf data from .mat file
			HRTFData hrtf = HRTFData.LoadHRTF(hrtfPath);

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
			int j = 0;
			int k = 0;

			// 0 to 1 scale factor for volume
			double scaleFactor = 0.5;

			// sound to be played
			sound = new File("sound.wav");

			// Read frames into buffer from wav file
			wavFile.readFrames(buffer, numFrames);

			// Close the wavFile
			wavFile.close();

			lft = hrtf.getLeftData(aIndex, eIndex);
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

			TimeUnit.SECONDS.sleep(1);
			int position = clip.getFramePosition();
			System.out.println(position);

			writeFile.close();

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}