/**
 * Class to construct map based on each class read in from Jar
 */

package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBuilder
{
	private static Map<Class<?>, List<Class<?>>> graph = new HashMap<Class<?>, List<Class<?>>>();
	private static Class<?> c;
    private static List<Class<?>> myList;
    private static List<Class<?>> aList = new ArrayList<Class<?>>();
    
    public ListBuilder()
    {
    	super();
    }

	public ListBuilder(Class<?> c, Map<Class<?>, List<Class<?>>> graph)
	{
		super();
		this.c=c;
		this.graph=graph;
	}
	//method to build graph map with Class names
	public void buildList()
	{
		myList = new ArrayList<Class<?>>();
		graph.put(c, myList); //add classes as key to hashmap
		aList.add(c);
		System.out.println(c);
	}
}
