import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Ui.Login;
import Ui.Dashboard;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "Confractus091205";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

 
    public Main() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NOOK");
        setBounds(400, 200, 960, 540);
        setIconImage(new ImageIcon(getClass().getResource("NOOK-icon.png")).getImage());
        createBooksTable();
        createUserTable();
        // Pass the Main frame (this) to Login
        setContentPane(new Login(this));  

    }

    public void switchToMainFrame() {
    	MainFrame dashboard = new MainFrame(); 
        dashboard.setVisible(true); 
        dispose(); 
    }
    
    private void createUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "student_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255), " +
                "role ENUM('student', 'admin'), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255))";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error creating table!", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
}