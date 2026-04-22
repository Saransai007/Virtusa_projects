CREATE TABLE Books (
    BookID INT PRIMARY KEY,
    Title VARCHAR(100),
    Author VARCHAR(100),
    Category VARCHAR(50)
);
CREATE TABLE Students (
    StudentID INT PRIMARY KEY,
    Name VARCHAR(100),
    Department VARCHAR(50),
    JoinDate DATE
);
CREATE TABLE IssuedBooks (
    IssueID INT PRIMARY KEY,
    BookID INT,
    StudentID INT,
    IssueDate DATE default CURRENT_DATE,
    ReturnDate DATE,
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);
//1. List all the books that are currently issued and have not been returned for more than 14 days.
SELECT s.StudentID,s.Name,b.title,i.IssueDate from students s JOIN IssuedBooks i on s.StudentID=i.StudentID
JOIN Books b on b.BookID=i.BookID where i.ReturnDate IS NULL
AND CURRENT_DATE-i.IssueDate>14
//2. Find the most popular book category based on the number of times books from that category have been issued.
SELECT b.Category from books b GROUP BY b.category 
ORDER BY COUNT(*) DESC LIMIT 1;
//3. Identify students who have not issued any books in the last 3 years and remove their records from the database.
DELETE FROM IssuedBooks where StudentId IN(
	SELECT StudentID FROM Students 
	where StudentID NOT IN(
		SELECT StudentID from IssuedBooks
		WHERE IssueDate >= CURRENT_DATE - INTERVAL '3 years'
	)
);

DELETE FROM Students Where StudentID NOT IN(
	SELECT StudentID FROM IssuedBooks
	WHERE IssueDate >= CURRENT_DATE - INTERVAL '3 years'
);