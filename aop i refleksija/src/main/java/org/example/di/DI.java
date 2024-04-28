package org.example.di;

import org.example.annotation.spring.*;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DI {
    private static DI instance = null;
    private final HashMap<String, Object> initClasses = new HashMap<>();
    private final HashMap<String,Object> initController = new HashMap<>();
    private static final Object mutex = new Object();
    private DI(){}

    public static DI getInstance(){
        DI result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null) instance = result = new DI();
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public void initClasses() throws Exception{
        for(Class cla55:listAllClasses()){
            if(cla55.isAnnotationPresent(Qualifier.class) && (cla55.isAnnotationPresent(Bean.class) || cla55.isAnnotationPresent(Component.class) || cla55.isAnnotationPresent(Service.class))){
                String str = ((Qualifier)cla55.getAnnotation(Qualifier.class)).value();
                if(DiContainer.getInstance().getQualifiers().get(str) == null){
                    DiContainer.getInstance().getQualifiers().put(str,cla55);
                }else {
                    throw new Exception("Class containing same qualifier already exists: "+cla55.getName());
                }
            }

            if(cla55.isAnnotationPresent(Bean.class) && ((Bean) cla55.getAnnotation(Bean.class)).singleton() || cla55.isAnnotationPresent(Service.class)){
                initClasses.put(cla55.getName(),cla55.getConstructor().newInstance());
            }

            if (cla55.isAnnotationPresent(Controller.class)) {
                DiRouts.getInstance().map(cla55);
                initController.put(cla55.getName(), cla55.getConstructor().newInstance());
                initClasses.put(cla55.getName(), cla55.getConstructor().newInstance());
            }
        }

        for (Object object:initClasses.values()){
            autoWire(object);
        }
    }

    private void autoWire(Object object) throws Exception{
        Class cla55 = object.getClass();
        Field[] fields = cla55.getDeclaredFields();
        for (Field field:fields){
            if(field.isAnnotationPresent(Autowired.class)){
                if(!field.getType().isPrimitive() && !field.getType().isInterface()){
                    setValue(field,object,false);
                }
                else if (field.getType().isInterface() && field.isAnnotationPresent(Qualifier.class)){
                    setValue(field,object,true);
                }
            }
        }
    }

    private void setValue(Field field,Object object,boolean isInterface)throws Exception{
        boolean details = field.getAnnotation(Autowired.class).details();
        Class cl;
        if(isInterface){
            String val = field.getAnnotation(Qualifier.class).value();
            if(DiContainer.getInstance().getQualifiers().get(val)==null){
                throw new Exception("NO BEANZZZ for qualifier"+field.getName());
            }
            cl = DiContainer.getInstance().getQualifiers().get(val);
        }else {
            cl = field.getType();
        }
        Object instance = inst(cl);
        if(instance != null){
            field.setAccessible(true);
            field.set(object,instance);
            if(details){
                System.out.println("===================="+cl.getName()+"===================="+field.getName()+"====================");
                System.out.println(field.getName() + "\n" + "in: " + object.getClass().getName() + "\non: " + LocalDateTime.now() + "\nwith id: " + instance.hashCode());
                System.out.println("--------------------"+cl.getName()+"--------------------"+field.getName()+"--------------------");
            }
            autoWire(instance);
        }
    }

    private Object inst(Class cla55) throws Exception{
        if(cla55.isAnnotationPresent(Bean.class)){
            boolean singleton = ((Bean)cla55.getAnnotation(Bean.class)).singleton();
            if(singleton) {
                return initClasses.get(cla55.getName());
            } else {
                return cla55.getConstructor().newInstance();
            }
        }
        if(cla55.isAnnotationPresent(Service.class)){
            return initClasses.get(cla55.getName());
        }
        if(cla55.isAnnotationPresent(Controller.class)){
            return initController.get(cla55.getName());
        }
        if(cla55.isAnnotationPresent(Component.class)){
            return cla55.getConstructor().newInstance();
        }
        throw new Exception("Autowired but not Bean || Service || Component");
    }

    @SuppressWarnings("rawtypes")
    private List<Class> listAllClasses(){
        List<Class> classes = new ArrayList<>();
        for (String path : listAllFiles("target")){
            try {
                File root = new File("target/classes");
                String cp = path.replace(root.getAbsolutePath(),"");
                cp = cp.replace(".class","");
                cp = cp.replace("\\",".");
                cp = cp.replace("/",".");
                cp = cp.replaceFirst(".","");
                System.out.println(cp);
                Class cl = Class.forName(cp);
                classes.add(cl);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return classes;
    }


    private List<String> listAllFiles(String dir){
        List<String> files = new ArrayList<>();
        File file = new File(dir);
        for (File file1 : file.listFiles()){
            if(file1.isDirectory()){
                files.addAll(listAllFiles(file1.getAbsolutePath()));
            } else {
                if(file1.getAbsolutePath().endsWith(".class") && !file1.getAbsolutePath().endsWith("CFT.java"))
                    files.add(file1.getAbsolutePath());

            }
        }
        return files;
    }
}
