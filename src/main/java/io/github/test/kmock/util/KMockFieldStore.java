package io.github.test.kmock.util;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KMockFieldStore {

    private Field[] fields;


    private KMockFieldStore(Field[] fields) {
        this.fields = fields;

    }

    public boolean isContainsAnnotation(Class<?> aClass,Class<? extends Annotation> annotation) {
        Class<?> findClass = Arrays.stream(this.fields)
                .filter(field -> field.getDeclaringClass().equals(aClass))
                .findFirst()
                .map(field -> field.getDeclaringClass())
                .orElseThrow(() -> new IllegalArgumentException("no matched test class field"));
        return Arrays.stream(findClass.getDeclaredAnnotations()).filter(a -> a.annotationType().equals(annotation))
                .count() > 0;

    }

    public static KMockFieldStore.BuildMeBuilder builder() {
        return new KMockFieldStore.BuildMeBuilder();
    }

    public static class BuildMeBuilder {
        private Field[] fields;


        BuildMeBuilder() {

        }

        public KMockFieldStore.BuildMeBuilder fields(Field[] fields) {
            if(fields == null)return this;
            List<Field> filteredFields = filteredFields(fields);
            validateFields(filteredFields);


            this.fields = filteredFields.toArray(new Field[filteredFields.size()]);
            return this;
        }

        public KMockFieldStore build() {
            return new KMockFieldStore(this.fields);
        }

        private List<Field> filteredFields(Field[] fields){
            return Arrays.stream(fields)
                    .filter(field -> field.getDeclaredAnnotations() != null)
                    .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                            .anyMatch(annotation -> annotation.annotationType().equals(KSpyBean.class) || annotation.annotationType().equals(KMockBean.class)))
                    .collect(Collectors.toList());
        }

        private void validateFields(List<Field> fields){
            if(fields.size() == 0)return;

            if(isDuplicateField(fields.size(),fields))throw new IllegalArgumentException("not allowd duplicate field");
            if(isInValidAnnotation(fields)) throw new IllegalArgumentException("not allowd invalid field");

        }

        private boolean isInValidAnnotation(List<Field> fields) {
            return fields.stream()
                    .filter(field -> isContainInValidAnnotaion(field.getAnnotations()))
                    .count() > 0;
        }

        private boolean isContainInValidAnnotaion(Annotation[] annotations){
            long inValidAnnotaionCount = Arrays.stream(annotations).filter(annotation -> !annotation.annotationType().equals(KMockBean.class) && !annotation.annotationType().equals(KSpyBean.class))
                    .count();


            boolean hasKmockAnnotaion = Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType().equals(KMockBean.class));
            boolean hasKspyAnnotation = Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType().equals(KSpyBean.class));
            return ( hasKmockAnnotaion && hasKspyAnnotation ) || inValidAnnotaionCount > 0;
        }

        private boolean isDuplicateField(int srcSize,List<Field> fields){
            long cc = fields.stream().map(field -> field.getType())
                    .distinct().count();
            return cc != srcSize;

        }








    }

}
