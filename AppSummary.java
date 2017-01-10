/**
 * 
 */

package ie.gmit.sw;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.*;

public class AppSummary extends JDialog{
	private static final long serialVersionUID = 777L;	
	private TypeSummaryTableModel tm = null;
	private JTable table = null;
	private JScrollPane tableScroller = null;
	private JButton btnClose = null;
	private JPanel tablePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private Container c;
	private static Map<Class<?>, Float> stabilityMap = new HashMap<Class<?>, Float>();
	private static Map<Class<?>, List<String>> effString = new HashMap<Class<?>, List<String>>();
    private static Map<Class<?>, List<String>> affString = new HashMap<Class<?>, List<String>>();
	
	public AppSummary(JFrame parent, boolean modal, Map<Class<?>, List<String>> effString, Map<Class<?>, List<String>> affString, Map<Class<?>, Float> stabilityMap){
        super(parent, modal);
        super.setTitle("Summary");
        super.setResizable(true);
        
        this.effString=effString;
		this.affString=affString;
		this.stabilityMap=stabilityMap;
        this.setSize(new Dimension(1000, 400));
        
		c = getContentPane();
		c.setLayout(new FlowLayout());	

		createTable();
        configureButtonPanel();
        
        c.add(tablePanel);
        c.add(buttonPanel);
	}
	
	
	private void createTable(){
		//pass appropriate maps to constructor of new table
		tm = new TypeSummaryTableModel(effString, affString, stabilityMap);
		table = new JTable(tm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionBackground(Color.YELLOW);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++){
			column = table.getColumnModel().getColumn(i);
			if ((i == 0)||(i == table.getColumnCount()-1)){
				column.setPreferredWidth(150);
				column.setMaxWidth(150);
				column.setMinWidth(150);
			}else{
				column.setPreferredWidth(400);
				column.setMaxWidth(400);
				column.setMinWidth(400);
			}
		}

		tableScroller = new JScrollPane(table);
		tableScroller.setPreferredSize(new java.awt.Dimension(700, 235));
		tableScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		tablePanel.add(tableScroller, FlowLayout.LEFT);
	}
	
	private void configureButtonPanel(){
    	buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		//Configure the Cancel button
		btnClose = new JButton("Close");
		btnClose.setToolTipText("Close this Window");
		btnClose.setPreferredSize(new java.awt.Dimension(100, 40));
		btnClose.setMaximumSize(new java.awt.Dimension(100, 40));
		btnClose.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnClose.setMinimumSize(new java.awt.Dimension(100, 40));
		btnClose.setIcon(new ImageIcon("images/close.gif"));
		btnClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});

		buttonPanel.add(btnClose);
	}
}
