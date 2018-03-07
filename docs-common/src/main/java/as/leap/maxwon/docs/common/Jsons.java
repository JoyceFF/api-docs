package as.leap.maxwon.docs.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Jsons {
  private static Logger logger = LoggerFactory.getLogger(Jsons.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public Jsons() {
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  public static String objectToJSONStr(Object obj) {
    if(obj == null) {
      return null;
    } else {
      try {
        return objectMapper.writeValueAsString(obj);
      } catch (Exception var2) {
        logger.error("Failed objectToJSONStr object is : " + obj, var2);
        return null;
      }
    }
  }

  public static String objectToPrettyJSONStr(Object obj) {
    if(obj == null) {
      return null;
    } else {
      try {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      } catch (Exception var2) {
        logger.error("Failed objectToPrettyJSONStr object is : " + obj, var2);
        return null;
      }
    }
  }

  public static <T> T objectFromJSONStr(String str, JavaType javaType) {
    if(str != null && str.length() != 0) {
      try {
        return objectMapper.readValue(str, javaType);
      } catch (Exception var3) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static <T> T objectFromJSONStr(String str, Class<T> type) {
    if(str != null && str.length() != 0) {
      try {
        return objectMapper.readValue(str, type);
      } catch (Exception var3) {
        logger.error("Failed objectFromJSONStr jsonStr is : " + str + ", type is : " + type.getCanonicalName(), var3);
        return null;
      }
    } else {
      return null;
    }
  }

  public static <T> List<T> listFromJSONStr(String str, Class<T> type) {
    ArrayList list = new ArrayList();
    List ncs = (List)objectFromJSONStr(str, List.class);
    if(ncs != null && ncs.size() > 0) {
      Iterator var4 = ncs.iterator();

      while(var4.hasNext()) {
        Object nc = var4.next();
        Object item = objectFromJSONStr(objectToJSONStr(nc), type);
        if(item != null) {
          list.add(item);
        }
      }
    }

    return list;
  }

  public static Map<String, Object> objectToMap(Object obj) {
    return (Map)objectMapper.convertValue(obj, Map.class);
  }

  public static Map<String, Object> beanToMap(Object obj) {
    if(obj == null)
      return null;

    Map<String, Object> map = new HashMap<String, Object>();
    try{
      Field[] declaredFields = obj.getClass().getDeclaredFields();
      for (Field field : declaredFields) {
        int modifier = field.getModifiers();
        if (modifier == Modifier.PRIVATE) {
          field.setAccessible(true);
          map.put(field.getName(), field.get(obj));
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    return map;
  }

  public static <T> T mapToObject(Map map, Class<T> tClass) {
    return objectMapper.convertValue(map, tClass);
  }


  public static boolean isJSONValid(String JSON_STRING) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(JSON_STRING);
      return true;
    } catch (IOException e) {
      return false;
    }
  }


}
