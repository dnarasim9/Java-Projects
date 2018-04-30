package emailapp;

import java.util.Scanner;

public class EmailApp {

    public static void main(String[] args) {
        // Ask the user for firstName and lastName
        System.out.print("Enter first name: ");
        Scanner input = new Scanner(System.in);
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        Email email = new Email(firstName, lastName);
        System.out.println(email.toString());

    }

}
