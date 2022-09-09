package io.github.test.kmock.util;

import io.github.test.kmock.annotation.KMockBean;
import io.github.test.kmock.annotation.KSpyBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 테스트클래스의 필드값 저장
 */
public class KMockFieldStore {

    private Field[] fields;


    private KMockFieldStore(Field[] fields) {
        this.fields = fields;

    }

    /**
     * 클래스 어노테이션포함여부 확인
     * @param aClass
     * @param annotation
     * @return
     */
    public boolean isContainsAnnotation(Class<?> aClass,Class<? extends Annotation> annotation) {
        return Arrays.stream(this.fields)
                .filter(field -> field.getType().equals(aClass))
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations()).anyMatch(a->a.annotationType().equals(annotation)))
                .count() > 0;

    }

    public static KMockFieldStore.BuildMeBuilder builder() {
        return new KMockFieldStore.BuildMeBuilder();
    }

    public static class BuildMeBuilder {
        private Field[] fields;


        BuildMeBuilder() {

        }

        /**
         * 필드값 설정
         * @param fields
         * @return
         */
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

        /**
         * 어노테이션필드 필터링, KSpyBean,KMockBean 어노테이션 포함된 필드만 필터링
         * @param fields
         * @return 필터링된 필드목록
         */
        private List<Field> filteredFields(Field[] fields){
            return Arrays.stream(fields)
                    .filter(field -> field.getDeclaredAnnotations() != null)
                    .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                            .anyMatch(annotation -> annotation.annotationType().equals(KSpyBean.class) || annotation.annotationType().equals(KMockBean.class)))
                    .collect(Collectors.toList());
        }

        /**
         * 필드검사
         * @param fields
         */
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

        /**
         * 필드 어노테이션 검사, KMockBean,KSpyBean 어노테이션만 허용
         * @param annotations 어노테이션목록
         * @return
         */
        private boolean isContainInValidAnnotaion(Annotation[] annotations){
            long inValidAnnotaionCount = Arrays.stream(annotations).filter(annotation -> !annotation.annotationType().equals(KMockBean.class) && !annotation.annotationType().equals(KSpyBean.class))
                    .count();


            boolean hasKmockAnnotaion = Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType().equals(KMockBean.class));
            boolean hasKspyAnnotation = Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType().equals(KSpyBean.class));
            return ( hasKmockAnnotaion && hasKspyAnnotation ) || inValidAnnotaionCount > 0;
        }

        /**
         * 중복된 필드값검사
         * @param srcSize 입력값 크기
         * @param fields  필드목록
         * @return
         */
        private boolean isDuplicateField(int srcSize,List<Field> fields){
            long cc = fields.stream().map(field -> field.getType())
                    .distinct().count();
            return cc != srcSize;

        }








    }

}
