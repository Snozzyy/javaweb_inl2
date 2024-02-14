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
            "<title>Show students</title>" +
            "<link rel=\"stylesheet\" href=\"css/style.css\">" +
            "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">" +
            "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap\" rel=\"stylesheet\">" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@100&display=swap\" rel=\"stylesheet\"> " +
            "</head>" +
            "<body>" +
            "<nav class=\"navbar\">" +
            "<a href=\"../\">Home</a>" +
            "<a href=\"/students\" id=\"active\">Students</a>" +
            "<a href=\"/createstudent\">Create student</a>" +
            "<a href=\"/createcourse\">Create course</a>" +
            "<a href=\"/assign\">Assign to course</a>" +
            "</nav>";

    String htmlBottom = // Bottom part of html-code
            "</body>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname;
        String lname;
        String city;
        String interests;

        String studentsTable =
                "<div class=\"tables\">" +
                "<table>" + // Table part of html-code
                "<tr>" +
                "<th>First name</th>" +
                "<th>Last name</th>" +
                "<th>City</th>" +
                "<th>Interests</th>" +
                "</tr>";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "root", "");

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

        studentsTable += "</table><br></div>"; // Close table

        String htmlForm = // Form part of html-code
                "<form action=\"/students\" method=\"post\">" +
                    "<label for=\"fname\">First name:</label><br>" +
                    "<input type=\"text\" id=\"fname\" name=\"fname\" required><br>" +
                    "<label for=\"lname\">Last name:</label><br>" +
                    "<input type=\"text\" id=\"lname\" name=\"lname\" required><br><br>" +
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

        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String course;
        String courseTable =
                "<div class=tables>" +
                "<table>" +
                "<tr>" +
                "<th>First name</th>" +
                "<th>Last name</th>" +
                "<th>Courses</th>" +
                "</tr>";

        String backButton =
                "<a href=\"/students\">" +
                "<button>Go back</button>" +
                "</a>";

        String sql = "SELECT c.name AS course_name " +
                "FROM students = s " +
                "INNER JOIN attendance = a " +
                "ON s.id = a.student_id " +
                "INNER JOIN courses = c " +
                "ON a.course_id = c.id " +
                "WHERE s.fname = '" + fname + "' AND s.lname = '" + lname + "';";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "root", "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.isBeforeFirst()) {
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

                courseTable += "</table></div><br>"; // Close table
                out.println(htmlTop + courseTable + backButton + htmlBottom);

            } else {
                String errorMessage = "User does not exist or is assigned to a course<br>";
                out.println(htmlTop + errorMessage + backButton + htmlBottom);
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Student is not found or is not assigned to a course");
        }
    }
}
