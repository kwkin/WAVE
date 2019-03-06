package wave.infrastructure.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;

public class WaveUtil
{
	public static <T> T deepCopy(T object, Class<T> classType)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(classType);
			JAXBElement<T> contentObject = new JAXBElement<T>(new QName(classType.getSimpleName()), classType, object);
			JAXBSource source = new JAXBSource(jaxbContext, contentObject);
			return jaxbContext.createUnmarshaller().unmarshal(source, classType).getValue();
		}
		catch (JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T object)
	{
		{
			if (object == null)
			{
				throw new RuntimeException("Unable to determine class.");
			}
			return deepCopy(object, (Class<T>) object.getClass());
		}
	}
}
