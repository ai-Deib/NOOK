import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddBookFrame extends JFrame {

    private JTextField bookNameField, authorField, genreField, pagesField, publicationDateField;
    private JButton saveButton;
    private DashboardPanel dashboardPanel; // Reference to DashboardPanel

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "Confractus091205";

    public AddBookFrame(DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel; // Store reference to DashboardPanel

        setTitle("Add Book");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        inputPanel.add(new JLabel("Book Name:"));
        bookNameField = new JTextField();
        inputPanel.add(bookNameField);

        inputPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        inputPanel.add(authorField);

        inputPanel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        inputPanel.add(genreField);

        inputPanel.add(new JLabel("Pages:"));
        pagesField = new JTextField();
        inputPanel.add(pagesField);

        inputPanel.add(new JLabel("Publication Date (YYYY-MM-DD):"));
        publicationDateField = new JTextField();
        inputPanel.add(publicationDateField);

        saveButton = new JButton("Save");
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(saveButton);

        add(inputPanel, BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookToDatabase();
            }
        });

        setVisible(true);
        createBooksTable();
    }

    private void createBooksTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(255), " +
                "author VARCHAR(255), " +
                "genre VARCHAR(100), " +
                "pages INT, " +
                "publication_date DATE, " +
                "availability VARCHAR(255))";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error creating table!", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveBookToDatabase() {
        try {
            // Validate input fields
            String title = bookNameField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String publicationDate = publicationDateField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || publicationDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pages;
            try {
                pages = Integer.parseInt(pagesField.getText().trim());
                if (pages <= 0) {
                    JOptionPane.showMessageDialog(this, "Pages must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format for Pages.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String insertSQL = "INSERT INTO books (title, author, genre, pages, publication_date, availability) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, genre);
                preparedStatement.setInt(4, pages);
                preparedStatement.setString(5, publicationDate);
                preparedStatement.setString(6, "available");

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the window
                    if (dashboardPanel != null) {
                        dashboardPanel.reloadDashboard(); // Reload Dashboard
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the book.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: Could not insert data.\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
