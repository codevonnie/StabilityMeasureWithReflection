/**
 * 
 */

package ie.gmit.sw;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyChecker implements Dependable
{
	private static Map<Class<?>, List<Class<?>>> efferent = new HashMap<Class<?>, List<Class<?>>>();
    private static Map<Class<?>, List<Class<?>>> afferent = new HashMap<Class<?>, List<Class<?>>>();
    private static Map<Class<?>, List<Class<?>>> graph = new HashMap<Class<?>, List<Class<?>>>();
    private static List<Class<?>> myList;
    private static List<Class<?>> aList = new ArrayList<Class<?>>();
    private static Class<?> c;
	
	public DependencyChecker(Map<Class<?>, List<Class<?>>> efferent, Map<Class<?>, List<Class<?>>> afferent, Map<Class<?>, List<Class<?>>> graph)
	{
		this.efferent=efferent;
		this.afferent=afferent;
		this.graph=graph;
	}
	
	public void getDependencies()
	{
		graph.forEach( (k,v) -> aList.add(k));
		
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
			   checkConstructors();
			   checkFields();
			   checkMethods();
			   efferent.put(c, myList);

		   }
		   
		   fillAfferentList();
		
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

	public void checkConstructors()
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

	public void checkFields()
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

	public void checkMethods()
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
		      			
	  				}
		    	}
		  }
	}
}
