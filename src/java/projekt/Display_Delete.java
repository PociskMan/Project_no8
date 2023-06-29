/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * author student
 */
public class Display_Delete extends HttpServlet {


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
            throws IOException  {  
        try{
            // Initialize the database
            Connection con = initializeDatabase();

            // Create a SQL query to insert data into demo table
            // demo table consists of two columns, so two '?' is used
            PreparedStatement st = con.prepareStatement("SELECT filename as 'Nazwa pliku mp3' FROM WCY20KC1S0_POCIASK_AUDIO ");

            // Execute the insert command using executeUpdate()
            // to make changes in the database
            ResultSet rs = st.executeQuery();

            // Close all the connections
            st.close();
            con.close();

            // Get a writer pointer to display the successful result
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
                "            <h1>Mp3 file list</h1><TABLE>\n");

            ResultSetMetaData rsmd = rs.getMetaData();
            int numcols = rsmd.getColumnCount();

            // Title the table with the result set's column labels
            writer.println("<TR>");
            for (int i = 1; i <= numcols; i++)
                writer.println("<TH>");
            writer.println("</TR>");

            while(rs.next()) {
                writer.println("<TR>");  // start a new row
                for(int i = 1; i <= numcols; i++) {
                    writer.println("<TD>");  // start a new data element
                    Object obj = rs.getObject(i);
                    if (obj != null)
                        writer.println(obj.toString() + "\n" + "<audio controls><source src=\"/projekt/"+obj.toString()+"\" /> </audio> <form method=\"post\" action=\"Display_Delete\" enctype=\\\"multipart/form-data\\\"> <button type=\"submit\" name=\"filen\" value=\""+obj.toString() + "\">DELETE</button></form>");
                    else
                        writer.println("&nbsp;");
                }
                writer.println("</TR>\n");
            }

            // End the table
            writer.println("</TABLE><form action=\"Upload\" method=\"get\">\n" +
                "        <input type=\"submit\" value=\"Upload mp3 file\" />\n" +
                "    </form>\n" +
                "    <form action=\"Display_Delete\" method=\"get\">\n" +
                "        <input type=\"submit\" value=\"Show mp3 file list\" />\n" +
                "    </form>\n" +
                "        <a href=\"/projekt/index.html\" > <input href=\"/projekt/index.html\" type=\"submit\" value=\"Main page\" /></a></div></body></html> ");
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }  

    public void doPost(HttpServletRequest request, HttpServletResponse response) //USUWANIE PRZEZ SOAP
            throws IOException {
        PrintWriter writer = response.getWriter();
        String fileppath = request.getParameter("filen");
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
            "            <h1>File deleted</h1>"+fileppath + "<h3>Path:</h3> " + webservice.SOAP.sciezka(fileppath) +"<form action=\"Upload\" method=\"get\">\n" +
            "        <input type=\"submit\" value=\"Upload mp3 file\" />\n" +
            "    </form>\n" +
            "    <form action=\"Display_Delete\" method=\"get\">\n" +
            "        <input type=\"submit\" value=\"Show mp3 file list\" />\n" +
            "    </form>\n" +
            "        <a href=\"/projekt/index.html\" > <input href=\"/projekt/index.html\" type=\"submit\" value=\"Main page\" /></a></div></body></html> ");
        writer.flush();

        webservice.SOAP.deletefile(fileppath);
        webservice.SOAP.deletefile2(fileppath);
    }
}
