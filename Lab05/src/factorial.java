/**
 * Created by Ari Sanders on 06-Jul-15.
 */
public class factorial {
    public static void main(String[] args) {
        long n = 20;
        long answer = factorial(n);
        System.out.println("answer is " + answer);
        System.out.println("iteratively, the answer would be " + factorialIter(n));
    }
    public static long factorial(long number){
        if (number <= 1){
            return 1;
        }
        return number * factorial(number - 1);
    }

    public static long factorialIter(long number){
        long out = 1;
        while (number>1){
            out *= number--;
        }
        return out;
    }
}
