/**
 * Class to calculate stability of each class of the Jar using efferent and afferent values
 * using the Positional Stability formula 
 * I = Ce/Ca+Ce ---> Stability = (no. of efferent values/no. of afferent values + no. of efferent values)
 */

package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stability
{
   private static Class<?> c;
   private static Map<Class<?>, List<Class<?>>> efferent = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, List<Class<?>>> afferent = new HashMap<Class<?>, List<Class<?>>>();
   private static Map<Class<?>, Float> stabilityMap = new HashMap<Class<?>, Float>();
   private static List<Class<?>> myList;
	   
	   
	public Stability(Map<Class<?>, List<Class<?>>> efferent, Map<Class<?>, List<Class<?>>> afferent, Map<Class<?>, Float> stabilityMap)
	{
		this.efferent=efferent;
		this.afferent=afferent;
		this.stabilityMap=stabilityMap;
	}
	//method to calculate stability
	public void calculateStability()
    {
	   float aff = 0;
	   float eff = 0;
	   float stability = 0;
	   Class<?> temp;
	   //loop through each entry in the efferent map	  	   
	   for (Map.Entry<Class<?>, List<Class<?>>> entry : efferent.entrySet())
	   {
		   myList = new ArrayList<Class<?>>();
		   //temp takes value of current entry key value
		   temp=entry.getKey();
		   //efferent value is equal to the number of classes which are the values in the current entry of the efferent map
		   eff=entry.getValue().size();
		   //check the afferent map for the name of the current efferent entry
		   if(afferent.containsKey(temp))
		   {
			   //add all from the class list to list
			   myList.addAll(entry.getValue());
		   }
		   //afferent value is equal to the number of afferent values for the current class
		   aff=afferent.get(temp).size();
		   
		   //if efferent is 0, the stability will be zero
		   if(eff==0)
			   stability=0;
		   else
		   {
			   //using the positional stability formula
			   stability=(eff /(eff+aff));
		   }
			   
		   //stabilityMap is filled using the name of the class being checked and the stability result
		   stabilityMap.put(temp, stability);
	   }
   }
}
