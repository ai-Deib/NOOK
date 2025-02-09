import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

public class BookDashboard extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    
    public BookDashboard() {
        setLayout(new BorderLayout());

        // Top Panel with Image Header
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(10, 200));
        panel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNewLabel_1 = new JLabel("");
        ImageIcon HeaderImg = new ImageIcon(getClass().getResource("libraryHeading.png"));
        Image HeaderImg2 = HeaderImg.getImage().getScaledInstance(1080, 500, Image.SCALE_SMOOTH);
        lblNewLabel_1.setIcon(new ImageIcon(HeaderImg2));
        panel.add(lblNewLabel_1, BorderLayout.CENTER);
        
        add(panel, BorderLayout.NORTH);
        
        // Load book data from database
        loadBookData();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadBookData() {
        String[] columnNames = {"Book ID", "Book Name", "Author", "Genre", "Pages", "Publication Date", "Availability", "Actions"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {
            
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("pages"),
                    rs.getDate("publication_date"),
                    rs.getString("availability"),
                    ""  // Placeholder for action buttons
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books from database: " + e.getMessage());
        }
    }
    
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton viewButton = createStyledButton("View");
        private JButton deleteButton = createStyledButton("Delete");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
            add(viewButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        private JButton viewButton = createStyledButton("View");
        private JButton deleteButton = createStyledButton("Delete");
        private int currentRow;

        public ButtonEditor() {
            viewButton.addActionListener(e -> openViewFrame(currentRow));
            deleteButton.addActionListener(e -> deleteBook(currentRow));
            panel.add(viewButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return super.stopCellEditing();
        }
    }

    private void openViewFrame(int row) {
        new BookViewFrame(model, row);
    }

    private void deleteBook(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int bookId = (int) model.getValueAt(row, 0);
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "password");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM books WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
                model.removeRow(row);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage());
            }
        }
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.GRAY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(70, 25));
        return button;
    }
}
