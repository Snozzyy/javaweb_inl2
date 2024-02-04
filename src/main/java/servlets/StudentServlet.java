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

@WebServlet(urlPatterns = "/students")
public class StudentServlet extends HttpServlet {

    String htmlTop = // Top part of html-code
            "<!DOCTYPE html>" +
            "<head>" +
            "<title>Students</title>" +
            "<link rel=\"stylesheet\" href=\"style.css\">" +
            "</head>" +
            "<body>";

    String navbar;

    String htmlBottom = // Bottom part of html-code
            "</body>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname;
        String lname;
        String city;
        String interests;

        String studentsTable = "<table>" + // Table part of html-code
                "<tr>" +
                "<th>First name</th>" +
                "<th>Last name</th>" +
                "<th>City</th>" +
                "<th>Interests</th>" +
                "</tr>";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "user", "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students;");

            while (rs.next()) {
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                city = rs.getString("city");
                interests = rs.getString("interests");

                studentsTable += // Dynamically add data to table
                        "<tr>" +
                            "<td>" + fname + "</td>" +
                            "<td>" + lname + "</td>" +
                            "<td>" + city + "</td>" +
                            "<td>" + interests + "</td>" +
                        "</tr>";
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        studentsTable += "</table><br>"; // Close table

        String htmlForm = // Form part of html-code
                "<form action=\"/students\" method=\"post\">" +
                    "<label for=\"fname\">First name:</label><br>" +
                    "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
                    "<label for=\"lname\">Last name:</label><br>" +
                    "<input type=\"text\" id=\"lname\" name=\"lname\"><br><br>" +
                    "<input type=\"submit\" value=\"Submit\"><br>" +
                "</form>";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(htmlTop + studentsTable + htmlForm + htmlBottom);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO
        //  - Vid fel visa ett meddelande som beskriver problemet
        //  - Fixa variabler för DB-info (user, pass, port, osv) - ALLA KLASSER

        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String course;
        String courseTable =
                "<table>" +
                "<tr>" +
                "<th>First name</th>" +
                "<th>Last name</th>" +
                "<th>Courses</th>" +
                "</tr>";
        String backButton = "<form action=\"/students\" method=\"get\">" +
                "<input type=\"submit\" value=\"Go Back\"><br>" +
                "</form>";

        String sql = "SELECT c.name AS course_name " +
                "FROM students = s " +
                "INNER JOIN attendance = a " +
                "ON s.id = a.student_id " +
                "INNER JOIN courses = c " +
                "ON a.course_id = c.id " +
                "WHERE s.fname = '" + fname + "' AND s.lname = '" + lname + "';";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "user", "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                course = rs.getString("course_name");

                courseTable +=
                        "<tr>" +
                        "<td>" + fname + "</td>" +
                        "<td>" + lname + "</td>" +
                        "<td>" + course + "</td>" +
                        "</tr>";

                System.out.println(fname + " " + lname);
                System.out.println(rs.getString("course_name"));
            }

            courseTable += "</table><br>"; // Close table

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(htmlTop + courseTable + backButton + htmlBottom);

    }
}
