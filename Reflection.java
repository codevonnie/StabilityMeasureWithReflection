package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Reflection {
   private static Class<?> c;
   private static Map<Class<?>, List<Class<?>>> efferent = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, List<Class<?>>> graph = new HashMap<Class<?>, List<Class<?>>>();
   private static List<Class<?>> myList;
   private static List<Class<?>> aList = new ArrayList<Class<?>>();
   
   public static void main(String args[]) throws Exception{
	   
	   JarInputStream in = new JarInputStream(new FileInputStream(new File("C:/Users/Yvonne/workspace/OOReflection/src/ie/gmit/sw/TestJar.jar")));
	   JarEntry next = in.getNextJarEntry();
	   URL[] urls = { new URL("jar:file:" + "C:/Users/Yvonne/workspace/OOReflection/src/ie/gmit/sw/TestJar.jar!/") };
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
		            Class<?> queryClass = child.loadClass(name);//make class instance of jar class
		            new Reflection(queryClass);//call Reflection constructor and pass jar class
		        } catch (ClassNotFoundException ee) {
		            System.out.println("Couldn't find class '"+ name + "'");
		            System.exit(1);
		        }
			}
			next = in.getNextJarEntry(); //get next class from jar
	}
		in.close(); //close JarInputStream
		child.close(); //close URLClassLoader
		
		getDependencies();
   }

   public Reflection(Class<?> c){
      super();
      this.c = c;

      //printConstructors();
      //printFields();
      //printMethods();
      //isInterface();
      //createArray();
      addToList(); //method to add classes to hashmap
   }
   
   public void addToList()
   {
	   myList = new ArrayList();
	   graph.put(c, myList); //add classes as key to hashmap
	   aList.add(c);
	   //System.out.println(graph.containsKey(c.getClass()));

   }
   
   public static void getDependencies()
   {
	   
	   //boolean iFace = isInterface();
	   System.out.println(graph.size());
	   
	   for (Map.Entry<Class<?>, List<Class<?>>> entry : graph.entrySet())
	   {
		   myList = new ArrayList();
		   c=entry.getKey();
		   for(int i=0; i<aList.size(); i++)
		   {
			   if(entry.getKey().isAssignableFrom(aList.get(i))&&aList.get(i)!=entry.getKey())
			   {
				   
				   myList.add(aList.get(i));
				   
				   //System.out.println(entry.getKey() + " is assignable from: " + " " + aList.get(i));
			   }
		   }
		   checkConstructor();
		   checkFields();
		   checkMethods();
		   efferent.put(c, myList);
		   //System.out.println(entry.getKey() + "/" + entry.getValue());
	   }
	   efferent.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
	   
	   //System.out.println(c.getInterfaces().toString());
   }
   
   public static boolean isInterface(){
	   
	   boolean imAnIface = c.isInterface();
	   return imAnIface;
   }

   private static void checkConstructor()
   {
	   Constructor<?> ctorlist[] = c.getDeclaredConstructors();
	   for (int i = 0; i < ctorlist.length; i++) {
		   
         Constructor<?> ct = ctorlist[i];
        
         Class<?> pvec[] = ct.getParameterTypes();
         for (int j = 0; j < pvec.length; j++){
        	 
        	 //System.out.println(pvec[j]);
        	 if(!pvec[j].toString().startsWith("class java.")&&!pvec[j].isPrimitive())
        	 {
        		 if(!myList.contains(pvec[j]))
        		 {
        			 myList.add(pvec[j]);
        		 }
        		 
        		 //System.out.println(pvec[j]);
        		 
        	 }
         }
         /*
         Class<?> evec[] = ct.getExceptionTypes();
         for (int j = 0; j < evec.length; j++){
            System.out.println("\texc #" + j + " " + evec[j]);
         }*/
         //System.out.println("\t-----");
      }
   }
   
   private static void checkFields()
   {
	   Field fieldlist[] = c.getDeclaredFields();
	      for (int i = 0; i < fieldlist.length; i++) {
	         Field fld = fieldlist[i];
	         if(!fld.getType().toString().startsWith("class java.")&&!fld.getType().isPrimitive())
        	 {
	        	 if(!myList.contains(fld.getType()))
        		 {
	        		 myList.add(fld.getType());
        		 }
	        	 
		         /*System.out.println("\tname = " + fld.getName());
		         System.out.println("\tdecl class = " + fld.getDeclaringClass());
		         System.out.println("\ttype = " + fld.getType());
		         int mod = fld.getModifiers();
		         System.out.println("\tmodifiers = " + Modifier.toString(mod));
		         System.out.println("-----");*/
        	 }
	      }
   }
   
   private static void checkMethods()
   {
	   Method methlist[] = c.getDeclaredMethods();

      for (int i = 0; i < methlist.length;i++) {
	      	Method m = methlist[i];

	      	for (int j = 0; j < methlist.length; j++)
	      	{
	      		if(!m.getReturnType().toString().contentEquals("void"))
  				{
	      			if(!myList.contains(m.getReturnType()))
	        		 {
		        		 myList.add(m.getReturnType());
	        		 }
  				}
	    	}
	      
	      	
	      }
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
