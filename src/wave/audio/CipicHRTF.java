package wave.audio;

import wave.infrastructure.util.PointUtil;

public class CipicHRTF
{
	private double headingAngle;
	private double elevationAngle;
	
	private int lowerIndex;
	private int higherIndex;
	private int lowerElIndex;
	private int higherElIndex;
	private double lowerScale;
	private double higherScale;
	
	public CipicHRTF(double headingAngle, double elevationAngle)
	{
		this.headingAngle = headingAngle;
		this.elevationAngle = elevationAngle;
		this.normalizeHeading();
		this.lowerIndex = this.lowerAIndex(this.headingAngle);
		this.higherIndex = this.higherAIndex(this.headingAngle);
		if (this.lowerIndex == this.higherIndex && this.lowerElIndex == this.higherElIndex)
		{
			this.lowerScale = 0.5;
			this.higherScale = 0.5;
		}
		else if (this.lowerScale == 0 && this.higherScale == 0)
		{
			if (this.headingAngle == 80)
			{
				this.lowerScale = 1.0;
			}
			else
			{
				this.higherScale = 1.0;
			}
		}
	}
	
	public int lowerElIndex()
	{
		return this.lowerElIndex;
	}
	
	public int higherElIndex()
	{
		return this.higherElIndex;
	}
	
	public double lowerScale()
	{
		return this.lowerScale;
	}
	
	public double higherScale()
	{
		return this.higherScale;
	}
	
	public int lowerIndex()
	{
		return this.lowerIndex;
	}
	
	public int higherIndex()
	{
		return this.higherIndex;
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
	
	private int lowerAIndex(double heading)
	{
		double index = 0;
		
		double radHeading = Math.toRadians(this.headingAngle);
		double normalized = Math.toDegrees(Math.atan2(Math.sin(radHeading), Math.cos(radHeading)));
		
		if ((normalized >= 100 || normalized <= -100))
		{
			if (normalized > 90)
			{
				normalized = 180 - normalized;
			}
			else
			{
				normalized = -180 - normalized;
			}			
			if (this.elevationAngle < 90)
			{
				this.elevationAngle = 180 - this.elevationAngle;
			}
		}
		else if (this.elevationAngle > 90)
		{
			this.elevationAngle = 180 - this.elevationAngle;
		}
		this.lowerElIndex = (int) ((this.elevationAngle + 45) / 5.625);
		
		if (normalized < -80)
		{
			index = 0;
		}
		else if (normalized >= -80 && normalized <= -65)
		{
			index = PointUtil.map(normalized, -80, -65, 0, 1);
		}
		else if (normalized >= -65 && normalized <= -45)
		{
			index = PointUtil.map(normalized, -65, -45, 1, 3);
		}
		else if (normalized >= -45 && normalized <= 45)
		{
			index = PointUtil.map(normalized, -45, 45, 3, 21);
		}
		else if (normalized >= 45 && normalized <= 65)
		{
			index = PointUtil.map(normalized, 45, 65, 21, 23);
		}
		else if (normalized >= 65 && normalized <= 80)
		{
			index = PointUtil.map(normalized, 65, 80, 23, 24);
		}
		else if (normalized > 80)
		{
			index = PointUtil.map(normalized, 100, 80, 24, 25);
		}
		this.lowerScale = Math.ceil(index) - index;
		
		index = Math.floor(index);
		if (index < 0)
		{
			index = 0;
		}
		else if (index > 24)
		{
			index = 24;
		}
	    return (int) index;
	}
	
	private int higherAIndex(double heading)
	{
		double index = 0;
		
		double radHeading = Math.toRadians(this.headingAngle);
		double normalized = Math.toDegrees(Math.atan2(Math.sin(radHeading), Math.cos(radHeading)));
		
		if ((normalized >= 90 || normalized <= -90))
		{
			if (normalized > 90)
			{
				normalized = 180 - normalized;
			}
			else
			{
				normalized = -180 - normalized;
			}			
			if (this.elevationAngle < 90)
			{
				this.elevationAngle = 180 - this.elevationAngle;
			}
		}
		else if (this.elevationAngle > 90)
		{
			this.elevationAngle = 180 - this.elevationAngle;
		}
		this.higherElIndex = (int) ((this.elevationAngle + 45) / 5.625);
		
		if (heading < -80)
		{
			index = 0;
		}
		else if (heading >= -80 && heading <= -65)
		{
			index = PointUtil.map(heading, -80, -65, 0, 1);
		}
		else if (heading >= -65 && heading <= -45)
		{
			index = PointUtil.map(heading, -65, -45, 1, 3);
		}
		else if (heading >= -45 && heading <= 45)
		{
			index = PointUtil.map(heading, -45, 45, 3, 21);
		}
		else if (heading >= 45 && heading <= 65)
		{
			index = PointUtil.map(heading, 45, 65, 21, 23);
		}
		else if (heading >= 65 && heading <= 80)
		{
			index = PointUtil.map(heading, 65, 80, 23, 24);
		}
		else if (heading > 80)
		{
			index = PointUtil.map(this.headingAngle, 100, 80, 24, 25);
		}
		this.higherScale = index - Math.floor(index);
		
		index = Math.ceil(index);
		if (index < 0)
		{
			index = 0;
		}
		else if (index > 24)
		{
			index = 24;
		}
	    return (int) index;
	}
	
	private void normalizeHeading()
	{
		double radHeading = Math.toRadians(this.headingAngle);
		double normalized = Math.toDegrees(Math.atan2(Math.sin(radHeading), Math.cos(radHeading)));
		this.headingAngle = normalized;
	}
	
	public static void main(String[] args)
	{
		CipicHRTF indicesHrtf = new CipicHRTF(90, 0);
		System.out.println("Lower Index: " + indicesHrtf.lowerIndex);
		System.out.println("Higher Index: " + indicesHrtf.higherIndex);
		System.out.println("Lower Elevation Index: " + indicesHrtf.lowerElIndex);
		System.out.println("Higher Elevation Index: " + indicesHrtf.higherElIndex);
		System.out.println("Lower Scale: " + indicesHrtf.lowerScale);
		System.out.println("Higher Scale: " + indicesHrtf.higherScale);
	}
}
