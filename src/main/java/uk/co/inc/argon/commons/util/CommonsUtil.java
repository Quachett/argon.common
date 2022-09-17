package uk.co.inc.argon.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.co.inc.argon.commons.exceptions.HttpException;


public class CommonsUtil {
    public static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String PIPE = "||";
    public static final String NEW = "NEW";
    public static final String YES = "Yes";
    public static final String NO = "No";
	private static final Gson gson = new GsonBuilder()
			.setDateFormat(ISO_8601_DATE_FORMAT)
			.setPrettyPrinting().create();
	
    public static String createAttributes(List<String> attributes) {
        //return attributes.stream().collect(Collectors.joining(SynapseConstants.COMMA));
    	return StringUtils.join(attributes,SynapseConstants.COMMA + " ");
    }
    
    public static String getLineOfQs(int num) {
        return Joiner.on(", ").join(Iterables.limit(Iterables.cycle("?"), num));
    }
    
    public static String arrayRangeToString(Object[] obj, int start, int end) {
    	int i;
    	String result = "";
    	for(i=0;i<end;i++) {
    		if(i<end-1)
    			result += obj[i] + ", ";
    		else
    			result += obj[i];
    	}
        return "[" + result +"]";
    }
   
    public static <T> String performMarshaling(T pojo) throws HttpException {           
        try {
            Marshaller jaxbMarshaller = JaxbContextCache.getInstance().getContext(pojo.getClass()).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            
            jaxbMarshaller.marshal(pojo, os);                       
            os.flush();
            
            String xml = new String(os.toByteArray(), StandardCharsets.UTF_8);
            RegExUtils.removeAll(xml, StringUtils.LF);
            
            //System.out.println("======================================================");
            System.out.println(xml);
            System.out.println("======================================================\n\n");
            
            return xml;
        }
        catch (JAXBException | IOException e) {
            throw new HttpException(e.toString(), 500);
        }
    }
    
    public static <T> T performUnmarshalling(ByteArrayInputStream bais, Class<T> clazz) throws HttpException {
        Unmarshaller unmarshaller = null;
        
        try {
            unmarshaller = JaxbContextCache.getInstance().getContext(clazz).createUnmarshaller();
            T pojo = clazz.cast(unmarshaller.unmarshal(bais));
            return pojo;
        }catch (JAXBException e) {
            throw new HttpException(e.toString(), 500);
        }
    }
    
    public static <T> T[] swap(T[] t, int i, int j) {
		
		T temp = t[j];
		t[j] = t[i];
		t[i] = temp;
		
		return t;
	}

	public static Gson getGson() {
		return gson;
	}
}
