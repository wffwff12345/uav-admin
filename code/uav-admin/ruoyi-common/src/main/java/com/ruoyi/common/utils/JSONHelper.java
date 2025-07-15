package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param obj 对象
     * @param <T> 对象类型
     * @return 字符串
     * @throws JsonProcessingException JSON 处理异常
     */
    public static <T> String stringify(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将 JSON 字符串转换为对象
     *
     * @param json JSON 字符串
     * @param clazz 对象类
     * @param <T> 对象类型
     * @return 对象
     * @throws JsonProcessingException JSON 处理异常
     */
    public static <T> T parse(String json, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为对象
     *
     * @param json JSON 字符串
     * @param reference 对象引用
     * @param <T> 对象类型
     * @return 对象
     * @throws JsonProcessingException JSON 处理异常
     */
    public static <T> T parse(String json, TypeReference<T> reference) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, reference);
    }
}
