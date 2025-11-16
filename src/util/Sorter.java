package util;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Sorter {

    //faster than quick-sort for n < 10
    public static void insertionSort(List<? extends Number> unsortedList) {
        IntStream.range(0, unsortedList.size() - 1).forEach( i -> {
            int j = i + 1;
            Number value = unsortedList.get(j);
            for(;j > 0 && value.doubleValue() < unsortedList.get(j-1).doubleValue(); j--) {
                Collections.swap(unsortedList, j, j-1);
            }
        });
    }

    private Sorter() {}

}
