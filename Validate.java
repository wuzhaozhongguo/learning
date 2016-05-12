package org.albert.security.facade;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/12.
 */
public class Validate {
    public static Set<String> has = new HashSet<>();
    public static Set<String> add = new HashSet<>();
    public static void vali(Class clazz){
        Method[] methods = clazz.getMethods();

        for (Method method : methods){
            Class[] parameterTypes = method.getParameterTypes();
            System.out.println(parameterTypes.hashCode());

            if (clazz.getSimpleName().equals("SecurityCommandFacade") || clazz.getSimpleName().equals("SecurityQueryFacade")){
                has.add(method.getName());
            }else{
                add.add(method.getName());
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        File file = new File("./security-facade/src/main/java/org/albert/security/facade");
        File[] files = file.listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File dir, String name) {
                if (name.lastIndexOf("Facade") != -1){
                    return true;
                }
                return false;
            }
        });
        for(File f : files){
            Class aClass = Thread.currentThread().getContextClassLoader().loadClass("org.albert.security.facade."+f.getName().replace(".java",""));
            Validate.vali(aClass);
        }
        System.out.println("has:"+has.size()+"add:"+add.size());
    }
}
