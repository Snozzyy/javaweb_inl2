package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@WebServlet(name = "CreateCourse", urlPatterns = "/createcourse")
public class CreateCourseServlet extends HttpServlet {
    String htmlTop = // Top part of html-code
            "<!DOCTYPE html>" +
            "<head>" +
            "<title>Create course</title>" +
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
            "<a href=\"/createstudent\">Create student</a>" +
            "<a href=\"/createcourse\" id=\"active\">Create course</a>" +
            "<a href=\"/assign\">Assign to course</a>" +
            "</nav>";
    String htmlBottom = // Bottom part of html-code
            "</body>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String htmlForm =
                "<form action=\"/createcourse\" method=\"post\">" +
                "<lablel for=\"cname\">Course name:<label><br>" +
                "<input type=\"text\" id=\"cname\" name=\"cname\" required><br>" +
                "<lablel for=\"yhp\">YHP:<label><br>" +
                "<input type=\"number\" id=\"yhp\" name=\"yhp\" required><br>" +
                "<lablel for=\"desc\">Description:<label><br>" +
                "<input type=\"text\" id=\"desc\" name=\"desc\" required><br><br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(htmlTop + htmlForm + htmlBottom);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cname = req.getParameter("cname");
        String yhp = req.getParameter("yhp");
        String desc = req.getParameter("desc");

        String sql = String.format("INSERT INTO courses (name, yhp, description) VALUES ('%s', %s, '%s');",
                cname, yhp, desc);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy",
                    "root", "");

            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        resp.sendRedirect("/createcourse");
    }
}
