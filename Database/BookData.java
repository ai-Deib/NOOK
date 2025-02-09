import java.sql.*;

public class BookData {
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "Confractus091205";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to retrieve all books from the database
    public static Object[][] getBooks() {
        String query = "SELECT book_id, title, author, genre, pages, publication_date, availability FROM books";
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            Object[][] data = new Object[rowCount][7];
            int rowIndex = 0;

            while (resultSet.next()) {
                data[rowIndex][0] = resultSet.getInt("book_id");
                data[rowIndex][1] = resultSet.getString("title");
                data[rowIndex][2] = resultSet.getString("author");
                data[rowIndex][3] = resultSet.getString("genre");
                data[rowIndex][4] = resultSet.getInt("pages");
                data[rowIndex][5] = resultSet.getDate("publication_date");
                data[rowIndex][6] = resultSet.getString("availability");
                rowIndex++;
            }

            return data;

        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[0][0]; // Return empty data if an error occurs
        }
    }
}
