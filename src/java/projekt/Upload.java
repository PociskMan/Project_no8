/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;
import webservice.SOAP;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;

@MultipartConfig(
  maxFileSize = 1024 * 1024 * 8,      // 8 MB
  location="/home/student/NetBeansProjects/projekt/build/web"
)
/**
 *
 * @author student
 */
public class Upload extends HttpServlet {
    
    
    protected static Connection initializeDatabase()
        throws SQLException, ClassNotFoundException
    {
        // Initialize all the information regarding
        // Database Connection
        String dbDriver = "org.mariadb.jdbc.Driver";
        String dbURL = "jdbc:mariadb://tjee.itc.wcy.wat.edu.pl:3306/";
        // Database name to access
        String dbName = "studenci_2020";
        String dbUsername = "student";
        String dbPassword = "student-TJEE";
  
        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL + dbName,
                                                     dbUsername, 
                                                     dbPassword);
        return con;
    }
 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String url1 = request.getServletPath();
        StringBuffer url2 = request.getRequestURL();
        String url3 = request.getRequestURI();
        PrintWriter writer = response.getWriter();
        writer.println("<html>\n" +
            "\n" +
            "    <head>\n" +
            "        <title>JAVA-EE PROJEKT | KAMIL POCIASK</title>\n" +
            "        <!-- załącznik Style.css -->\n" +
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">\n" +
            "        <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
            "\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <div id=\"top\">\n" +
            "            <h1>JAVA-EE PROJECT | KAMIL POCIASK</h1>\n" +
            "        </div>\n" +
            "\n" +
            "        <div id=\"upload\">\n" +
            "            <h1>Upload mp3 file</h1> Max file size: 8MB" +
            "<form method=\"post\" action=\"Upload\" enctype=\"multipart/form-data\">\n" +
            "    <input type=\"file\" name=\"file\" />" +
            "    <input type=\"submit\" value=\"Upload\" />" +
            "  </form>" + "<a href=\"/projekt/index.html\" ><input href=\"/projekt/index.html\" type=\"submit\" value=\"Main page\"</div>\n" +
            "    </body>\n" +
            "</html>");
        writer.flush();
    }
 
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        for (Part part : request.getParts()) {
            part.write("/" + fileName);
        }
        try {
            // Initialize the database
            Connection con = initializeDatabase();
  
            // Create a SQL query to insert data into demo table
            // demo table consists of two columns, so two '?' is used
            PreparedStatement st = con.prepareStatement("insert into WCY20KC1S0_POCIASK_AUDIO (filename) Values (?)");
  
            // For the first parameter,
            // get the data using request object
            // sets the data to st pointer
            //st.setInt(1, Integer.valueOf(request.getParameter("id")));
  
            // Same for second parameter
            st.setString(1, fileName);
  
            // Execute the insert command using executeUpdate()
            // to make changes in database
            st.executeUpdate();
  
            // Close all the connections
            st.close();
            con.close();
  
            // Get a writer pointer 
            // to display the successful result
            String url1 = request.getServletPath();
            StringBuffer url2 = request.getRequestURL();
            String url3 = request.getRequestURI();
            PrintWriter out = response.getWriter();
             
            out.println("<html>\n" +
                "\n" +
                "    <head>\n" +
                "        <title>JAVA-EE PROJEKT | KAMIL POCIASK</title>\n" +
                "        <!-- załącznik Style.css -->\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">\n" +
                "        <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
                "\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div id=\"top\">\n" +
                "            <h1>JAVA-EE PROJECT | KAMIL POCIASK</h1>\n" +
                "        </div>\n" +
                "\n" +
                "        <div id=\"upload\">" +
                "            <h1>Upload mp3 file</h1>Uploaded Successfully" +
                "<form action=\"Upload\" method=\"get\">" +
                "        <input type=\"submit\" value=\"Upload mp3 file\" />" +
                "    </form>" +
                "    <form action=\"Display_Delete\" method=\"get\">" +
                "        <input type=\"submit\" value=\"Show mp3 list\" />" +
                "    </form>" +
                "        <a href=\"/projekt/index.html\" ><input href=\"/projekt/index.html\" type=\"submit\" value=\"Main page\" />"
                + "</div>\n"
                + "</body>\n"
                + "</html> ");
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
