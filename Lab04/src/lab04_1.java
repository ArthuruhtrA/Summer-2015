import java.util.*;

/**
 * Created by Ari Sanders on 10-Jul-15.
 */
public class lab04_1 {
    public static void main(String[] args) {
        //a
        System.out.println("Creating new BetterArray of 10.");
        BetterArray array = new BetterArray(10);
        System.out.println("Done.\n");
        //b
        System.out.println("Filling with 200-209:");
        for (int i = 200; i < 210; i++) {
            array.set(i - 200, i);
            System.out.println("Added " + i + " to the BetterArray.");
        }
        System.out.println("Done.\n");
        //c
        outputList(array, 19);
        //d
        System.out.println("Filling with 300-319:");
        for (int i = 300; i < 320; i++) {
            array.put(i, i - 300);
            System.out.println("Added " + i + " to the BetterArray.");
        }
        System.out.println("Done.\n");
        //e
        outputList(array, 19);
        //f
        System.out.println("Adding 999 before element 14.");
        array.add(14, 999);
        System.out.println("Done.\n");
        //g
        outputList(array, 20);
        //h
        System.out.println("Deleting element 15.");
        array.remove(15);
        System.out.println("Done.\n");
        //i
        outputList(array, 19);
        //j
        System.out.println("Creating new BetterArray of 1000.");
        BetterArray newArray = new BetterArray(1000);
        System.out.println("Done.\n");
        //k
        System.out.println("Filling the new array with random numbers.");
        for (int i = 0; i < newArray.size(); i++) {
            newArray.set(i, (int) (Math.random() * 1001));
        }
        System.out.println("Done.\n");
        //l
        System.out.println("Sorting new array in ascending order.");
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1.equals(o2)) {
                    return 0;
                }
                if (o2.equals(null)) {
                    return -1;
                } else if (o1.equals(null)) {
                    return 1;
                }
                int o1i, o2i;
                if (Number.class.isAssignableFrom(o1.getClass()) && Number.class.isAssignableFrom(o2.getClass())) {
                    o1i = Integer.parseInt(o1.toString());
                    o2i = Integer.parseInt(o2.toString());
                } else {
                    o1i = Integer.valueOf(o1.toString());
                    o2i = Integer.valueOf(o1.toString());
                }

                if (o1i < o2i) {
                    return -1;
                }
                if (o1i > o2i) {
                    return 1;
                }
                return 0;
            }
        };
        try {
            newArray.sort(comparator);
        } catch (NoSuchElementException e) {
            System.out.println("Somehow we got somewhere we shouldn't have!\nHere is the current array:");
            for (Object element : newArray) {
                System.out.print(element + "\t");
            }
        }
        System.out.println("Done.\n");
        //m
        System.out.println("Checking sorted array:");
        int curr;
        int prev = 0;
        for (Object element : newArray) {
            if (element != null) {
                curr = (int) element;
                if (curr < prev) {
                    System.out.println(curr + " should not be after " + prev);
                }
                prev = curr;
            } else {
                //Not sure what to test here
            }
        }
        System.out.println("Done.");
    }

    public static void outputList(List array, int max) {
        System.out.println("Array 0-" + max + " on array of size " + array.size() + ":");
        for (int i = 0; i <= max; i++) {
            try {
                System.out.println(array.get(i));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Sorry, the index " + i + " does not exist.");
            }
        }
        System.out.println("\n");
    }
}