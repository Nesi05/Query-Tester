/*Nesi Mpanzu
Project Convertisor
Query Tester */

package com.nesi.tp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.nesi.connection.nesiConnection;

public class Fenetre extends JFrame {

  /**
  * ToolBar to lunch query
  */
  private JToolBar tool = new JToolBar();

  /**
  * Button
  */
  private JButton load = new JButton(new ImageIcon("img/load.png"));

  /**
  *  delimitor
  */
  private JSplitPane split;

  /**
  * contenaire of result 
  */
  private JPanel result = new JPanel();

  /**
  * default query to start 
    */
  private String requete = "SELECT  * FROM classe";

  /**
  * composant in which taping query
  */
  private JTextArea text = new JTextArea(requete);
		
  /**
  * Constructor
  */
  public Fenetre(){
    setSize(900, 600);
    setTitle("TP JDBC");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
		
    initToolbar();
    initContent();
    initTable(requete);
  }
	
  /**
  * Initialize toolbar
  */
  private void initToolbar(){
    load.setPreferredSize(new Dimension(30, 35));
    load.setBorder(null);
    load.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        initTable(text.getText());
      }
    });

    tool.add(load);
    getContentPane().add(tool, BorderLayout.NORTH);
  }
	
  /**
  * Initialize windows containt
  */
  public void initContent(){
    //You know it...
    result.setLayout(new BorderLayout());
    split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(text), result);
    split.setDividerLocation(100);
    getContentPane().add(split, BorderLayout.CENTER);		
  }
	
  /**
  * Initialize the visual with the query entered in editor
  * @param query
  */
  public void initTable(String query){
    try {
    // create statement
      long start = System.currentTimeMillis();
      Statement state = SdzConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      // execut query
      ResultSet res = state.executeQuery(query);
      //execution time

      //get meta afin so getting name of row
      ResultSetMetaData meta = res.getMetaData();
      //initialize a array Object for array title
      Object[] column = new Object[meta.getColumnCount()];

      for(int i = 1 ; i <= meta.getColumnCount(); i++)
        column[i-1] = meta.getColumnName(i);

      // manipulation to obtain numbre of lignes
      res.last();
      int rowCount = res.getRow();
      Object[][] data = new Object[res.getRow()][meta.getColumnCount()];

      //back to the start
      res.beforeFirst();
      int j = 1;

      //filling the array Object[][]
      while(res.next()){
        for(int i = 1 ; i <= meta.getColumnCount(); i++)
          data[j-1][i-1] = res.getObject(i);
				
        j++;
      }

      //close all                                    
      res.close();
      state.close();

      long totalTime = System.currentTimeMillis() - start;

      //removing containt of the container
      result.removeAll();
      //add JTable
      result.add(new JScrollPane(new JTable(data, column)), BorderLayout.CENTER);
      result.add(new JLabel("The query has been executed in " + totalTime + " ms and has returned " + rowCount + " ligne(s)"), BorderLayout.SOUTH);
      //forcing update to affich
      result.revalidate();
			
    } catch (SQLException e) {
      //in case of exception, affich a pop-up and delete containt	
      result.removeAll();
      result.add(new JScrollPane(new JTable()), BorderLayout.CENTER);
      result.revalidate();
      JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR ! ", JOptionPane.ERROR_MESSAGE);
    }	
  }

  /**
  * Starting point of the program
  * @param args
  */
  public static void main(String[] args){
    Fenetre fen = new Fenetre();
  fen.setVisible(true);
  }
}