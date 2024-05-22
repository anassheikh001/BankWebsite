import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class openAccount extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String address = request.getParameter("address");
        String accountType = request.getParameter("accountType");
        int amount = Integer.parseInt(request.getParameter("amount"));
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?useTimeZone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false", "root", "root");
            String query = "INSERT INTO customerdetails VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, dob);
            ps.setString(5, address);
            ps.setString(6, accountType);
            ps.setInt(7, amount);
            ps.setString(8, password);
            ps.setString(9, cpassword);

            int result = ps.executeUpdate();
            if (result > 0) {
                out.println("<script>alert('Account opened successfully');</script>");
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
            } else {
                out.println("<script>alert('Failed to open account');</script>");
                RequestDispatcher rd = request.getRequestDispatcher("openAccount.html");
                rd.include(request, response);
            }
            ps.close();
            con.close();
        } catch (Exception e) {
            out.println("<script>alert('Error: " + e.getMessage() + "');</script>");
            RequestDispatcher rd = request.getRequestDispatcher("openAccount.html");
            rd.include(request, response);
        }
        out.close();
    }
}
