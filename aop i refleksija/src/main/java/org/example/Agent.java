package org.example;

import org.example.di.DI;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Agent {
    public static void premain(String arg, Instrumentation inst) {
        System.err.println("<<Hello from Static Agent!>>");
        try {
            DI.getInstance().initClasses();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
