import pypyodbc
from tkinter import *
from tkinter import messagebox
from tkcalendar import DateEntry

# Database connection
def connect_db():
    conn_str = r'DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\Users\stars\OneDrive\Documents\Library.accdb;'
    conn = pypyodbc.connect(conn_str)
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            password TEXT NOT NULL,
            role TEXT NOT NULL
        )
    ''')
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS Books (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            author TEXT NOT NULL,
            is_available BOOLEAN NOT NULL DEFAULT 1
        )
    ''')
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS Transactions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER,
            book_id INTEGER,
            issue_date TEXT,
            return_date TEXT,
            fine_paid BOOLEAN DEFAULT 0,
            remarks TEXT,
            FOREIGN KEY(user_id) REFERENCES Users(id),
            FOREIGN KEY(book_id) REFERENCES Books(id)
        )
    ''')
    conn.commit()
    return conn

# Login window
class LoginWindow:
    def __init__(self, root):
        self.root = root
        self.root.title("Library Management System - Login")
        self.root.geometry("300x200")

        Label(root, text="Username").grid(row=0, column=0)
        self.username_entry = Entry(root)
        self.username_entry.grid(row=0, column=1)

        Label(root, text="Password").grid(row=1, column=0)
        self.password_entry = Entry(root, show='*')
        self.password_entry.grid(row=1, column=1)

        Button(root, text="Login", command=self.login).grid(row=2, columnspan=2)

    def login(self):
        username = self.username_entry.get()
        password = self.password_entry.get()

        conn = connect_db()
        cursor = conn.cursor()
        cursor.execute("SELECT role FROM Users WHERE username=? AND password=?", (username, password))
        result = cursor.fetchone()

        if result:
            role = result[0]
            if role == "Admin":
                AdminWindow(Toplevel(self.root))
            else:
                UserWindow(Toplevel(self.root))
            self.root.destroy()
        else:
            messagebox.showerror("Error", "Invalid credentials")
        conn.close()

# Admin window
class AdminWindow:
    def __init__(self, root):
        self.root = root
        self.root.title("Admin Dashboard")
        self.root.geometry("300x200")

        Button(root, text="Add Book", command=self.add_book).grid(row=0, column=0)
        Button(root, text="Update Book", command=self.update_book).grid(row=1, column=0)
        Button(root, text="User Management", command=self.manage_users).grid(row=2, column=0)

    def add_book(self):
        AddBookWindow(Toplevel(self.root))

    def update_book(self):
        pass  # Add functionality later

    def manage_users(self):
        pass  # Add functionality later

# Add book window
class AddBookWindow:
    def __init__(self, root):
        self.root = root
        self.root.title("Add Book")
        self.root.geometry("300x200")

        Label(root, text="Title").grid(row=0, column=0)
        self.title_entry = Entry(root)
        self.title_entry.grid(row=0, column=1)

        Label(root, text="Author").grid(row=1, column=0)
        self.author_entry = Entry(root)
        self.author_entry.grid(row=1, column=1)

        Button(root, text="Add Book", command=self.add_book).grid(row=2, columnspan=2)

    def add_book(self):
        title = self.title_entry.get()
        author = self.author_entry.get()

        if title and author:
            conn = connect_db()
            cursor = conn.cursor()
            cursor.execute("INSERT INTO Books (title, author) VALUES (?, ?)", (title, author))
            conn.commit()
            conn.close()
            messagebox.showinfo("Success", "Book added successfully")
            self.root.destroy()
        else:
            messagebox.showerror("Error", "All fields are required")

# User window
class UserWindow:
    def __init__(self, root):
        self.root = root
        self.root.title("User Dashboard")
        self.root.geometry("300x200")

        Button(root, text="Issue Book", command=self.issue_book).grid(row=0, column=0)
        Button(root, text="Return Book", command=self.return_book).grid(row=1, column=0)
        Button(root, text="Pay Fine", command=self.pay_fine).grid(row=2, column=0)

    def issue_book(self):
        IssueBookWindow(Toplevel(self.root))

    def return_book(self):
        pass  # Add functionality later

    def pay_fine(self):
        pass  # Add functionality later

# Issue book window
class IssueBookWindow:
    def __init__(self, root):
        self.root = root
        self.root.title("Issue Book")
        self.root.geometry("300x200")

        Label(root, text="Book Title").grid(row=0, column=0)
        self.title_entry = Entry(root)
        self.title_entry.grid(row=0, column=1)

        Label(root, text="Issue Date").grid(row=1, column=0)
        self.issue_date = DateEntry(root)
        self.issue_date.grid(row=1, column=1)

        Label(root, text="Return Date").grid(row=2, column=0)
        self.return_date = DateEntry(root)
        self.return_date.grid(row=2, column=1)

        Button(root, text="Issue Book", command=self.issue_book).grid(row=3, columnspan=2)

    def issue_book(self):
        title = self.title_entry.get()
        issue_date = self.issue_date.get_date()
        return_date = self.return_date.get_date()

        conn = connect_db()
        cursor = conn.cursor()
        cursor.execute("SELECT id FROM Books WHERE title=? AND is_available=1", (title,))
        result = cursor.fetchone()

        if result:
            book_id = result[0]
            cursor.execute("INSERT INTO Transactions (user_id, book_id, issue_date, return_date) VALUES (?, ?, ?, ?)", (1, book_id, issue_date, return_date))
            cursor.execute("UPDATE Books SET is_available=0 WHERE id=?", (book_id,))
            conn.commit()
            messagebox.showinfo("Success", "Book issued successfully")
            self.root.destroy()
        else:
            messagebox.showerror("Error", "Book not available")
        conn.close()

# Main function
def main():
    root = Tk()
    LoginWindow(root)
    root.mainloop()

if __name__ == "__main__":
    main()
