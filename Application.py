import tkinter as tk
from tkinter import messagebox
from datetime import datetime, timedelta

# Simulated data store for books and users
books = [
    {"title": "My experiment with truth", "author": "M K Gandhi", "available": True},
    {"title": "Arthashashtra", "author": "Kautilya", "available": False},
]

users = [
    {"username": "Sonali", "password": "sonali1605", "role": "admin"},
    {"username": "user1", "password": "user1@123", "role": "user"},
]

# Root window
root = tk.Tk()
root.title("Library Management System")
root.geometry("600x400")

# Global variable to store logged-in user role
logged_in_role = None

# Login Screen
def login_screen():
    def validate_login():
        global logged_in_role
        username = entry_username.get()
        password = entry_password.get()
        for user in users:
            if user["username"] == username and user["password"] == password:
                logged_in_role = user["role"]
                messagebox.showinfo("Login Success", f"Welcome {user['role'].capitalize()}")
                main_menu_screen()
                return
        messagebox.showerror("Error", "Invalid credentials")

    clear_screen()
    tk.Label(root, text="Login Page", font=("Arial", 20)).pack(pady=20)
    
    tk.Label(root, text="Username").pack(pady=5)
    entry_username = tk.Entry(root)
    entry_username.pack(pady=5)
    
    tk.Label(root, text="Password").pack(pady=5)
    entry_password = tk.Entry(root, show="*")
    entry_password.pack(pady=5)
    
    tk.Button(root, text="Login", command=validate_login).pack(pady=20)

# Main Menu Screen
def main_menu_screen():
    clear_screen()

    def go_to_books():
        if logged_in_role == "admin":
            manage_books_screen()
        else:
            messagebox.showerror("Error", "Access denied")

    def go_to_transactions():
        transactions_screen()

    def go_to_reports():
        reports_screen()

    tk.Label(root, text="Library Management System", font=("Arial", 20)).pack(pady=20)
    
    tk.Button(root, text="Maintenance (Admin only)", command=go_to_books).pack(pady=10)
    tk.Button(root, text="Transactions", command=go_to_transactions).pack(pady=10)
    tk.Button(root, text="Reports", command=go_to_reports).pack(pady=10)
    tk.Button(root, text="Logout", command=login_screen).pack(pady=10)

# Maintenance (Add/Update Books)
def manage_books_screen():
    clear_screen()

    def add_book():
        title = entry_title.get()
        author = entry_author.get()
        if title and author:
            books.append({"title": title, "author": author, "available": True})
            messagebox.showinfo("Success", "Book added successfully")
            main_menu_screen()
        else:
            messagebox.showerror("Error", "All fields are mandatory")

    tk.Label(root, text="Add Book", font=("Arial", 20)).pack(pady=20)
    
    tk.Label(root, text="Title").pack(pady=5)
    entry_title = tk.Entry(root)
    entry_title.pack(pady=5)
    
    tk.Label(root, text="Author").pack(pady=5)
    entry_author = tk.Entry(root)
    entry_author.pack(pady=5)
    
    tk.Button(root, text="Add Book", command=add_book).pack(pady=20)
    tk.Button(root, text="Back", command=main_menu_screen).pack(pady=10)

# Transactions Screen
def transactions_screen():
    clear_screen()

    def issue_book():
        clear_screen()

        def issue():
            book_name = entry_book.get()
            issue_date = datetime.now().date()
            return_date = issue_date + timedelta(days=15)
            book_found = None

            for book in books:
                if book["title"] == book_name:
                    book_found = book
                    break

            if not book_found or not book_found["available"]:
                messagebox.showerror("Error", "Book is unavailable or doesn't exist")
                return

            messagebox.showinfo("Success", f"Book '{book_name}' issued!\nReturn by {return_date}")
            book_found["available"] = False
            transactions_screen()

        tk.Label(root, text="Issue Book", font=("Arial", 20)).pack(pady=20)
        
        tk.Label(root, text="Book Name").pack(pady=5)
        entry_book = tk.Entry(root)
        entry_book.pack(pady=5)
        
        tk.Button(root, text="Issue Book", command=issue).pack(pady=20)
        tk.Button(root, text="Back", command=transactions_screen).pack(pady=10)

    def return_book():
        clear_screen()

        def return_it():
            book_name = entry_book.get()
            fine_paid = chk_fine_var.get()

            for book in books:
                if book["title"] == book_name and not book["available"]:
                    book["available"] = True
                    messagebox.showinfo("Success", f"Book '{book_name}' returned!")
                    transactions_screen()
                    return

            messagebox.showerror("Error", "Book was not issued")

        tk.Label(root, text="Return Book", font=("Arial", 20)).pack(pady=20)
        
        tk.Label(root, text="Book Name").pack(pady=5)
        entry_book = tk.Entry(root)
        entry_book.pack(pady=5)
        
        chk_fine_var = tk.IntVar()
        tk.Checkbutton(root, text="Fine Paid", variable=chk_fine_var).pack(pady=5)
        
        tk.Button(root, text="Return Book", command=return_it).pack(pady=20)
        tk.Button(root, text="Back", command=transactions_screen).pack(pady=10)

    tk.Label(root, text="Transactions", font=("Arial", 20)).pack(pady=20)
    tk.Button(root, text="Issue Book", command=issue_book).pack(pady=10)
    tk.Button(root, text="Return Book", command=return_book).pack(pady=10)
    tk.Button(root, text="Back", command=main_menu_screen).pack(pady=10)

# Reports Screen
def reports_screen():
    clear_screen()

    tk.Label(root, text="Available Books", font=("Arial", 20)).pack(pady=20)
    for book in books:
        availability = "Available" if book["available"] else "Issued"
        tk.Label(root, text=f"{book['title']} by {book['author']} - {availability}").pack()

    tk.Button(root, text="Back", command=main_menu_screen).pack(pady=20)

# Clear the screen
def clear_screen():
    for widget in root.winfo_children():
        widget.destroy()

# Start with login screen
login_screen()
root.mainloop()