package org.example.di;

import org.example.annotation.http.GET;
import org.example.annotation.http.POST;
import org.example.annotation.http.Path;

import java.lang.reflect.Method;
import java.util.HashMap;

public class DiRouts {

    private static DiRouts instance = null;
    private final HashMap<String, Method> routeWithGet = new HashMap<>();
    private final HashMap<String, Method> routWithPost = new HashMap<>();

    private static final Object mutex = new Object();
    private DiRouts(){}

    public static DiRouts getInstance(){
        DiRouts result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null) instance = result = new DiRouts();
            }
        }
        return result;
    }

    public void map(Class cla55) throws Exception{
        Method[] methods = cla55.getDeclaredMethods();
        for(Method method:methods){
            if(method.isAnnotationPresent(Path.class)){
                String path = method.getAnnotation(Path.class).path();
                if(method.isAnnotationPresent(GET.class)){
                    if(routeWithGet.get(path)!= null)
                        throw new Exception("Vec postoji takav path za get");
                    routeWithGet.put(path,method);
                }else if(method.isAnnotationPresent(POST.class))
                    if(routWithPost.get(path)!= null)
                        throw new Exception("Vec postoji takav path za post");
                    routWithPost.put(path,method);
            } else {
                if(method.isAnnotationPresent(GET.class) || method.isAnnotationPresent(POST.class)){
                    throw new Exception("No path found exception");
                }
            }
        }
    }
}
