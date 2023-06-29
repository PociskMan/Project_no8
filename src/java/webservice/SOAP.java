/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ejb.LocalBean;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * author student
 */
@LocalBean
@WebService(serviceName = "SOAP")
@Stateless()
public class SOAP {
    
    public static String sciezka(String filename) throws IOException {
        String urll = "/home/student/NetBeansProjects/projekt/build/web/"+filename; // OFFLINE
        
        File f = new File(urll);
          
        if(f.exists()==false){
            urll = "nie istnieje";
        }
      
        f.delete();
        return urll;
        
    }
    
    // From server
    public static void deletefile(String filename) throws IOException {
        String urll = "/home/student/NetBeansProjects/projekt/build/web/"+filename; //OFFLINE
     
        File f = new File(urll);
        f.delete();
    }
    
    protected static Connection initializeDatabase() throws SQLException, ClassNotFoundException {
        // Initialize all the information regarding
        // Database Connection
        String dbDriver = "org.mariadb.jdbc.Driver";
        String dbURL = "jdbc:mariadb://tjee.itc.wcy.wat.edu.pl:3306/";
        // Database name to access
        String dbName = "studenci_2020";
        String dbUsername = "student";
        String dbPassword = "student-TJEE";
  
        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
        return con;
    }
    
    // From DB
    public static void deletefile2(String fileppath) throws IOException {
        try{
            // Initialize the database
            Connection con = initializeDatabase();
            
            // Create a SQL query to insert data into demo table
            // demo table consists of two columns, so two '?' is used
            PreparedStatement st = con.prepareStatement("DELETE FROM WCY20KC1S0_POCIASK_AUDIO WHERE filename='"+ fileppath + "'");
  
            st.executeUpdate();
  
            // Close all the connections
            st.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        for (Part part : request.getParts()) {
            part.write("/" + fileName);
        }
        response.getWriter().print("The file uploaded sucessfully.");
    }
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
}
