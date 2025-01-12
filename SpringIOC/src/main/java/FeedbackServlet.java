import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackServlet extends HttpServlet {

    // JDBC configuration - Change these to your MySQL settings
    private static final String URL = "jdbc:mysql://localhost:3306/feedback_db";
    private static final String USER = "root";
    private static final String PASSWORD = "harsh2363";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve parameters from the request
        String name = request.getParameter("name");
        String bookName = request.getParameter("bookName");
        String feedback = request.getParameter("feedback");

        // Set the content type for the response
        response.setContentType("text/html");

        // Use try-with-resources for safe resource handling
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // SQL query to insert feedback into the database
            String query = "INSERT INTO feedback (name, book_name, feedback) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, bookName);
                ps.setString(3, feedback);

                int rowsAffected = ps.executeUpdate();
                try (PrintWriter out = response.getWriter()) {
                    if (rowsAffected > 0) {
                        out.println("<h2>Feedback saved!</h2>");
                    } else {
                        out.println("<h2>Failed to save feedback.</h2>");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.println("<h2>Error: " + e.getMessage() + "</h2>");
            }
        }
    }
}
