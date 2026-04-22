import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
class BookTemplate{
    String name;
    String author;
    public BookTemplate(String name,String author){
        this.name=name;
        this.author=author;
    }
}
class Book{
    

    static List<BookTemplate> bookList=new ArrayList<>();
    public boolean ValidateAdmin(String name,String Password){
        return name.equals("admin") && Password.equals("admin123");
    }
    public void addBook(String name,String author){
        bookList.add(new BookTemplate(name,author));
    }
    public void updateBook(int index){
        System.out.println("1. Update Name");
        System.out.println("2. Update Author");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();
        sc.nextLine();
        if(index>=0 && index<bookList.size()){
            switch(choice){
                case 1:
                    System.out.println("Enter New Name:");
                    String newName=sc.nextLine();
                    bookList.get(index).name=newName;
                    break;
                case 2:
                    System.out.println("Enter New Author:");
                    String newAuthor=sc.nextLine();
                    bookList.get(index).author=newAuthor;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    public void deleteBook(int index){
        if(index>=0 && index<bookList.size()){
            bookList.remove(index);
        }else{
            System.out.println("Invalid index");
        }
    }
    public void availableBooks(){
        System.out.println("-----Available Books-----");
        if(bookList.isEmpty()){
            System.out.println("No books available");
            return;
        }
        int j=1;
        for(BookTemplate i:bookList){
            System.out.println((j++)+". "+i.name+" by "+i.author);
        }
    }
}
class User{
    private String name;
    private String password;
    private Map<String, LocalDate> borrowedBooks;
    public User(String name, String password){
        this.name=name;
        this.password=password;
        this.borrowedBooks=new HashMap<>();
    }
    public String getName(){
        return name;
    }
    public boolean authenticate(String password){
        return this.password.equals(password);
    }
    public void AddBorrowedBook(String BookTitle,String Date){
        LocalDate borrowDate;

            if(Date.isEmpty()){
                borrowDate=LocalDate.now();
            } else {
                borrowDate=LocalDate.parse(Date);
            }

        borrowedBooks.put(BookTitle, borrowDate);
    }
    public void returnBook(String BookTitle){
        if(borrowedBooks.containsKey(BookTitle)){
            borrowedBooks.remove(BookTitle);
        }else{
            System.out.println("Book not found in borrowed list");
        }
    }
    public void printBorrowedBooks(){
        System.out.println("-----Borrowed Books-----");
        if(borrowedBooks.isEmpty()){
            System.out.println("No borrowed books");
            return;
        }
        for(Map.Entry<String, LocalDate> entry:borrowedBooks.entrySet()){
            LocalDate dateBefore=entry.getValue(); 
            LocalDate dateAfter=LocalDate.now();                    

            long daysBetween=ChronoUnit.DAYS.between(dateBefore, dateAfter);
            System.out.println("Book: "+entry.getKey()+" | Borrowed on: "+entry.getValue()+"| Fine:"+(daysBetween>14?(daysBetween-14)*1:0)+"Rs | Due in: "+(14-daysBetween)+" days");
        }
    }
    public void availableBooks(){
        System.out.println("-----Available Books-----");
        if(Book.bookList.isEmpty()){
            System.out.println("No books available");
            return;
        }
        for(BookTemplate i:Book.bookList){
            System.out.println(i.name+" by "+i.author);
        }
    }
    public String SearchBook(String title){
        int j=1;
        for(BookTemplate i:Book.bookList){
           if(i.name.toLowerCase().contains(title.toLowerCase()) ||
   i.author.toLowerCase().contains(title.toLowerCase())){
                System.out.println((j++)+" "+i.name+" by "+i.author);
            }
        }
        System.out.println("Select the book number to borrow or 0 to cancel:");
        Scanner sc=new Scanner(System.in);
        int choice=sc.nextInt();
        j=1;
        for(BookTemplate i:Book.bookList){
           if(i.name.toLowerCase().contains(title.toLowerCase()) ||
   i.author.toLowerCase().contains(title.toLowerCase())){
                if(choice == j){
                    return i.name;
                }
                j++;
            }
        }
        return "invalid Book";
    }
}
public class LibraryManagement {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        List<User> users=new ArrayList<>();
        Book book=new Book();
        while(true){
            System.out.println("1.Login");
            System.out.println("2.Exit");
            int mainChoice=sc.nextInt();
            sc.nextLine();
            if(mainChoice == 2){
                System.out.println("Exiting application...");
                break;
            }
            System.out.println("Enter Usernamer:");
            String name= sc.nextLine();
            System.out.println("Enter Password:");
            String password=sc.nextLine();
            if(book.ValidateAdmin(name, password)){
                System.out.println("Admin Login Successful");
                while(true){
                    System.out.println("1. Add Book");
                    System.out.println("2. Update Book");
                    System.out.println("3. Delete Book");
                    System.out.println("4. Logout");
                    int choice=sc.nextInt();
                    sc.nextLine();
                    switch(choice){
                        case 1:
                            System.out.println("Enter Book Title:");
                            String title=sc.nextLine();
                            System.out.println("Enter Book Author:");
                            String author=sc.nextLine();
                            book.addBook(title, author);
                            break;
                        case 2:
                            book.availableBooks();
                            System.out.println("Enter Book Index to Update:");
                            int index=sc.nextInt();
                            sc.nextLine();
                            
                            book.updateBook(index-1);
                            break;
                        case 3:
                            book.availableBooks();
                            System.out.println("Enter Book Index to Delete:");
                            int delIndex=sc.nextInt();
                            sc.nextLine();
                            book.deleteBook(delIndex-1);
                            break;
                        case 4:
                            System.out.println("Logging out...");
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                    if(choice == 4){
                        break;
                    }
                }
            }else{
                User user=null;
                for(User u:users){
                    if(u.getName().equals(name)){
                        user=u;
                        break;
                    }
                }
                if(user==null){
                    System.out.println("New user! Registering...");
                    user=new User(name, password);
                    users.add(user);
                    System.out.println("User Registration Successful");
                }
                else {
                    if(user.authenticate(password)){
                        System.out.println("Login Successful");
                    }else{
                        System.out.println("Incorrect Password");
                        continue;
                    }
                
                    while(true){
                        System.out.println("1. View Available Books");
                        System.out.println("2. Search and Borrow Book");
                        System.out.println("3. Return Book");
                        System.out.println("4. View Borrowed Books");
                        System.out.println("5. Logout");
                        int choice=sc.nextInt();
                        sc.nextLine();
                        switch(choice){
                            case 1:
                                user.availableBooks();
                                break;
                            case 2:
                                System.out.println("Enter Book Title to Search:");
                                String searchTitle=sc.nextLine();
                                String bookTitle=user.SearchBook(searchTitle);
                                if(!bookTitle.equals("invalid Book")){
                                    user.AddBorrowedBook(bookTitle,"");
                                    System.out.println("Book Borrowed: "+bookTitle);
                                }else{
                                    System.out.println("Book not found");
                                }
                                break;
                            case 3:
                                user.printBorrowedBooks();
                                System.out.println("Enter Book Title to Return:");
                                String returnTitle=sc.nextLine();
                                user.returnBook(returnTitle);
                                System.out.println("Book Returned: "+returnTitle);
                                break;
                            case 4:
                                user.printBorrowedBooks();
                                break;
                            case 5:
                                System.out.println("Logging out...");
                                break;
                            default:
                                System.out.println("Invalid choice");
                        }
                        if(choice == 5){
                            break;
                        }
                    }
                }
            }
        }
    }
}
