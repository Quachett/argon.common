package uk.co.inc.argon.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JaxbContextCache {
    private Map<Class, JAXBContext> jaxbMap;
    private static JaxbContextCache ctx = null;
    
    private JaxbContextCache() {
        jaxbMap = Collections.synchronizedMap(new HashMap<Class, JAXBContext>());
    }
    
    public static JaxbContextCache getInstance() {
        if(ctx == null) {
            synchronized (JaxbContextCache.class) {
                if(ctx == null)
                    ctx = new JaxbContextCache();
            }
        }
        return ctx;
    }
    
    public JAXBContext getContext(Class clazz) throws JAXBException {
        if(!jaxbMap.containsKey(clazz)) {
            synchronized (jaxbMap) {
                if(!jaxbMap.containsKey(clazz)) {
                    JAXBContext jc = JAXBContext.newInstance(clazz);
                    jaxbMap.put(clazz, jc);
                }
            }
        }
        return jaxbMap.get(clazz);
    }
    
    public static void clearCache() {
        if(ctx != null) {
            synchronized (JaxbContextCache.class) {
                if(ctx != null)
                    ctx = null;
            }
        }
    }
}
