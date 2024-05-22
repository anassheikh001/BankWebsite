
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class customerLogin extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?useTimeZone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false", "root", "root");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM customerdetails WHERE email = ? AND password = ?");
            ps.setString(2, email);
            ps.setString(8, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                out.println("<script>alert('Logged In successfully');</script>");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
            } else {
                out.println("<script>alert('Invalid login credintials..... Please try again');</script>");
                RequestDispatcher rd = request.getRequestDispatcher("clogin.html");
                rd.include(request, response);
            }
        } catch (Exception e) {
            out.println(e);
        }
        out.close();
    }

    public void doPost(HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException {

        doGet(rq, rs);
    }
}
