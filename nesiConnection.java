package com.nesi.connection;
/*Nesi Mpanzu
Project Convertisor
Query Tester*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class nesiConnection{

  /**
  * URL of connexion
  */
  private static String url = "jdbc:postgresql://localhost:5432/Ecole";

  /**
  * Username
  */
  private static String user = "postgres";

  /**
  * User Passeword
  */
  private static String passwd = "postgres";

  /**
  * Objet Connexion
  */
  private static Connection connect;

  /**
  * Method returning our instance
  * and created it if it doesn't exist...
  * @return
  */
  public static Connection getInstance(){
    if(connect == null){
      try {
        connect = DriverManager.getConnection(url, user, passwd);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "CONNEXION ERROR ! ", JOptionPane.ERROR_MESSAGE);
      }
    }		
    return connect;	
  }
}