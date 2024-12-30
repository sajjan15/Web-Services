package clinicManagementREST;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String adminUsername = "admin";
        String adminPassword = "password123";
        
        String doctorUsername = "doctor";
        String doctorPassword = "doctor123";
        
        String doctorUsername1 = "ENT_001";
        String doctorPassword1 = "ent001";
        
        String patientUsername = "patient";
        String patientPassword = "patient123";
        
        String patientUsername1 = "PAT_006";
        String patientPassword1 = "pat006";

        if (username.equals(adminUsername) && password.equals(adminPassword)) {

            response.sendRedirect("AdminDashboard.html");
        } else if (username.equals(doctorUsername) && password.equals(doctorPassword)) {

            response.sendRedirect("DoctorDashboard.html");
        } else if (username.equals(patientUsername) && password.equals(patientPassword)) {

            response.sendRedirect("PatientDashboard.html");
        }   else if (username.equals(patientUsername1) && password.equals(patientPassword1)) {

                response.sendRedirect("Patient006Form.html");
        }   else if (username.equals(doctorUsername1) && password.equals(doctorPassword1)) {

            response.sendRedirect("Doctor001Form.html");
        } else {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3 style='color:red;'>Invalid username or password.</h3>");
            out.println("<a href='login.html'>Go back to Login</a>");
            out.println("</body></html>");
        }
    }
}
