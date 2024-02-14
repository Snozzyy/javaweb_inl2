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

@WebServlet(name = "AssignStudent", urlPatterns = "/assign")
public class AssignStudentServlet extends HttpServlet {
    String htmlTop = // Top part of html-code
            "<!DOCTYPE html>" +
            "<head>" +
            "<title>Assign to course</title>" +
            "<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">" +
            "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap\" rel=\"stylesheet\">" +
            "<link href=\"https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@100&display=swap\" rel=\"stylesheet\"> " +
            "<link rel=\"stylesheet\" href=\"css/style.css\">" +
            "</head>" +
            "<body>" +
            "<nav class=\"navbar\">" +
            "<a href=\"../\">Home</a>" +
            "<a href=\"/students\">Students</a>" +
            "<a href=\"/createstudent\">Create student</a>" +
            "<a href=\"/createcourse\">Create course</a>" +
            "<a href=\"/assign\" id=\"active\">Assign to course</a>" +
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
        String courseName;
        String yhp;
        String desc;

        String studentsTable =
                "<div class=\"tables\">" +
                "<table>" + // Table part of html-code
                "<tr>" +
                "<th>First name</th>" +
                "<th>Last name</th>" +
                "<th>City</th>" +
                "<th>Interests</th>" +
                "</tr>";

        String coursesTable =
                "<table>" + // Table part of html-code
                "<tr>" +
                "<th>Course name</th>" +
                "<th>YHP</th>" +
                "<th>Description</th>" +
                "</tr>";

        String htmlForm =
                "<form action=\"/assign\" method=\"post\">" +
                "<label for=\"fname\">First name:</label><br>" +
                "<input type=\"text\" id=\"fname\" name=\"fname\" required><br>" +
                "<label for=\"lname\">Last name:</label><br>\n" +
                "<input type=\"text\" id=\"lname\" name=\"lname\" required><br>" +
                "<label for=\"course\">Course:</label><br>" +
                "<input type=\"text\" id=\"course\" name=\"course\" required><br><br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";

        // Populate student-table
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3305/gritacademy",
                    "user", "user");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT fname, lname, city, interests FROM students;");

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

            studentsTable += "</table><br>";

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        // Populate course-table
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3305/gritacademy",
                    "user", "user");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, yhp, description FROM courses;");

            while (rs.next()) {
                courseName = rs.getString("name");
                yhp = rs.getString("yhp");
                desc = rs.getString("description");

                coursesTable += // Dynamically add data to table
                        "<tr>" +
                        "<td>" + courseName + "</td>" +
                        "<td>" + yhp + "</td>" +
                        "<td>" + desc + "</td>" +
                        "</tr>";
            }

            coursesTable += "</table><br></div>";

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(htmlTop + studentsTable + coursesTable + htmlForm + htmlBottom);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String course = req.getParameter("course");

        String sql = String.format(
                "INSERT INTO " +
                    "attendance (student_id, course_id)" +
                "SELECT" +
                    "(SELECT id FROM students WHERE fname = '%s' AND lname = '%s')," +
                    "(SELECT id FROM courses WHERE name = '%s');", fname, lname, course);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3305/gritacademy",
                    "inserter", "inserter");

            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Entry added");

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        resp.sendRedirect("/assign");
    }
}
