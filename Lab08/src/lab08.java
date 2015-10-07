import java.util.Scanner;

/**
 * Created by Ari Sanders on 15-Jul-15.
 */
public class lab08 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        intValue first;
        intValue second;
        while (true){
            System.out.print("Please input first number:\n>");
            first = new intValue(in.nextLine());
            System.out.print("Please input second number:\n>");
            second = new intValue(in.nextLine());
            System.out.print("Please choose an operation:\n 0: Addition (+)\n 1: Multiplication (*)\n>");
            switch (in.nextLine().toLowerCase()){
                case "+":case "0":case "addition":case "plus":case "add":case "sum":
                    System.out.println(first.value + " + " + second.value + " = " + first.add(second).value);
                    break;
                case "*":case "1":case "multiplication":case "times":case "multiply":case "product":
                    System.out.println(first.value + " * " + second.value + " = " + first.multiply(second).value);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
