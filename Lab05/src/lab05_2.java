import java.util.ArrayList;

/**
 * Created by Ari Sanders on 13-Jul-15.
 */
public class lab05_2 {
    public static ArrayList<Double>[] results = new ArrayList[2];

    public static void main(String[] args) {
        int howMany = 10000;
        ArrayList<Double> result = new ArrayList<>();

        System.out.println("Building new array of " + howMany + " doubles.");
        Double[] array = new Double[howMany];
        System.out.println("Done.\n");

        System.out.println("Filling with random numbers.");
        for (int i = 0; i < howMany; i++) {
            array[i] = Math.random();
        }
        System.out.println("Done.\n");

        System.out.println("Sorting using quicksort.");
        long startTime = System.currentTimeMillis();
        ////////////////////////////////////////////
        //Run first loop of sort
        if (array.length <= 2) {
            if (array.length == 2 && array[0] > array[1]) {
                result.add(array[1]);
                result.add(array[0]);
            }
            else {
                result.add(array[0]);
            }
        }
        else {
            ArrayList<Double> less = new ArrayList<>();
            ArrayList<Double> more = new ArrayList<>();
            Double pivot;
            int pivotCount = 0;

            //Calculate pivot using median-of-three
            double first = array[0];
            double middle = array[array.length / 2];
            double last = array[array.length - 1];
            if ((first <= middle && first >= last) || (first >= middle && first <= last)) {
                pivot = first;
            } else if ((last <= middle && last >= first) || (last >= middle && last <= first)) {
                pivot = last;
            } else {
                pivot = middle;
            }

            for (double element : array) {
                if (element == pivot) {
                    pivotCount++;
                } else if (element < pivot) {
                    less.add(element);
                } else {
                    more.add(element);
                }
            }

            Sorter s0 = new Sorter(0, less);
            s0.start();
            Sorter s1 = new Sorter(1, more);
            s1.start();
            try {
                s0.join();
                s1.join();
            }
            catch (InterruptedException e){
                System.out.println("Someone interrupted our thread! This is not good.");
            }
            result.addAll(results[0]);
            for (; pivotCount > 0; pivotCount--) {
                result.add(pivot);
            }
            result.addAll(results[1]);
        }
        ////////////////////////////////////////////

        long endTime = System.currentTimeMillis();
        System.out.println("Done. Operation took " + ((double) (endTime - startTime) / 1000) + " seconds.\n");

        System.out.println("Checking for correct sort.");
        double curr;
        double prev = 0;
        for (double element : result) {
            curr = element;
            if (curr < prev) {
                System.out.println(curr + " should not be after " + prev);
            }
            prev = curr;
        }
        System.out.println("Done.");
    }

    /**
     * Performs a quicksort on the input ArrayList
     * @param array ArrayList to sort
     * @return Sorted ArrayList
     */
    public static ArrayList<Double> sort(ArrayList<Double> array) {
        if (array.size() <= 2) {
            if (array.size() == 2 && array.get(0) > array.get(1)) {
                Double temp = array.get(0);
                array.set(0, array.get(1));
                array.set(1, temp);
            }
            return array;
        }

        ArrayList<Double> less = new ArrayList<>();
        ArrayList<Double> more = new ArrayList<>();
        Double pivot;
        int pivotCount = 0;

        //Calculate pivot using median-of-three
        double first = array.get(0);
        double middle = array.get(array.size() / 2);
        double last = array.get(array.size() - 1);
        if ((first <= middle && first >= last) || (first >= middle && first <= last)) {
            pivot = first;
        } else if ((last <= middle && last >= first) || (last >= middle && last <= first)) {
            pivot = last;
        } else {
            pivot = middle;
        }

        for (double element : array) {
            if (element == pivot) {
                pivotCount++;
            } else if (element < pivot) {
                less.add(element);
            } else {
                more.add(element);
            }
        }

        int total = 0;
        for (Double element : sort(less)) {
            array.set(total++, element);
        }
        for (; pivotCount > 0; pivotCount--) {
            array.set(total++, pivot);
        }
        for (double element : sort(more)) {
            array.set(total++, element);
        }

        return array;
    }

    static class Sorter extends Thread{
        int id;
        ArrayList<Double> array;

        public Sorter(int id, ArrayList<Double> array){
            this.id = id;
            this.array = array;
        }

        public void run(){
            results[id] = sort(array);
        }
    }
}
