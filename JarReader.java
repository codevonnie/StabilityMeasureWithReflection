/**
 * Class that uses a JarInputStream to read in a Jar and uses ClassLoader to get each class in Jar
 */

package ie.gmit.sw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class JarReader
{
	private static Map<Class<?>, List<Class<?>>> graph = new HashMap<Class<?>, List<Class<?>>>();
	
	public JarReader(Map<Class<?>, List<Class<?>>> graph)
	{
		this.graph=graph;
	}
	//method takes in filePath gotten from GUI
	public void readJar(String filePath)
	{
		System.out.println(filePath);
		//creates instance of ListBuilder class
		ListBuilder ls = new ListBuilder();
		//changes all backslashes to forward slashes in filepath
		String path=filePath.replace(File.separator, "/");
		JarInputStream in =	null;
		JarEntry next;
		URLClassLoader child = 	null;
		
		try
		{
			in = new JarInputStream(new FileInputStream(new File(path)));
			//add !/ to filepath for urls array
			path = path.concat("!/");
			URL[] urls = { new URL("jar:file:" + path) };
			child = new URLClassLoader (urls, in.getClass().getClassLoader());
			next = in.getNextJarEntry();
			//loops through all entries in Jar 
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
			            ls = new ListBuilder(queryClass, graph);//call ListBuilder constructor and pass jar class and map
			            //call buildList method to add to graph map
			            ls.buildList();
			        } catch (ClassNotFoundException ee) {
			            System.out.println("Couldn't find class '"+ name + "'");
			            System.exit(1);
			        }
				}
				next = in.getNextJarEntry(); //get next class from jar
			}
			in.close(); //close JarInputStream
			child.close(); //close URLClassLoader
			System.out.println("JarReader");
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
