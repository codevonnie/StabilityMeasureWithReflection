/**
 * 
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
	private Object[][] data = {
		{"Stuff 1", "Other Stuff 1", "Even More Stuff 1", " "},
		{"Stuff 2", "Other Stuff 2", "Even More Stuff 2", " "},
		{"Stuff 3", "Other Stuff 3", "Even More Stuff 3", " "},
		{"Stuff 4", "Other Stuff 4", "Even More Stuff 4", " "},
		{"Stuff 5", "Other Stuff 5", "Even More Stuff 5", " "},
		{"Stuff 6", "Other Stuff 6", "Even More Stuff 6", " "},
		{"Stuff 5", "Other Stuff 5", "Even More Stuff 5", " "},
		{"Stuff 6", "Other Stuff 6", "Even More Stuff 6", " "},
		{"Stuff 5", "Other Stuff 5", "Even More Stuff 5", " "},
		{"Stuff 6", "Other Stuff 6", "Even More Stuff 6", " "},
		{"Stuff 7", "Other Stuff 7", "Even More Stuff 7", " "}
	};
	private static Map<Class<?>, List<Class<?>>> efferent = new HashMap<Class<?>, List<Class<?>>>();
	private static Map<Class<?>, List<Class<?>>> afferent = new HashMap<Class<?>, List<Class<?>>>();
	private static Map<Class<?>, Float> stabilityMap = new HashMap<Class<?>, Float>();
	private static List<Class<?>> classList = new ArrayList<Class<?>>();
	
	public TypeSummaryTableModel(Map<Class<?>, List<Class<?>>> efferent, Map<Class<?>, List<Class<?>>> afferent, Map<Class<?>, Float> stabilityMap)
	{
		this.efferent=efferent;
		this.afferent=afferent;
		this.stabilityMap=stabilityMap;
		fillTable();
	}



	public void fillTable()
	{
		
		afferent.forEach( (k,v) -> classList.add(k));
		int j;
				
		for(int i=0; i<cols.length; i++)
		{
			j=0;
			for (Class c : classList)
			{
				switch(i){
					case 0 :
						data[j][i]=c;
						System.out.println("j: "+j+" i: "+i+ " "+ data[j][i]);
					    break; // optional
					   
					   case 1 :
						  data[j][i]=efferent.get(c);
						  System.out.println("j: "+j+" i: "+i+ " "+ data[j][i]);
					      break; // optional
					      
					   case 2 :
						   data[j][i]=afferent.get(c);
						   System.out.println("j: "+j+" i: "+i+ " "+ data[j][i]);
						      break; // optional
						      
					   case 3 :
						   data[j][i]=stabilityMap.get(c).toString();
						   System.out.println("j: "+j+" i: "+i+ " "+ data[j][i]);
						      break; // optional
					   
					   // You can have any number of case statements.
					   default : // Optional
					      // Statements
					}
				j++;	
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