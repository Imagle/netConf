package config;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import config.mockDB.ConfigModel;



public class Client extends JFrame{
	
	private String filepath = "F:\\Workspaces\\eclipse\\set\\Config.mdb";
	
	public Client(){
		JTable table = new JTable();
		ConfigModel mode = new ConfigModel(filepath);
		table.setModel(mode);
		for(int i=0; i<table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(40);
		table.setRowHeight(20);
		JScrollPane jsp = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(200, 70));
		jsp.setAutoscrolls(true);
		getContentPane().add(jsp);
		
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new Client();
	}
}
