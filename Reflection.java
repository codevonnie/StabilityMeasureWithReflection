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
   private static Class<?> c;
   private static Map<Class<?>, List<Class<?>>> efferent = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, List<Class<?>>> afferent = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, List<Class<?>>> graph = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, Float> stabilityMap = new HashMap<Class<?>, Float>();
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
		calculateStability();
		//printStability();
   }

   public Reflection(Class<?> c){
      super();
      this.c = c;

      //printConstructors();
      //printFields();
      //printMethods();
      //createArray();
      addToList(); //method to add classes to hashmap
   }
   
   public void addToList()
   {
	   myList = new ArrayList<Class<?>>();
	   graph.put(c, myList); //add classes as key to hashmap
	   aList.add(c);
	   //System.out.println(graph.containsKey(c.getClass()));

   }
   
   public static void getDependencies()
   {
	   
	   System.out.println(graph.size());
	   
	   for (Map.Entry<Class<?>, List<Class<?>>> entry : graph.entrySet())
	   {
		   myList = new ArrayList<Class<?>>();
		   c=entry.getKey();
		   for(int i=0; i<aList.size(); i++)
		   {
			   if((aList.get(i).isAssignableFrom(c))&&(aList.get(i)!=c))
			   {
				   myList.add(aList.get(i));

			   }
		   }
		   checkConstructor();
		   checkFields();
		   checkMethods();
		   efferent.put(c, myList);
		   //System.out.println(entry.getKey() + "/" + entry.getValue());
	   }
	   
	   fillAfferentList();
	   //efferent.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
	   //afferent.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
	   

   }
   
   private static void calculateStability()
   {
	   float aff = 0;
	   float eff = 0;
	   float stability = 0;
	   Class<?> temp;
	  
	   System.out.println("efferent");
	  efferent.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
	  System.out.println("afferent");
	  afferent.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
	   
	   for (Map.Entry<Class<?>, List<Class<?>>> entry : efferent.entrySet())
	   {
		   myList = new ArrayList<Class<?>>();
		   temp=entry.getKey();
		   eff=entry.getValue().size();
		   
		   if(afferent.containsKey(temp))
		   {
			   myList.addAll(entry.getValue());
		   }
		   
		   aff=afferent.get(temp).size();
		   
		   if(eff==0)
			   stability=0;
		   else
		   {
			   stability=(eff /(eff+aff));
		   }
			   

		   stabilityMap.put(temp, stability);

	   }
	   
	   stabilityMap.forEach( (k,v) -> System.out.println(k + " " + v));

   }
   
   
   
   private static void fillAfferentList()
   {
	   for(int i =0; i<aList.size(); i++) {
		
			myList=new ArrayList<Class<?>>();
			
			for (Map.Entry<Class<?>, List<Class<?>>> entry : efferent.entrySet())
		   {
				if(entry.getValue().contains(aList.get(i)))
				{
					myList.add(entry.getKey());
				}
		   }
			
			afferent.put(aList.get(i), myList);
			
		}
   }

   private static void checkConstructor()
   {
	   Constructor<?> ctorlist[] = c.getDeclaredConstructors();
	   for (int i = 0; i < ctorlist.length; i++) {
		   
         Constructor<?> ct = ctorlist[i];
        
         Class<?> pvec[] = ct.getParameterTypes();
         for (int j = 0; j < pvec.length; j++){
        	 
        	 if((!pvec[j].toString().startsWith("class java."))&&(!pvec[j].isPrimitive())&&(!pvec[j].isArray()))
        	 {
        		 if(!myList.contains(pvec[j]))
        		 {
        			 myList.add(pvec[j]);
        		 }
       		 
        	 }
         }
      }
	   
   }
   
   private static void checkFields()
   {
	   Field fieldlist[] = c.getDeclaredFields();
	      for (int i = 0; i < fieldlist.length; i++) {
	         Field fld = fieldlist[i];
	         if((!fld.getType().toString().startsWith("class java."))&&(!fld.getType().isPrimitive())&&(!fld.getType().isArray()))
        	 {
	        	 if(!myList.contains(fld.getType()))
        		 {
	        		 myList.add(fld.getType());
        		 }
        	 }
	      }
	    
   }
   
   private static void checkMethods()
   {
	   Method methlist[] = c.getDeclaredMethods();

      for (int i = 0; i < methlist.length;i++) {
	      	Method m = methlist[i];
	      	Class<?> pvec[] = m.getParameterTypes();
	      	for (int j = 0; j < methlist.length; j++)
	      	{
	      		for (int l = 0; l < pvec.length; l++){
	      			if((!pvec[l].toString().startsWith("class java."))&&(!pvec[l].isPrimitive())&&(!pvec[l].isArray()))
	      			{
	      				if(!myList.contains(pvec[l]))
		        		 {
			        		 myList.add(pvec[l]);
		        		 }
	      			}
	      			
	      		}
	      		//System.out.println("m.getReturnType: " + m.getReturnType());
	      		if((!m.getReturnType().toString().contentEquals("void"))&&(!m.getReturnType().isArray()))
  				{
	      			if((!m.getReturnType().toString().startsWith("class java."))&&(!m.getReturnType().isPrimitive()))
  					{
	      				if(!myList.contains(m.getReturnType()))
		        		 {
			        		 myList.add(m.getReturnType());
		        		 }
  					}
	      			//
	      			
  				}
	    	}
	      }
     
   }
  
}
