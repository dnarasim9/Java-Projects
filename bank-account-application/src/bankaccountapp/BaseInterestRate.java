package bankaccountapp;

public interface BaseInterestRate {

  default double getBaseInterestRate() {
    return 2.5;
  }


}
