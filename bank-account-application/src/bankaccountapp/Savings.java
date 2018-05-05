package bankaccountapp;

public class Savings extends Account {
  // Properties specific to savings account
  private int safetyDepositBoxId;
  private int safetyDepositBoxKey;

  // Constructor to initialize properties and methods
  public Savings(String name, String ssn, double initDeposit) {
    super(name, ssn, initDeposit);
    setSafetyDepositBoxInfo();
    showInfo();
  }

  // Methods specific to savings account

  public void setRate() {
    rate = getBaseInterestRate() - 0.25;
  }

  public void setSafetyDepositBoxInfo() {
    accountNumber = "1" + accountNumber;
    this.safetyDepositBoxId = (int) (Math.random() * Math.pow(10, 3));
    this.safetyDepositBoxKey = (int) (Math.random() * Math.pow(10, 4));
  }

  public void showInfo() {
    super.showInfo();
    System.out.println("    accountType: Savings, "
        + "accountNumber: " + accountNumber + ", "
        + "interestRate: " + rate + ", "
        + "safetyDepositBoxId: " + safetyDepositBoxId + ", "
        + "safetyDepositBoxKey: " + safetyDepositBoxKey);
    System.out.println("******************");
  }

}
