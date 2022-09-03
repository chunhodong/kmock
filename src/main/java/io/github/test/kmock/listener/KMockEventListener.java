package io.github.test.kmock.listener;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class KMockEventListener implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        environment.getActiveProfiles();




        //현재패키지내에있는 WEBMVC붙은 클래스 찾기
        //클래스의 어노테이션필드 빈으로 저장

        Set<Class> r = getClasses("io.github.test.kmock.controller");
        //event.getApplicationContext().getBeanFactory().
    }




    private static Set<Class> getClasses(String packageName){
        Set<Class> classes = new HashSet<Class>();
        String packageNameSlash = "./" + packageName.replace(".", "/");
        URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlash);
        if(directoryURL == null){
            System.err.println("Could not retrive URL resource : " + packageNameSlash);
            return null;
        }

        String directoryString = directoryURL.getFile();
        if(directoryString == null){
            System.err.println("Could not find directory for URL resource : " + packageNameSlash);
            return null;
        }

        File directory = new File(directoryString);
        if(directory.exists()){
            String[] files = directory.list();
            for(String fileName : files){
                if(fileName.endsWith(".class")){
                    fileName = fileName.substring(0, fileName.length() - 6);  // remove .class
                    try{
                        Class c = Class.forName(packageName + "." + fileName);
                        if(!Modifier.isAbstract(c.getModifiers())) // add a class which is not abstract
                            classes.add(c);
                    } catch (ClassNotFoundException e){
                        System.err.println(packageName + "." + fileName + " does not appear to be a valid class");
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println(packageName + " does not appear to exist as a valid package on the file system.");
        }

        return classes;
    }
}
