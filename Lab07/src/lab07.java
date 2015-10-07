import java.io.IOException;
import java.util.Random;
/**
 * Created by Ari Sanders on 19-Jul-15.
 */

public class lab07 {
    private final static int ARRAYSIZE = 1000;
    private final static int VIRTRATIO = 100;

    public static void main(String[] args) throws Exception {
        vMem array;
        // make an array of ARRAYSIZE elements
        try {
            array = new vMem(ARRAYSIZE, ARRAYSIZE / VIRTRATIO);
        }
        catch (IOException e){
            System.out.println("Error: Could not load memory.");
            return;
        }

        Random random = new Random();

        int index;
        long value;
        long gotten = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < ARRAYSIZE * ARRAYSIZE; i++){
            index = random.nextInt(ARRAYSIZE);
            value = random.nextLong();
            /*if (i % ARRAYSIZE == 0) {
                //Adding this output makes it easier to see how far it is, but it adds 0.9 seconds on 1000/100
                System.out.println(i + " is " + (double)i / (ARRAYSIZE * ARRAYSIZE) * 100 + "%");
            }*/

            try {
                array.put(index, value);
                gotten = array.get(index);
            }
            catch (IndexOutOfBoundsException e){
                long upper = ARRAYSIZE - 1;
                System.out.println("The value " + index + " is outside 0.." + upper);
                e.printStackTrace();
            }

            // check for error
            if (gotten != value){
                System.out.println("error at " + i);
                return;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Elapsed time (milliseconds) is " + (endTime - startTime) / 1000000);
        System.out.println("Test complete.");

        //Additionally requested functionality
        for (int i = 0; i < ARRAYSIZE; i++){
            array.put(i, ARRAYSIZE - i);
        }
        long curr;
        long prev = ARRAYSIZE + 1;
        for (int i = 0; i < ARRAYSIZE; i++){
            curr = array.get(i);
            if (curr > prev){
                System.out.println(curr + " should not be after " + prev + " @ " + i);
                //throw new Exception();
            }
            prev = curr;
            //System.out.println(curr);
        }
        System.out.println("Complete.");
    }
}
