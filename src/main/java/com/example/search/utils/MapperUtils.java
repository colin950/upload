package com.example.search.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.IOException;

public class MapperUtils {
    private MapperUtils() { throw new IllegalStateException("Utility class");}

    public static final ModelMapper mapper = new ModelMapper();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapToTarget(Object source, Class<T> targetClass) { return mapper.map(source, targetClass); }
    //TODO 뭔지 알아보자 설정 값
    static {
        // mapper 설정
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // objectMapper 설정
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * object > json return
     */
    public static String toJson(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }

    /**
     * json > object return
     * @param json
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> type) throws IOException {
        if (json == null || type == null) {
            return null;
        }
        return objectMapper.readValue(json, type);
    }
}
