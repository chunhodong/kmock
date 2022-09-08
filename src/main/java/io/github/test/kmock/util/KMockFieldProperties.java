package io.github.test.kmock.util;

import com.sun.nio.sctp.IllegalReceiveException;

import java.lang.reflect.Field;
import java.util.Arrays;

public class KMockFieldProperties {

    private Field[] fields;


    private KMockFieldProperties(Field[] fields) {
        this.fields = fields;

    }

    public static KMockFieldProperties.BuildMeBuilder builder() {
        return new KMockFieldProperties.BuildMeBuilder();
    }

    public static class BuildMeBuilder {
        private Field[] fields;


        BuildMeBuilder() {
        }

        public KMockFieldProperties.BuildMeBuilder fields(Field[] fields) {
            //null값체크
            //빈값여부체크
            this.fields = fields;
            return this;
        }

        public KMockFieldProperties build() {

            //빈값체크
            return new KMockFieldProperties(this.fields);
        }

        private void validateFields(Field[] fields){

            if(isDuplicateField(fields.length,fields))throw new IllegalArgumentException("not allowd duplicate filed");
            if(isDuplicatedAnnotation(fields)) throw new IllegalArgumentException("not allowd duplicate filed");

            //중복필드
            //어노테이션은 1개

        }

        private boolean isDuplicatedAnnotation(Field[] fields) {
            Arrays.stream(fields).map(field -> field.getAnnotations())
        }

        private boolean isDuplicateField(int srcSize,Field[] fields){
            return Arrays.stream(fields).map(field -> field.getDeclaringClass())
                    .distinct().count() == srcSize;

        }








    }

}
