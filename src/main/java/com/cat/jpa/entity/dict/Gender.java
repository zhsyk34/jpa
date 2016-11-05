package com.cat.jpa.entity.dict;

import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

public enum Gender {
    MALE(1, "男"), FEMALE(2, "女"), OTHER(3, "其他");

    private static final Map<Integer, Gender> INDEX_MAP;
    private static final Map<String, Gender> SEX_MAP;

    static {
        INDEX_MAP = new HashMap<>();
        SEX_MAP = new HashMap<>();
        for (Gender gender : values()) {
            INDEX_MAP.put(gender.getIndex(), gender);
            SEX_MAP.put(gender.getSex(), gender);
        }
    }

    @Getter
    private final int index;
    @Getter
    private final String sex;

    Gender(int index, String sex) {
        this.index = index;
        this.sex = sex;
    }

    public static Gender of(Integer index) {
        return index == null || index < 0 ? null : INDEX_MAP.get(index);
    }

    public static Gender of(String sex) {
        return sex == null || sex.isEmpty() ? null : SEX_MAP.get(sex);
    }

    @Override
    public String toString() {
        return this.getSex().toLowerCase();
    }

    @Converter
    public static class GenderConverter implements AttributeConverter<Gender, String> {
        @Override
        public String convertToDatabaseColumn(Gender gender) {
            return gender.getSex();
        }

        @Override
        public Gender convertToEntityAttribute(String string) {
            return Gender.of(string);
        }
    }
}
