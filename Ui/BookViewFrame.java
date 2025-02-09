import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookViewFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField, textField_1, textField_2, textField_3, textField_4;
    private int bookId;
    private Connection connection;

    public BookViewFrame(int bookId) {
        this.bookId = bookId;
        setTitle("View Book");
        setBounds(200, 200, 432, 448);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Database Connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "Confractus091205");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel("Book Name:");
        label.setBounds(21, 26, 92, 26);
        panel.add(label);
        textField = new JTextField(20);
        textField.setBounds(112, 24, 283, 31);
        panel.add(textField);

        JLabel label_1 = new JLabel("Author Name:");
        label_1.setBounds(21, 65, 92, 26);
        panel.add(label_1);
        textField_1 = new JTextField(20);
        textField_1.setBounds(112, 63, 283, 31);
        panel.add(textField_1);

        JLabel label_2 = new JLabel("Genre:");
        label_2.setBounds(21, 103, 92, 26);
        panel.add(label_2);
        textField_2 = new JTextField(20);
        textField_2.setBounds(112, 101, 283, 31);
        panel.add(textField_2);

        JLabel label_3 = new JLabel("Pages:");
        label_3.setBounds(21, 145, 92, 26);
        panel.add(label_3);
        textField_3 = new JTextField(20);
        textField_3.setBounds(112, 143, 283, 31);
        panel.add(textField_3);

        JLabel label_4 = new JLabel("Publication:");
        label_4.setBounds(21, 187, 92, 26);
        panel.add(label_4);
        textField_4 = new JTextField(20);
        textField_4.setBounds(112, 185, 283, 31);
        panel.add(textField_4);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(160, 250, 100, 40);
        panel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        loadBookData();
        setVisible(true);
    }

    private void loadBookData() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE id = ?");
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                textField.setText(rs.getString("name"));
                textField_1.setText(rs.getString("author"));
                textField_2.setText(rs.getString("genre"));
                textField_3.setText(String.valueOf(rs.getInt("pages")));
                textField_4.setText(rs.getString("publication"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load book data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE books SET name = ?, author = ?, genre = ?, pages = ?, publication = ? WHERE id = ?");
            stmt.setString(1, textField.getText());
            stmt.setString(2, textField_1.getText());
            stmt.setString(3, textField_2.getText());
            stmt.setInt(4, Integer.parseInt(textField_3.getText()));
            stmt.setString(5, textField_4.getText());
            stmt.setInt(6, bookId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update book!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}