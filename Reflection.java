package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Reflection {
   private Class<?> c;
   private Map<Class<?>, List<Class<?>>> graph = new HashMap<>();
   private List<Class<?>> mylist;
   
   public static void main(String args[]) throws Exception{
	   
	   JarInputStream in = new JarInputStream(new FileInputStream(new File("C:/Users/Yvonne/workspace/OOReflection/src/ie/gmit/sw/string-service.jar")));
		JarEntry next = in.getNextJarEntry();
		URL[] urls = { new URL("jar:file:" + "C:/Users/Yvonne/workspace/OOReflection/src/ie/gmit/sw/string-service.jar!/") };
		URLClassLoader child = new URLClassLoader (urls, in.getClass().getClassLoader());
	   
		while(next!=null)
		{
			if(next.getName().endsWith(".class"))
			{
				String name=next.getName().replaceAll("/", "\\.");
				name=name.replaceAll(".class", "");
				if (!name.contains("$")) 
					name.substring(0, name.length() - ".class".length());
		        try {
		            Class<?> queryClass = child.loadClass(name);
		            new Reflection(queryClass);
		        } catch (ClassNotFoundException ee) {
		            System.out.println("Couldn't find class '"+ name + "'");
		            System.exit(1);
		        }
			}
			next = in.getNextJarEntry(); 
	}
		in.close();
		child.close();
   }

   public Reflection(Class<?> c){
      super();
      this.c = c;

      //printConstructors();
      //printFields();
      //printMethods();
      //isInterface();
      //createArray();
      addToList();
   }
   
   public void addToList()
   {
	   
	   graph.put(c.getClass(), mylist);
	   System.out.println(graph.containsKey(c.getClass()));
   }
   
   public void isInterface(){
	   
	   boolean imAnIface = c.isInterface();
	   if(imAnIface==true)
		   System.out.println("Interface");
	   else
		   System.out.println("");
   }

   public void printConstructors(){
      Constructor<?> ctorlist[] = c.getDeclaredConstructors();
      System.out.println("--------------" + ctorlist.length + " Constructors --------------");
      for (int i = 0; i < ctorlist.length; i++) {
         Constructor<?> ct = ctorlist[i];
         System.out.println("\tname  = " + ct.getName());
         System.out.println("\tdecl class = " + ct.getDeclaringClass());

         Class<?> pvec[] = ct.getParameterTypes();
         for (int j = 0; j < pvec.length; j++){
            System.out.println("\tparam #" + j + " " + pvec[j]);
         }

         Class<?> evec[] = ct.getExceptionTypes();
         for (int j = 0; j < evec.length; j++){
            System.out.println("\texc #" + j + " " + evec[j]);
         }
         System.out.println("\t-----");
      }
   }

   public void printFields(){
      Field fieldlist[] = c.getDeclaredFields();
      for (int i = 0; i < fieldlist.length; i++) {
         Field fld = fieldlist[i];
         System.out.println("\tname = " + fld.getName());
         System.out.println("\tdecl class = " + fld.getDeclaringClass());
         System.out.println("\ttype = " + fld.getType());
         int mod = fld.getModifiers();
         System.out.println("\tmodifiers = " + Modifier.toString(mod));
         System.out.println("-----");
      }
   }

   public void printMethods(){
      Method methlist[] = c.getDeclaredMethods();
      System.out.println("--------------" + methlist.length + " Methods --------------");
      for (int i = 0; i < methlist.length;i++) {
      	Method m = methlist[i];
      	System.out.println("\tname = " + m.getName());
      	System.out.println("\tdecl class = " + m.getDeclaringClass());
      	Class<?> pvec[] = m.getParameterTypes();
      	for (int j = 0; j < pvec.length; j++){
         		System.out.println("\tparam #" + j + " " + pvec[j]);
    	}
      	Class<?> evec[] = m.getExceptionTypes();
      	for (int j = 0; j < evec.length; j++){
         		System.out.println("\texc #" + j + " " + evec[j]);
      	}
      	System.out.println("\treturn type = " + m.getReturnType());
      	System.out.println("\t-----");
      }
   }

   public void createArray(){
      try {
         Class<?> cls = Class.forName("java.lang.String");
         Object arr = Array.newInstance(cls, 10);
         Array.set(arr, 5, "Msc OO");
         String s = (String)Array.get(arr, 5);
         System.out.println(s);
      }catch (Throwable e) {
         System.err.println(e);
      }
   }
}
