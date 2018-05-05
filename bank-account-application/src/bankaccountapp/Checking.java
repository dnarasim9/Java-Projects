package bankaccountapp;

public class Checking extends Account {
  // Properties specific to checking account
  private long debitCardNumber;
  private int debitCardPin;

  // Constructor to initialize properties and methods
  public Checking(String name, String ssn, double initDeposit) {
    super(name, ssn, initDeposit);
    setDebitCardInfo();
    showInfo();

  }

  // Methods specific to checking account

  public void setRate() {
    rate = getBaseInterestRate() * 0.15;
  }

  private void setDebitCardInfo() {
    accountNumber = "2" + accountNumber;
    this.debitCardNumber = (long) (Math.random() * Math.pow(10, 12));
    this.debitCardPin = (int) (Math.random() * Math.pow(10, 4));
  }

  public void showInfo() {
    super.showInfo();
    System.out.println("    accountType: Checking, "
        + "accountNumber: " + accountNumber + ", "
        + "interestRate: " + rate + ", "
        + "debitCardNumber: " + debitCardNumber + ", "
        + "debitCardPin: " + debitCardPin);
    System.out.println("******************");
  }

}
