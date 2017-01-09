/**
 * 
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
	
	public void calculateStability()
    {
	   float aff = 0;
	   float eff = 0;
	   float stability = 0;
	   Class<?> temp;
	  	  	   
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
}
