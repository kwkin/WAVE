package wave.infrastructure.util;

import java.nio.ByteBuffer;

public class UnitToString
{
	public String colorDescription(int color)
	{
		byte[] bytes = ByteBuffer.allocate(4).putInt(color).array();
		StringBuilder message = new StringBuilder("A: ");
		message.append((byte) bytes[0] & 0xFF);
		message.append(" R: ");
		message.append((byte) bytes[1] & 0xFF);
		message.append(" G: ");
		message.append((byte) bytes[2] & 0xFF);
		message.append(" B: ");
		message.append((byte) bytes[3] & 0xFF);
		return message.toString();
	}
}
