package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "CreateStudent", urlPatterns = "/createstudent")
public class CreateStudentServlet extends HttpServlet {

    String htmlTop = // Top part of html-code
            "<!DOCTYPE html>" +
            "<head>" +
            "<title>Create student</title>" +
            "<link rel=\"stylesheet\" href=\"css/style.css\">" +
            "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">" +
            "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap\" rel=\"stylesheet\">" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@100&display=swap\" rel=\"stylesheet\"> " +
            "</head>" +
            "<body>" +
            "<nav class=\"navbar\">" +
            "<a href=\"index.html\">Home</a>" +
            "<a href=\"/students\">Students</a>" +
            "<a href=\"/createstudent\" id=\"active\">Create student</a>" +
            "<a href=\"/createcourse\">Create course</a>" +
            "<a href=\"/assign\">Assign to course</a>" +
            "</nav>";
    String htmlBottom = // Bottom part of html-code
            "</body>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String htmlForm =
                "<form action=\"/createstudent\" method=\"post\">" +
                "<label for=\"fname\">First name:</label><br>" +
                "<input type=\"text\" id=\"fname\" name=\"fname\" required><br>" +
                "<label for=\"lname\">Last name:</label><br>\n" +
                "<input type=\"text\" id=\"lname\" name=\"lname\" required><br>" +
                "<label for=\"city\">City:</label><br>" +
                "<input type=\"text\" id=\"city\" name=\"city\"><br>" +
                "<label for=\"interests\">Interests:</label><br>" +
                "<input type=\"text\" id=\"interests\" name=\"interests\"><br><br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(htmlTop + htmlForm + htmlBottom);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String city = req.getParameter("city");
        String interests = req.getParameter("interests");
        String sql = String.format("INSERT INTO students (fname, lname, city, interests) VALUES ('%s', '%s', '%s', '%s');",
                fname, lname, city, interests);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "root", "");

            Statement stmt = con.createStatement();
            int rows = stmt.executeUpdate(sql);
            System.out.println(rows);

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        resp.sendRedirect("/students");
    }
}
