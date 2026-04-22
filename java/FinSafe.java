import java.util.*;
class InSufficientFundsException extends Exception{
    public InSufficientFundsException(String message){
        super(message);
    }
}
class Account{
    private String accName;
    private double balance;
    private List<Double> transactionHistory;
    public Account(String accName,double balance){
        this.accName=accName;
        this.balance=balance;
        this.transactionHistory=new ArrayList<>();
    }
    public double getBalance(){
        return balance;
    }
    public void deposit(double amount) throws InSufficientFundsException{
        if(amount<=0){
            throw new InSufficientFundsException("Deposit must be positive");
        }
        balance+=amount;
        addTransaction(amount);
        System.out.println("Deposited: "+amount);
    }
    public void withdraw(double amount) throws InSufficientFundsException{
        processTransaction(amount);
        balance-=amount;
        addTransaction(-amount);
        System.out.println("Withdrawn: "+amount);
    }
    public void processTransaction(double amount) throws InSufficientFundsException{
        if(amount<=0){
            throw new InSufficientFundsException("Amount must be positive");
        }
        if(amount>balance){
            throw new InSufficientFundsException("Insufficient Balance");
        }
    }
    private void addTransaction(double amount){
        if(transactionHistory.size()==5){
            transactionHistory.remove(0);
        }
        transactionHistory.add(amount);
    }
    public void printMiniStatement(){
        System.out.println("-----Last 5 Transactions-----");
        if(transactionHistory.isEmpty()){
            System.out.println("No Transactions yet");
            return;
        }
        for(double i:transactionHistory){
            System.out.println(i>0?"Deposit: "+i : "Withdraw: "+(-i));
        }
    }
}
public class FinSafe {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        Map<String, Account> accounts = new HashMap<>();

        while(true){  // LOGIN LOOP

            System.out.print("\nEnter Account Holder Name (or type 'exit'): ");
            String name = sc.nextLine();

            if(name.equalsIgnoreCase("exit")){
                System.out.println("Exiting application...");
                break;
            }

            Account acc;

            // Check if user exists
            if(accounts.containsKey(name)){
                acc = accounts.get(name);
                System.out.println("✅ Welcome back, " + name);
            } 
            else {
                System.out.print("New user! Enter Initial Balance: ");
                double balance = sc.nextDouble();
                sc.nextLine(); // FIX buffer

                acc = new Account(name, balance);
                accounts.put(name, acc);

                System.out.println("✅ Account created for " + name);
            }

            // USER MENU LOOP
            while(true){
                System.out.println("\n===== FinSafe Menu =====");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. View Balance");
                System.out.println("4. Mini Statement");
                System.out.println("5. Logout");

                System.out.print("Choose option: ");
                int choice = sc.nextInt();

                try{
                    switch(choice){
                        case 1:
                            System.out.print("Enter amount: ");
                            double d = sc.nextDouble();
                            acc.deposit(d);
                            break;

                        case 2:
                            System.out.print("Enter amount: ");
                            double d1 = sc.nextDouble();
                            acc.withdraw(d1);
                            break;

                        case 3:
                            System.out.println("💰 Balance: " + acc.getBalance());
                            break;

                        case 4:
                            acc.printMiniStatement();
                            break;

                        case 5:
                            System.out.println("🔒 Logged out from " + name);
                            sc.nextLine(); // FIX buffer
                            break;

                        default:
                            System.out.println("Invalid choice!");
                    }

                    if(choice == 5) break;

                } 
                catch (InSufficientFundsException e) {
                    System.out.println(e.getMessage());
                } 
                catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } 
                catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        }
    }
}
