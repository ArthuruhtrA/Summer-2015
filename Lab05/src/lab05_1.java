import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ari Sanders on 15-Jul-15.
 */
public class lab05_1 {
    public static void main(String[] args) {
        int howMany = 10000;

        System.out.println("Building new array of " + howMany + " doubles.");
        Double[] array = new Double[howMany];
        System.out.println("Done.\n");

        System.out.println("Filling with random numbers.");
        for (int i = 0; i < howMany; i++) {
            array[i] = Math.random();
        }
        System.out.println("Done.\n");

        System.out.println("Sorting using mergesort.");
        long startTime = System.currentTimeMillis();
        List<Double> input = new ArrayList<>();
        for (Double element : array){
            input.add(element);
        }
        Object[] result = sort(input).toArray();
        long endTime = System.currentTimeMillis();
        System.out.println("Done. Operation took " + ((double) (endTime - startTime) / 1000) + " seconds.\n");

        System.out.println("Checking for correct sort.");
        double curr;
        double prev = 0;
        for (Object element : result) {
            curr = (Double)element;
            if (curr < prev) {
                System.out.println(curr + " should not be after " + prev);
            }
            prev = curr;
        }
        System.out.println("Done.");
    }

    /**
     * Performs a mergesort on the input ArrayList
     * @param array ArrayList to be sorted
     * @return Sorted ArrayList
     */
    public static List<Double> sort(List<Double> array){
        int size = array.size();

        if (size <= 2){
            if (size == 2 && array.get(0) > array.get(1)){
                Double temp = array.get(0);
                array.set(0, array.get(1));
                array.set(1, temp);
            }
            return array;
        }
        else {
            List<Double> first = new ArrayList<>();
            first.addAll(array.subList(0, size / 2 ));
            first = sort(first);
            List<Double> second = new ArrayList<>();
            second.addAll(array.subList(size / 2, size));
            second = sort(second);

            int j = 0;
            int k = 0;
            int fSize = first.size();
            int sSize = second.size();
            for (int i = 0; i < size; i++){
                if (j < fSize && k < sSize) {
                    if (first.get(j) < second.get(k)) {
                        array.set(i, first.get(j++));
                    } else {
                        array.set(i, second.get(k++));
                    }
                }
                else if (j < fSize){
                    array.set(i, first.get(j++));
                }
                else {
                    array.set(i, second.get(k++));
                }
            }

            return array;
        }
    }

}
