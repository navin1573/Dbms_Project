import java.swing.*;
import java.awt.*;
import java.sql.*;
import DefaultTableModel;

public class MusicManager extends JFrame {

JTextField nameField, artistField, genreField, searchField;

JButton insertBtn, fetchBtn, deleteBtn, updateBtn, searchBtn;

JTable musicTable;

DefaultTableModel tableModel;

Connection conn;

public MusicManager() {

setTitle("Music DB Manager");

setSize(850, 550);

setLocationRelativeTo(null);

setDefaultCloseOperation(EXIT_ON_CLOSE);

setLayout(new BorderLayout());

JPanel mainPanel = new JPanel(new GridLayout(5, 2));

nameField = addLabeledField("Music Name:");

artistField = addLabeledField("Artist:");

genreField = addLabeledField("Genre:");

searchField = addLabeledField("Search by name:");

// Buttons

insertBtn = createButton("Insert");

fetchBtn = createButton("View All");

deleteBtn = createButton("Delete");

updateBtn = createButton("Update");

searchBtn = createButton("Search");

JPanel buttonPanel = new JPanel(new FlowLayout());

buttonPanel.add(insertBtn); buttonPanel.add(fetchBtn);

buttonPanel.add(deleteBtn);

buttonPanel.add(updateBtn); buttonPanel.add(searchBtn);

// Table

tableModel = new DefaultTableModel(new String[] {"ID", "Music Name",

"Artist", "Genre"}, 0);
  musicTable = new JTable(tableModel);

JScrollPane scrollPane = new JScrollPane(musicTable);

add(mainPanel, BorderLayout.NORTH);

add(buttonPanel, BorderLayout.CENTER);

add(scrollPane, BorderLayout.SOUTH);

try {

conn =

DriverManager.getConnection("jdbc:mysql://localhost:3306/music", "root",

"dbms");

} catch (SQLException e) {

showError("DB Connection failed", e);

}

// Action Listeners

insertBtn.addActionListener(e -> insertRecord());

fetchBtn.addActionListener(e -> fetchAll());

deleteBtn.addActionListener(e -> deleteRecord());

updateBtn.addActionListener(e -> updateRecord());

searchBtn.addActionListener(e -> searchRecord());

}

JTextField addLabeledField(String label) {

JPanel panel = (JPanel) getContentPane().getComponent(0);

panel.add(new JLabel(label));

JTextField field = new JTextField(20);

panel.add(field);

return field;

}

JButton createButton(String text) {

JButton btn = new JButton(text);

btn.setBackground(Color.GRAY);

btn.setForeground(Color.WHITE);

return btn;

}

void fetchAll() {

tableModel.setRowCount(0);

try (Statement stmt = conn.createStatement()) {

ResultSet rs = stmt.executeQuery("SELECT * FROM misc");

while (rs.next()) {

tableModel.addRow(new Object[]{rs.getInt("id"),

rs.getString("music_name"), rs.getString("artist"), rs.getString("genre")});

}

} catch (SQLException e) {
  showError("Fetch error", e);

}

}

void insertRecord() {

try {

String sql = "INSERT INTO misc (music_name, artist, genre)

VALUES (?, ?, ?)";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setString(1, nameField.getText());

stmt.setString(2, artistField.getText());

stmt.setString(3, genreField.getText());

stmt.executeUpdate();

showInfo("Record inserted.");

fetchAll();

} catch (SQLException e) {

showError("Insert error", e);

}

}

void deleteRecord() {

int selectedRow = musicTable.getSelectedRow();

if (selectedRow != -1) {

int musicId = (int) tableModel.getValueAt(selectedRow, 0);

try {

String sql = "DELETE FROM misc WHERE id = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setInt(1, musicId);

stmt.executeUpdate();

showInfo("Deleted record with ID " + musicId);

fetchAll();

} catch (SQLException e) {

showError("Delete error", e);

}

}

}

void updateRecord() {

int selectedRow = musicTable.getSelectedRow();

if (selectedRow != -1) {

int musicId = (int) tableModel.getValueAt(selectedRow, 0);

try {

String sql = "UPDATE misc SET music_name = ?, artist = ?,

genre = ? WHERE id = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setString(1, nameField.getText());

stmt.setString(2, artistField.getText());
  stmt.setString(3, genreField.getText());

stmt.setInt(4, musicId);

stmt.executeUpdate();

showInfo("Updated record with ID " + musicId);

fetchAll();

} catch (SQLException e) {

showError("Update error", e);

}

}

}

void searchRecord() {

String keyword = searchField.getText().trim();

tableModel.setRowCount(0);

if (keyword.isEmpty()) {

fetchAll();

return;

}

try {

String sql = "SELECT * FROM misc WHERE music_name LIKE ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setString(1, "%" + keyword + "%");

ResultSet rs = stmt.executeQuery();

while (rs.next()) {

tableModel.addRow(new Object[]{rs.getInt("id"),

rs.getString("music_name"), rs.getString("artist"), rs.getString("genre")});

}

} catch (SQLException e) {

showError("Search error", e);

}

}

void showInfo(String message) {

JOptionPane.showMessageDialog(this, "✅ " + message);

}

void showError(String title, Exception ex) {

JOptionPane.showMessageDialog(this, "❌ " + title + ": " +

ex.getMessage());

}

public static void main(String[] args) {

SwingUtilities.invokeLater(() -> new

MusicManager().setVisible(true));

}

}
