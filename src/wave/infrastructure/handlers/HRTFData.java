package wave.infrastructure.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

public class HRTFData
{
	public static String HRIR_L = "hrir_l";
	public static String HRIR_R = "hrir_r";
	public static String ITD = "ITD";

	private Map<String, MLArray> matMap;
	private int numAzimuths;
	private int numElevations;
	private int numSamples;

	public static HRTFData LoadHRTF(Path path) throws IOException
	{
		return LoadHRTF(path.toFile());
	}

	public static HRTFData LoadHRTF(File file) throws IOException
	{
		MatFileReader matFile = new MatFileReader(file);
		HRTFData data = new HRTFData();
		data.matMap = matFile.getContent();

		MLArray matArray = (MLArray) data.matMap.get(HRIR_L);
		int[] dims = matArray.getDimensions();
		data.numAzimuths = dims[0];
		data.numElevations = dims[1];
		data.numSamples = dims[2];
		return data;
	}

	public double[] getLeftData(int azimuthIndex, int elevationIndex)
	{
		MLDouble matDouble = (MLDouble) this.matMap.get(HRIR_L);
		double[][] hrir_l = matDouble.getArray();

		double[] values = new double[numSamples];
		for (int index = 0, indexSamples = elevationIndex; index < numSamples; ++index, indexSamples += this.numElevations)
		{
			values[index] = hrir_l[azimuthIndex][indexSamples];
		}
		return values;
	}

	public double[] getRightData(int azimuthIndex, int elevationIndex)
	{
		MLDouble matDouble = (MLDouble) this.matMap.get(HRIR_R);
		double[][] hrir_l = matDouble.getArray();

		double[] values = new double[numSamples];
		for (int index = 0, indexSamples = elevationIndex; index < numSamples; ++index, indexSamples += this.numElevations)
		{
			values[index] = hrir_l[azimuthIndex][indexSamples];
		}
		return values;
	}

	public double getDelay(int azimuthIndex, int elevationIndex)
	{
		MLDouble matDouble = (MLDouble) this.matMap.get(ITD);
		double[][] itd = matDouble.getArray();

		double delay = itd[azimuthIndex][elevationIndex];

		return delay;
	}

	public int getNumAzimuths()
	{
		return this.numAzimuths;
	}

	public int getNumElevations()
	{
		return this.numElevations;
	}

	public int getNumSamples()
	{
		return this.numSamples;
	}
}
