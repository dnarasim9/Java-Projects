package studentdatabase;

import java.util.Scanner;

public class StudentDatabaseApp {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter number of students to enroll: ");

    int numOfStudents = input.nextInt();
    Student[] students = new Student[numOfStudents];
    for (int i=0; i<numOfStudents; i++) {
      Student student = new Student();
      System.out.println(student.toString());
    }

  }

}
