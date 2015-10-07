import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Ari Sanders on 08-Jul-15.
 */
public class lab06 {
    enum Algorithm {
        QUICKSORT,
        MERGESORT,
        INSERTION_SORT,
        BUBBLE_SORT;

        static Algorithm fromString(String name){
            switch (name.toUpperCase()){
                case "0":case "QUICK SORT":case "QUICK_SORT":case "QUICKSORT":case "QUICK":
                    return Algorithm.QUICKSORT;
                case "1":case "MERGE SORT":case "MERGE_SORT":case "MERGESORT":case "MERGE":
                    return Algorithm.MERGESORT;
                case "2":case "INSERTION SORT":case "INSERTION_SORT":case "INSERTION":
                    return Algorithm.INSERTION_SORT;
                case "3":case "BUBBLE SORT":case "BUBBLE_SORT":case "BUBBLE":
                    return Algorithm.BUBBLE_SORT;
                default:
                    throw new InputMismatchException();
            }
        }
    }

    public static void main(String[] args) {
        /* This program will test four sorting algorithms
         * Quicksort
         * Mergesort
         * Insertion Sort
         * Bubble Sort
         */
        Scanner in = new Scanner(System.in);
        System.out.print("Select your algorithm. Your choices are:\n" +
                "0: Quicksort\n" +
                "1: Mergesort\n" +
                "2: Insertion Sort\n" +
                "3: Bubble Sort\n>");
        Algorithm algorithm = null;
        boolean canContinue = false;
        do {
            try {
                algorithm = Algorithm.fromString(in.nextLine());
                canContinue = true;
            }
            catch (InputMismatchException e){
                System.out.println("Invalid input. Please try again.");
            }
        } while (!canContinue);

        int[] sizes = {100, 1000, 10000, 100000, 1000000, 10000000};
        for (int size : sizes){
            System.out.println("Running " + algorithm.name() + " on " + size + " elements.");
            double time = sort(algorithm, size);
            System.out.println("Sort completed in " + time + " seconds.");
        }
        System.out.println("All sorts complete!");
    }

    public static double sort(Algorithm algorithm, int howmany){
        ArrayList<Double> array = new ArrayList<>();
        for (int i = 0; i < howmany; i++){
            array.add(Math.random());
        }
        long startTime = System.currentTimeMillis();
        switch (algorithm){
            case QUICKSORT:
                array = quickSort(array);
                break;
            case MERGESORT:
                array = mergeSort(array);
                break;
            case INSERTION_SORT:
                array = insertionSort(array);
                break;
            case BUBBLE_SORT:
                array = bubbleSort(array);
                break;
        }
        long endTime = System.currentTimeMillis();

        //Check
        double curr;
        double prev = 0;
        for (Object element : array) {
            curr = (Double)element;
            if (curr < prev) {
                System.out.println(curr + " should not be after " + prev);
            }
            prev = curr;
        }

        return (double) (endTime - startTime) / 1000;
    }

    public static ArrayList<Double> quickSort(ArrayList<Double> array){
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
        for (Double element : quickSort(less)) {
            array.set(total++, element);
        }
        for (; pivotCount > 0; pivotCount--) {
            array.set(total++, pivot);
        }
        for (double element : quickSort(more)) {
            array.set(total++, element);
        }

        return array;
    }

    public static ArrayList<Double> mergeSort(ArrayList<Double> array){
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
            ArrayList<Double> first = new ArrayList<>();
            first.addAll(array.subList(0, size / 2 ));
            first = mergeSort(first);
            ArrayList<Double> second = new ArrayList<>();
            second.addAll(array.subList(size / 2, size));
            second = mergeSort(second);

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

    public static ArrayList<Double> insertionSort(ArrayList<Double> array){
        //Ported from Wikipedia
        int size = array.size();
        int j;
        Double x;
        for (int i = 1; i < size; i++) {
            x = array.get(i);
            j = i;
            while (j > 0 && array.get(j - 1) > x){
                array.set(j, array.get(j - 1));
                j--;
            }
            array.set(j, x);
        }
        return array;
    }

    public static ArrayList<Double> bubbleSort(ArrayList<Double> array){
        //Ported from Wikipedia
        int size = array.size();
        while (size > 0) {
            int sub = 0;
            for (int i = 1; i < size; i++){
                if (array.get(i - 1) > array.get(i)) {
                    Double temp = array.get(i);
                    array.set(i, array.get(i - 1));
                    array.set(i - 1, temp);
                    sub = i;
                }
            }
            size = sub;
        }
        return array;
    }
}
