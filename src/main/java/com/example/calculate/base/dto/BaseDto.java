package com.example.calculate.base.dto;

import com.example.calculate.utils.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

// 직렬화는 JVM 메모리에 상주 되어 있는 객체 데이터를 그대로 영속화 할 때 사용 된다.
// 시스템이 종료 되더라도 없어지지 않고 영속화되어 네트워크로 전송도 가능 하다.
// 서버가 다중화 되어 있고 세션 클러스터링 하는 환경에서 도메인 객체가 세션에 저장 된다면
// 해당 도메인 객체가 Serializable 인터페이스를 구현해야 정상적으로 세션을 저장/불러오기 할 수 있다
public class BaseDto implements Serializable {

    public <T> T toEntity(T src) {
        Field[] superFields = this.getClass().getSuperclass().getDeclaredFields();
        Field[] fields = this.getClass().getDeclaredFields();

        Field[] allFields = new Field[superFields.length + fields.length];
        Arrays.setAll(
                allFields,
                i -> (i < superFields.length ? superFields[i] : fields[i - superFields.length]));

        for (Field field : allFields) {
            Object value = null;

            //private Field일 경우 접근 허용
            field.setAccessible(true);

            try {
                // Field Value 참조
                value = field.get(this);
                if (StringUtils.isNotEmpty(value)) {
                    Field f = src.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    f.set(src, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {

            }
        }
        return src;
    }
}
