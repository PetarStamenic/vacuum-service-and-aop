package org.example.di;

import java.util.HashMap;

public class DiContainer {
    @SuppressWarnings("rawtypes")
    private final HashMap<String,Class> qualifier = new HashMap<>();
    private static final Object mutex = new Object();

    private static DiContainer instance = null;
    private DiContainer(){}

    public static DiContainer getInstance(){
        DiContainer result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null) instance = result = new DiContainer();
            }
        }
        return result;
    }

    public HashMap<String, Class> getQualifiers() {
        return qualifier;
    }
}
