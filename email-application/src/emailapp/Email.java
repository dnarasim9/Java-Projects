package emailapp;

import java.util.Scanner;

public class Email {
    private String firstName;
    private String lastName;
    private String department;
    private String companySuffix = "mycompany.com";
    private String emailAddress;
    private String password;
    private int passwordLength = 8;
    private int mailboxCapacity = 500;
    private String alternateEmailAddress;

    // Constructor to setup email operations
    public Email(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        this.department = setDepartment();
        this.emailAddress = generateEmailAddress();
        this.password = generatePassword(passwordLength);
    }

    // Determine the department
    private String setDepartment() {
        System.out.println("Department codes:\n1 for Sales\n2 for Marketing\n3 for Development\n0 for None");
        System.out.print("Enter department code: ");
        Scanner input = new Scanner(System.in);
        int departmentCode = input.nextInt();

        if (departmentCode == 1) {this.department = "sales";}
        else if (departmentCode == 2) {this.department = "marketing";}
        else if (departmentCode == 3) {this.department = "development";}
        else if (departmentCode == 0) {this.department = "";}
        else throw new IllegalArgumentException("Please enter one of the provided options for department codes");
        return this.department;
    }

    // Generate email address
    private String generateEmailAddress() {
        if (!department.equals("")) {
            this.emailAddress = firstName + "." + lastName + "@" + department + "." + companySuffix;
        } else {
            this.emailAddress = firstName + "." + lastName + "@" + companySuffix;
        }
        return this.emailAddress;
    }

    // Generate random password
    private String generatePassword(int length) {
        String passwordSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%*";
        char[] password = new char[length];
        for (int i=0; i<length; i++) {
            int random = (int) (Math.random() * passwordSet.length());
            password[i] = passwordSet.charAt(random);
        }
        this.password = String.valueOf(password);
        return this.password;
    }

    // Set password
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {return password;}

    // Set mailbox capacity
    public void setMailboxCapacity (int mailboxCapacity) {
        this.mailboxCapacity = mailboxCapacity;
    }

    public int getMailboxCapacity() {return mailboxCapacity;}

    // Set alternate email address
    public void setAlternateEmailAddress (String alternateEmail) {
        this.alternateEmailAddress = alternateEmail;
    }

    public String getAlternateEmailAddress() {return alternateEmailAddress;}

    // toString method
    public String toString() {
        return "firstName: " + firstName + ", "
                + "lastName: " + lastName + ", "
                + "email: " + emailAddress + ", "
                + "password: " + password + ", "
                + "mailboxCapacity: " + mailboxCapacity + "MB";
    }

}
