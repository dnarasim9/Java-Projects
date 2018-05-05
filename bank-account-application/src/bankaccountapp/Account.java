package bankaccountapp;

public abstract class Account implements BaseInterestRate {
  // Common properties for all types of accounts
  private String name;
  private String ssn;
  private double initDeposit;
  protected String accountNumber;
  protected double rate;
  private double balance;

  // Constructor to initialize common properties and methods
  public Account(String name, String ssn, double initDeposit) {
    this.name = name;
    this.ssn = ssn;
    this.initDeposit = initDeposit;
    this.balance = initDeposit;
    this.accountNumber = setAccountNumber();
    setRate();
  }

  // Common methods for all types of accounts

  public String setAccountNumber() {
    String ssnLastTwoDigits = ssn.substring(ssn.length()-2, ssn.length());
    int fiveDigitRandomNumber = (int) (Math.random() * Math.pow(10, 5));
    int threeDigitRandomNumber = (int) (Math.random() * Math.pow(10, 3));
    this.accountNumber = ssnLastTwoDigits + fiveDigitRandomNumber + threeDigitRandomNumber;
    return this.accountNumber;
  }

  public abstract void setRate();

  public void deposit(double amount) {
    System.out.println("You successfully deposited $" + amount + " to account " + accountNumber);
    balance = balance + amount;
    viewBalance();
  }

  public void withdraw(double amount) {
    System.out.println("You successfully withdrew $" + amount + " from account " + accountNumber);
    balance = balance - amount;
    viewBalance();
  }

  public void transfer(String toWhere, double amount) {
    System.out.println("You successfully transferred $" + amount + " from account " + accountNumber + " to " + toWhere);
    balance = balance - amount;
    viewBalance();
  }

  public void viewBalance() {
    System.out.println("Your balance is: $" + balance);
  }

  public void showInfo() {
    System.out.println();
    System.out.println("name: " + name + ", "
        + "ssn: " + ssn + ", "
        + "initDeposit: " + initDeposit);
  }

}
