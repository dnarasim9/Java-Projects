package studentdatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Student {
  private String firstName;
  private String lastName;
  private String classLevel;
  private String studentId;
  private List<String> courses;
  private static int costPerCourse = 600;
  private int balance;
  private static int id = 1001;

  // Constructor for the student class
  public Student() {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter the student's first name: ");
    this.firstName = input.nextLine();
    System.out.print("Enter the student's last name: ");
    this.lastName = input.nextLine();

    System.out.print("Class level codes: \n1 for Freshman\n2 for Sophomore\n3 for Junior\n4 for Senior\n");
    System.out.print("Enter student class level: ");
    int classLevelCode = input.nextInt();
    this.classLevel = getClassLevel(classLevelCode);

    this.studentId = setStudentId(classLevelCode);

    System.out.print("Courses offered: Chemistry, Computer Science, English, History, Mathematics\n");
    this.courses = new ArrayList<>();
    this.courses = enrollInCourses();

    this.balance = viewBalance();
    payTuition();

  }

  // Get Class Level
  private String getClassLevel(int classLevelCode) {
    if (classLevelCode == 1) {return "Freshman";}
    else if (classLevelCode == 2) {return "Sophomore";}
    else if (classLevelCode == 3) {return "Junior";}
    else if (classLevelCode ==4) {return "Senior";}
    else throw new IllegalArgumentException("Please enter one of the given class level codes");
  }

  private String setStudentId(int classLevelCode) {
    this.studentId = classLevelCode + "" + id;
    id++;
    return studentId;
  }

  // Enroll in courses
  private List<String> enrollInCourses() {
    Set<String> courseSet = new HashSet<>(Arrays.asList("Chemistry", "Computer Science", "English", "History", "Mathematics"));

    do {
      Scanner input = new Scanner(System.in);
      System.out.print("Enter name of the course to enroll in, Q to finish and quit: ");
      String courseEnrolled = input.nextLine();

      if (courseSet.contains(courseEnrolled)) {
        this.courses.add(courseEnrolled);
      } else if (courseEnrolled.equals("Q")) {
        break;
      } else {
        throw new IllegalArgumentException("Please enter one of the above mentioned course names or Q");
      }
    } while (1 != 0);

    return this.courses;

  }

  // View balance method
  private int viewBalance() {
    return (this.courses.size() * costPerCourse);
  }

  // Pay tuition method
  private void payTuition() {
    int balance = viewBalance();
    System.out.println("Your current balance: $" + balance);
    System.out.print("Enter payment amount: $");
    Scanner input = new Scanner(System.in);
    int payment = input.nextInt();
    this.balance = this.balance - payment;
    System.out.println("Payment of $" + payment + " was successful, your new balance is: $" + this.balance);
  }

  // toString method
  public String toString() {
    return "firstName: " + firstName + ", "
        + "lastName: " + lastName + ", "
        + "studentId: " + studentId + ", "
        + "classLevel: " + classLevel + ", "
        + "coursesEnrolled: " + courses + ", "
        + "balance: " + balance;
  }

}
