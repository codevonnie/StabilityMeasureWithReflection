/**
 *  Class which constructs and fills Table for GUI
 */

package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.*;
public class TypeSummaryTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 777L;
	private String[] cols = {"Class", "Efferent", "Afferent", "Stability"};
	private Object[][] data;
	private static Map<Class<?>, List<String>> effString = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>> affString = new HashMap<Class<?>, List<String>>();
	private static Map<Class<?>, Float> stabilityMap = new HashMap<Class<?>, Float>();
	private static List<Class<?>> classList;
	
	public TypeSummaryTableModel(Map<Class<?>, List<String>> effString, Map<Class<?>, List<String>> affString, Map<Class<?>, Float> stabilityMap)
	{
		this.effString=effString;
		this.affString=affString;
		this.stabilityMap=stabilityMap;
		fillTable();
	}
	//method to fill the table values
	public void fillTable()
	{
		classList= new ArrayList<Class<?>>();
		//classList takes the names of each class
		affString.forEach( (k,v) -> classList.add(k));
		int rows;
		//2D Object array which takes the value of classLists for the row length and the length of the column array for the columns
		Object[][] arr = new Object[classList.size()][cols.length];
		//data array is initialised with the arr array
		data=arr;

		//loop through each column and fill
		for(int col=0; col<cols.length; col++)
		{
			//row is reset to zero after each column is filled
			rows=0;
			//loop through each class entry in classList
			for (Class c : classList)
			{
				//switch takes what column is being filled and uses the appropriate list or map
				switch(col){
					//first column, class names are taken from classList entry
					case 0 :
						data[rows][col]=c.getSimpleName();
					    break; 
						//second column, class names are gotten from efferent map classlist using the current class as the key
					   case 1 :
						       data[rows][col]=effString.get(c);
					      break; 
					    //third column, class names are gotten from afferent map class list using the current class as the key
					   case 2 :
						   data[rows][col]=affString.get(c);
						      break; 
					   //fourth column, stability values are gotten from stability map using the current class as the key    
					   case 3 :
						   data[rows][col]=stabilityMap.get(c).toString();
						      break; 

					   default : 

					}
				//row is incremented after each loop
				rows++;	
			}
		}
	}
	
	
	public int getColumnCount() {
        return cols.length;
    }
	
    public int getRowCount() {
        return data.length;
	}

    public String getColumnName(int col) {
    	return cols[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
	}
   
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
	}
}