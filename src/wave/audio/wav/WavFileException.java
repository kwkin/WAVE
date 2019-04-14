package wave.audio.wav;

public class WavFileException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3930408536211417822L;

	public WavFileException()
	{
		super();
	}

	public WavFileException(String message)
	{
		super(message);
	}

	public WavFileException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WavFileException(Throwable cause) 
	{
		super(cause);
	}
}

