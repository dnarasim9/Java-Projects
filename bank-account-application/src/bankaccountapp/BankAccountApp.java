package bankaccountapp;

import java.util.LinkedList;
import java.util.List;

public class BankAccountApp {

  public static void main(String[] args) {

    List<Account> accounts = new LinkedList<Account>();

    List<String[]> newAccountHolders = utilities.CSV.read("./NewBankAccounts.csv");

    for (String[] accountHolder : newAccountHolders) {
      String name = accountHolder[0];
      String ssn = accountHolder[1];
      String accountType = accountHolder[2];
      Double initDeposit = Double.parseDouble(accountHolder[3]);

      if (accountType.equals("Savings")) {
        accounts.add(new Savings(name, ssn, initDeposit));
      } else if (accountType.equals("Checking")) {
        accounts.add(new Checking(name, ssn, initDeposit));
      } else {
        System.err.println("Could not read accountType, Creating a checking account by default");
        accounts.add(new Checking(name, ssn, initDeposit));
      }

    }

    for (Account account : accounts) {
      account.showInfo();
    }

    // Withdraw $500 from a random account and print balance for that account
    System.out.println();
    accounts.get((int) (Math.random() * accounts.size())).withdraw(500);

  }

}
