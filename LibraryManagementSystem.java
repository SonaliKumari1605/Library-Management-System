import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LibraryManagementSystem {

    // Simulated data store for books and users
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();

    static {
        // Initializing some books and users
        books.add(new Book("My experiment with truth", "M K Gandhi", true));
        books.add(new Book("Arthashashtra", "Kautilya", false));
        users.add(new User("Sonali", "sonali1605", "admin"));
        users.add(new User("user1", "user1@123", "user"));
    }

    static String loggedInRole = null;

    public static void main(String[] args) {
        loginScreen();
    }

    // Login Screen
    private static void loginScreen() {
        JFrame loginFrame = new JFrame("Library Management System - Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new FlowLayout());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        loggedInRole = user.getRole();
                        JOptionPane.showMessageDialog(loginFrame, "Welcome " + user.getRole());
                        loginFrame.dispose();
                        mainMenuScreen();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);

        loginFrame.setVisible(true);
    }

    // Main Menu Screen
    private static void mainMenuScreen() {
        JFrame mainMenuFrame = new JFrame("Library Management System - Main Menu");
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setLayout(new FlowLayout());

        JButton manageBooksButton = new JButton("Manage Books (Admin only)");
        manageBooksButton.addActionListener(e -> {
            if (loggedInRole.equals("admin")) {
                mainMenuFrame.dispose();
                manageBooksScreen();
            } else {
                JOptionPane.showMessageDialog(mainMenuFrame, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton manageUsersButton = new JButton("Manage Users (Admin only)");
        manageUsersButton.addActionListener(e -> {
            if (loggedInRole.equals("admin")) {
                mainMenuFrame.dispose();
                manageUsersScreen();
            } else {
                JOptionPane.showMessageDialog(mainMenuFrame, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton transactionsButton = new JButton("Transactions");
        transactionsButton.addActionListener(e -> {
            mainMenuFrame.dispose();
            transactionsScreen();
        });

        JButton reportsButton = new JButton("Reports");
        reportsButton.addActionListener(e -> {
            mainMenuFrame.dispose();
            reportsScreen();
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            mainMenuFrame.dispose();
            loginScreen();
        });

        mainMenuFrame.add(manageBooksButton);
        mainMenuFrame.add(manageUsersButton);
        mainMenuFrame.add(transactionsButton);
        mainMenuFrame.add(reportsButton);
        mainMenuFrame.add(logoutButton);

        mainMenuFrame.setVisible(true);
    }

    // Manage Books Screen
    private static void manageBooksScreen() {
        JFrame manageBooksFrame = new JFrame("Manage Books");
        manageBooksFrame.setSize(400, 400);
        manageBooksFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        manageBooksFrame.setLayout(new FlowLayout());

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField removeTitleField = new JTextField(20);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                books.add(new Book(title, author, true));
                JOptionPane.showMessageDialog(manageBooksFrame, "Book added successfully");
                titleField.setText("");
                authorField.setText("");
            } else {
                JOptionPane.showMessageDialog(manageBooksFrame, "All fields are mandatory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(e -> {
            String title = removeTitleField.getText();
            boolean found = false;
            for (Book book : books) {
                if (book.getTitle().equals(title)) {
                    books.remove(book);
                    JOptionPane.showMessageDialog(manageBooksFrame, "Book removed successfully");
                    removeTitleField.setText("");
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(manageBooksFrame, "Book not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        manageBooksFrame.add(new JLabel("Add Book"));
        manageBooksFrame.add(new JLabel("Title:"));
        manageBooksFrame.add(titleField);
        manageBooksFrame.add(new JLabel("Author:"));
        manageBooksFrame.add(authorField);
        manageBooksFrame.add(addBookButton);

        manageBooksFrame.add(new JLabel("Remove Book"));
        manageBooksFrame.add(new JLabel("Title:"));
        manageBooksFrame.add(removeTitleField);
        manageBooksFrame.add(removeBookButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            manageBooksFrame.dispose();
            mainMenuScreen();
        });
        manageBooksFrame.add(backButton);

        manageBooksFrame.setVisible(true);
    }

    // Manage Users Screen
    private static void manageUsersScreen() {
        JFrame manageUsersFrame = new JFrame("Manage Users");
        manageUsersFrame.setSize(400, 400);
        manageUsersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        manageUsersFrame.setLayout(new FlowLayout());

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField roleField = new JTextField(20);
        JTextField removeUserField = new JTextField(20);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();
            if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                // Check if the username already exists
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        JOptionPane.showMessageDialog(manageUsersFrame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                users.add(new User(username, password, role));
                JOptionPane.showMessageDialog(manageUsersFrame, "User added successfully");
                usernameField.setText("");
                passwordField.setText("");
                roleField.setText("");
            } else {
                JOptionPane.showMessageDialog(manageUsersFrame, "All fields are mandatory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.addActionListener(e -> {
            String username = removeUserField.getText();
            boolean found = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    users.remove(user);
                    JOptionPane.showMessageDialog(manageUsersFrame, "User removed successfully");
                    removeUserField.setText("");
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(manageUsersFrame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        manageUsersFrame.add(new JLabel("Add User"));
        manageUsersFrame.add(new JLabel("Username:"));
        manageUsersFrame.add(usernameField);
        manageUsersFrame.add(new JLabel("Password:"));
        manageUsersFrame.add(passwordField);
        manageUsersFrame.add(new JLabel("Role:"));
        manageUsersFrame.add(roleField);
        manageUsersFrame.add(addUserButton);

        manageUsersFrame.add(new JLabel("Remove User"));
        manageUsersFrame.add(new JLabel("Username:"));
        manageUsersFrame.add(removeUserField);
        manageUsersFrame.add(removeUserButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            manageUsersFrame.dispose();
            mainMenuScreen();
        });
        manageUsersFrame.add(backButton);

        manageUsersFrame.setVisible(true);
    }

    // Transactions Screen
    private static void transactionsScreen() {
        JFrame transactionsFrame = new JFrame("Transactions");
        transactionsFrame.setSize(400, 300);
        transactionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        transactionsFrame.setLayout(new FlowLayout());

        JTextField issueBookField = new JTextField(20);
        JButton issueButton = new JButton("Issue Book");
        issueButton.addActionListener(e -> {
            String bookTitle = issueBookField.getText();
            for (Book book : books) {
                if (book.getTitle().equals(bookTitle) && book.isAvailable()) {
                    book.setAvailable(false);
                    JOptionPane.showMessageDialog(transactionsFrame, "Book '" + bookTitle + "' issued!");
                    issueBookField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(transactionsFrame, "Book is unavailable or doesn't exist", "Error", JOptionPane.ERROR_MESSAGE);
        });

        JTextField returnBookField = new JTextField(20);
        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> {
            String bookTitle = returnBookField.getText();
            for (Book book : books) {
                if (book.getTitle().equals(bookTitle) && !book.isAvailable()) {
                    book.setAvailable(true);
                    JOptionPane.showMessageDialog(transactionsFrame, "Book '" + bookTitle + "' returned!");
                    returnBookField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(transactionsFrame, "Book was not issued", "Error", JOptionPane.ERROR_MESSAGE);
        });

        transactionsFrame.add(new JLabel("Issue Book:"));
        transactionsFrame.add(issueBookField);
        transactionsFrame.add(issueButton);

        transactionsFrame.add(new JLabel("Return Book:"));
        transactionsFrame.add(returnBookField);
        transactionsFrame.add(returnButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            transactionsFrame.dispose();
            mainMenuScreen();
        });
        transactionsFrame.add(backButton);

        transactionsFrame.setVisible(true);
    }

    // Reports Screen
    private static void reportsScreen() {
        JFrame reportsFrame = new JFrame("Available Books");
        reportsFrame.setSize(400, 400);
        reportsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reportsFrame.setLayout(new FlowLayout());

        for (Book book : books) {
            String availability = book.isAvailable() ? "Available" : "Issued";
            reportsFrame.add(new JLabel(book.getTitle() + " by " + book.getAuthor() + " - " + availability));
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            reportsFrame.dispose();
            mainMenuScreen();
        });
        reportsFrame.add(backButton);

        reportsFrame.setVisible(true);
    }

    // Book class
    static class Book {
        private String title;
        private String author;
        private boolean available;

        public Book(String title, String author, boolean available) {
            this.title = title;
            this.author = author;
            this.available = available;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }

    // User class
    static class User {
        private String username;
        private String password;
        private String role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}
