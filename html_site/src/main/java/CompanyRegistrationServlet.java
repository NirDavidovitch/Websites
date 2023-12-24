import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/companyRegistration")
public class CompanyRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// JDBC URL, username, and password of MySQL server
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/my_store";
	private static final String JDBC_USER = "Nird221";
	private static final String JDBC_PASSWORD = "1234";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve data from the form
        String companyName = request.getParameter("companyName");
        String city = request.getParameter("city");
        String contactPhone = request.getParameter("contactPhone");
        String creditCard = request.getParameter("creditCard");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        
        MySqlCRUD sqlCRUD = MySqlCRUD.getInstance(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        
        if (0 != sqlCRUD.createCompany(companyName, city, contactPhone, creditCard, expiryDate, cvv)) {
        	try (SocketChannel channel = SocketChannel.open()) {
    			channel.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 4997));
    			
    			String jsonString = "{\"company\":\"" + companyName + "\",\"command\":\"RegCompany\",\"priority\":\"2\"}";

    			channel.write(ByteBuffer.wrap(jsonString.getBytes()));
        	} catch (IOException  e) {
    			throw new RuntimeException(e);
    		}
        	showSuccessPopup(response);
        } else {
            // Handle the case where registration was not successful
            response.getWriter().println("Registration failed.");
        }
    }

	private void showSuccessPopup(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<script>");
		out.println("alert('Company submitted successfully!');");
		out.println("window.location.href = '/IotWebPage';"); // Redirect to the home page
		out.println("</script>");
	}
}
