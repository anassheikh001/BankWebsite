import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class transfermoney extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String senderEmail = request.getParameter("senderEmail");
        String amountStr = request.getParameter("amount");
        
        try {
            double amount = Double.parseDouble(amountStr);

            // Fetch sender's information from the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?useTimeZone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false", "root", "root");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM customerdetails WHERE email = ?");
            ps.setString(1, senderEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double senderBalance = rs.getDouble("amount");

                if (senderBalance >= amount) {
                    // Deduct amount from sender's account
                    double newBalance = senderBalance - amount;
                    PreparedStatement deductPs = con.prepareStatement("UPDATE customerdetails SET amount = ? WHERE email = ?");
                    deductPs.setDouble(1, newBalance);
                    deductPs.setString(2, senderEmail);
                    deductPs.executeUpdate();

                    // Simulate successful transfer
                    out.println("<h3>Transfer successful!</h3>");
                    out.println("<p>Amount transferred: $" + amount + "</p>");
                    out.println("<p>Sender's Remaining Balance: $" + newBalance + "</p>");
                } else {
                    out.println("<h3>Insufficient balance in sender's account.</h3>");
                }
            } else {
                out.println("<h3>Sender account not found.</h3>");
            }

            con.close();
        } catch (NumberFormatException e) {
            out.println("<h3>Error: Invalid amount format</h3>");
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }

        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
