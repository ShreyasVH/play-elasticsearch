package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final ObjectMapper objMapper = new ObjectMapper();

    public static <T> T convertObject(Object from, Class<T> to) {
        return objMapper.convertValue(from, to);
    }
}
