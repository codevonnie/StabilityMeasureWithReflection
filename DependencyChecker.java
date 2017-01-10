/**
 * Class that implements Dependable interface and implements methods to check for dependencies.
 * Efferent and Afferent mpas are constructed in this class
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
    private static Map<Class<?>, List<String>> effString = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>> affString = new HashMap<Class<?>, List<String>>();
    private static List<Class<?>> myList;
    private static List<Class<?>> aList;
    private static List<String> strList;
    private static Class<?> c;
	
	public DependencyChecker(Map<Class<?>, List<Class<?>>> efferent, Map<Class<?>, List<Class<?>>> afferent, Map<Class<?>, List<Class<?>>> graph, Map<Class<?>, List<String>> effString, Map<Class<?>, List<String>> affString)
	{
		this.efferent=efferent;
		this.afferent=afferent;
		this.graph=graph;
		this.effString=effString;
		this.affString=affString;
	}
	
	public void getDependencies()
	{
		aList = new ArrayList<Class<?>>();
		
		//graph map contains all classes contained in Jar and each key which is the class name is added to an ArrayList
		graph.forEach( (k,v) -> aList.add(k));
		//loop through each map entry in graph map
		for (Map.Entry<Class<?>, List<Class<?>>> entry : graph.entrySet())
		   {
			   myList = new ArrayList<Class<?>>();
			   strList=new ArrayList<String>();
			   //Class c takes the value of the current class of loop 
			   c=entry.getKey();
			   for(int i=0; i<aList.size(); i++)
			   {
				   //go through loop and check if each class in list implements the current class. 
				   //also checks that current class is not compared to itself
				   if((aList.get(i).isAssignableFrom(c))&&(aList.get(i)!=c))
				   {
					   //add to list
					   myList.add(aList.get(i));
					   if(aList.get(i)!=null)
						   strList.add(aList.get(i).getSimpleName());

				   }
			   }
			   checkConstructors();
			   checkFields();
			   checkMethods();
			   //add current class and completed list into efferent map
			   efferent.put(c, myList);
			   effString.put(c, strList);

		   }
		   //construct afferent map from efferent map
		   fillAfferentList();
		
	}
	//method to check for dependencies in class constructors
	public void checkConstructors()
	{
		//creates list of constructors for current class being checked
		Constructor<?> ctorlist[] = c.getDeclaredConstructors();
		   for (int i = 0; i < ctorlist.length; i++) {
			 
	         Constructor<?> ct = ctorlist[i];
	        //creates array of classes with all parameters passed to constructor
	         Class<?> pvec[] = ct.getParameterTypes();
	         for (int j = 0; j < pvec.length; j++){
	        	 //loop through params array and if class is not a standard java class, not a primitive and not an array add to list
	        	 if((!pvec[j].toString().startsWith("class java."))&&(!pvec[j].isPrimitive())&&(!pvec[j].isArray()))
	        	 {
	        		 //if the list does not already contain the class then add to list
	        		 if(!myList.contains(pvec[j]))
	        		 {
	        			 myList.add(pvec[j]);
	        			 if((pvec[j]!=null)&&(!strList.contains(pvec[j].getSimpleName())))
	        				 strList.add(pvec[j].getSimpleName());
	        		 }
	        	 }
	         }
	      }
	}
	//method to check for dependencies in class fields
	public void checkFields()
	{
		//create Field array from all Fields in current class being checked
		Field fieldlist[] = c.getDeclaredFields();
	      for (int i = 0; i < fieldlist.length; i++) {
	         Field fld = fieldlist[i];
	       //loop through Fields array and if class is not a standard java class, not a primitive and not an array add to list
	         if((!fld.getType().toString().startsWith("class java."))&&(!fld.getType().isPrimitive())&&(!fld.getType().isArray()))
	      	 {
	        	 	 //if the list does not already contain the class then add to list
		        	 if(!myList.contains(fld.getType()))
	      		 {
		        		 myList.add(fld.getType());
		        		 if((fld.getType()!=null)&&(!strList.contains(fld.getType().getSimpleName())))
		        			 strList.add(fld.getType().getSimpleName());
	      		 }
	      	 }
	    }
		
	}
	//method to check for dependencies in class methods
	public void checkMethods()
	{
		//create Method array from all declared Methods in current class being checked
		Method methlist[] = c.getDeclaredMethods();

	      for (int i = 0; i < methlist.length;i++) {
	    	  	//create array of Methods
		      	Method m = methlist[i];
		      	//create array of classes that are parameters in each method
		      	Class<?> pvec[] = m.getParameterTypes();
		      //loop through Method array and for each method check params array and if class is not a standard java class, not a primitive and not an array add to list
		      	for (int j = 0; j < methlist.length; j++)
		      	{
		      		for (int l = 0; l < pvec.length; l++){
		      			if((!pvec[l].toString().startsWith("class java."))&&(!pvec[l].isPrimitive())&&(!pvec[l].isArray()))
		      			{
		      				//if the list does not already contain the class then add to list
		      				if(!myList.contains(pvec[l]))
			        		 {
				        		 myList.add(pvec[l]);
				        		 if((pvec[l]!=null)&&(!strList.contains(pvec[l].getSimpleName())))
				        			 strList.add(pvec[l].getSimpleName());
				        		 
			        		 }
		      			}
		      			
		      		}
		      		//check if method return type is not void and not an array
		      		if((!m.getReturnType().toString().contentEquals("void"))&&(!m.getReturnType().isArray()))
	  				{
		      			//if method return type is not a standard java class or not a primitive and does not exist in list, add to list 
		      			if((!m.getReturnType().toString().startsWith("class java."))&&(!m.getReturnType().isPrimitive()))
	  					{
		      				if(!myList.contains(m.getReturnType()))
			        		 {
				        		 myList.add(m.getReturnType());
				        		 if((m.getReturnType()!=null)&&(!strList.contains(m.getReturnType().getSimpleName())))
				        			 strList.add(m.getReturnType().getSimpleName());
			        		 }
	  					}
		      			
	  				}
		    	}
		  }
	}
	//method for filling afferent map from efferent map
	private static void fillAfferentList()
   {
	   
	   for(int i =0; i<aList.size(); i++) {
		
			myList=new ArrayList<Class<?>>();
			strList=new ArrayList<String>();
			
			//loop through each entry in efferent map
			for (Map.Entry<Class<?>, List<Class<?>>> entry : efferent.entrySet())
		   {
				//if a class in efferent map contains entry for current class, add class to list to be added to afferent map
				if(entry.getValue().contains(aList.get(i)))
				{
					myList.add(entry.getKey());
					strList.add(entry.getKey().getSimpleName());
				}
		   }
			
			afferent.put(aList.get(i), myList);
			affString.put(aList.get(i), strList);
			
		}
	 }
}
