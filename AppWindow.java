/**
 * 
 */

package ie.gmit.sw;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppWindow {
	private JFrame frame;
	private String filePath;
	private static Map<Class<?>, List<Class<?>>> graph;
	private static Map<Class<?>, List<Class<?>>> efferent;
	private static Map<Class<?>, List<Class<?>>> afferent;
	private static Map<Class<?>, Float> stabilityMap;
	private static Map<Class<?>, List<String>> effString;
    private static Map<Class<?>, List<String>> affString;
    private JButton btnOther;
    private JButton btnDialog;
    
	public AppWindow(){
		//Create a window for the application
		frame = new JFrame();
		frame.setTitle("B.Sc. in Software Development - GMIT");
		frame.setSize(550, 250);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		
        //The file panel will contain the file chooser
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
        top.setBorder(new javax.swing.border.TitledBorder("Select File to Encode"));
        top.setPreferredSize(new java.awt.Dimension(500, 100));
        top.setMaximumSize(new java.awt.Dimension(500, 100));
        top.setMinimumSize(new java.awt.Dimension(500, 100));
        
        final JTextField txtFileName =  new JTextField(20);
		txtFileName.setPreferredSize(new java.awt.Dimension(100, 30));
		txtFileName.setMaximumSize(new java.awt.Dimension(100, 30));
		txtFileName.setMargin(new java.awt.Insets(2, 2, 2, 2));
		txtFileName.setMinimumSize(new java.awt.Dimension(100, 30));
		
		JButton btnChooseFile = new JButton("Browse...");
		btnChooseFile.setToolTipText("Browse to find file");
        btnChooseFile.setPreferredSize(new java.awt.Dimension(90, 30));
        btnChooseFile.setMaximumSize(new java.awt.Dimension(90, 30));
        btnChooseFile.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnChooseFile.setMinimumSize(new java.awt.Dimension(90, 30));
		btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
        		JFileChooser fc = new JFileChooser("./");
        		int returnVal = fc.showOpenDialog(frame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
                	File file = fc.getSelectedFile().getAbsoluteFile();
                	String name = file.getAbsolutePath(); 
                	txtFileName.setText(name);
                	filePath=name;//save filepath of Jar to be analysed
                	if(filePath.contains(".jar"))
	                	//enable import button only when a file has been selected
	                	btnOther.setEnabled(true);
                	
                	
                	graph = new HashMap<Class<?>, List<Class<?>>>();
                	efferent = new HashMap<Class<?>, List<Class<?>>>();
                	afferent = new HashMap<Class<?>, List<Class<?>>>();
                	stabilityMap = new HashMap<Class<?>, Float>();
                	effString = new HashMap<Class<?>, List<String>>();
                    affString = new HashMap<Class<?>, List<String>>();
            	}
			}
        });
		
		btnOther = new JButton("Import Jar");
		btnOther.setEnabled(false);
		btnOther.setToolTipText("Import Jar for analysis");
		btnOther.setPreferredSize(new java.awt.Dimension(150, 30));
		btnOther.setMaximumSize(new java.awt.Dimension(150, 30));
		btnOther.setMargin(new java.awt.Insets(2, 2, 2, 2));
		btnOther.setMinimumSize(new java.awt.Dimension(150, 30));
		btnOther.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	//when import button is clicked, make instance of JarReader class and send graph to be filled to the constructor
            	JarReader jr = new JarReader(graph);
				//send filepath to readJar method
            	jr.readJar(filePath);
				//create instance of DependencyChecker and send maps to constructor
				DependencyChecker dCheck = new DependencyChecker(efferent, afferent, graph, effString, affString);
				//call DependencyChecker method to check the dependencies to and from each class of the jar
				dCheck.getDependencies();
				//create instance of Stability class and pass maps to constructor
				Stability stability = new Stability(efferent, afferent, stabilityMap);
				//call calculate method to work out the stability for each class
				stability.calculateStability();
				//enable check stability button only when Jar has been imported
				btnDialog.setEnabled(true);

			}
        });
		
        top.add(txtFileName);
        top.add(btnChooseFile);
        top.add(btnOther);
        frame.getContentPane().add(top); //Add the panel to the window
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setPreferredSize(new java.awt.Dimension(500, 50));
        bottom.setMaximumSize(new java.awt.Dimension(500, 50));
        bottom.setMinimumSize(new java.awt.Dimension(500, 50));
        
        btnDialog = new JButton("Show Stability Details"); //Create Quit button
        btnDialog.setEnabled(false);
        btnDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	//create instance of AppSummary and pass maps
            	AppSummary as =  new AppSummary(frame, true, effString, affString, stabilityMap);
            	as.show();
			}
        });
        
        JButton btnQuit = new JButton("Quit"); //Create Quit button
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
			}
        });
        bottom.add(btnDialog);
        bottom.add(btnQuit);

        frame.getContentPane().add(bottom);       
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new AppWindow();
	}
}