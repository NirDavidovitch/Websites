import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ProductRegistration")
public class ProductRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve data from the form
		String company = request.getParameter("registeredCompany");
		String productID = request.getParameter("productID");
        String productName = request.getParameter("productName");
        String modelYear = request.getParameter("modelYear");

        MySqlCRUD sqlCRUD = MySqlCRUD.getInstance();
        
        if (0 != sqlCRUD.createProduct(company, productID, productName, modelYear)) {
        	showSuccessPopup(response);
        } else {
            // Handle the case where registration was not successful
            response.getWriter().println("Product Registration failed.");
        }
    }

	private void showSuccessPopup(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<script>");
		out.println("alert('Product submitted successfully!');");
		out.println("window.location.href = '/IotWebPage';"); // Redirect to the home page
		out.println("</script>");
	}
}
