package RR.testing;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Task {

    public static void main(String[] args) {

           int[] test1 = new int[] {1, 2, 4, 4, 2, 3, 4, 1, 7};
           int[] test2 = new int[] {4,4};
           int[] test3 = new int[] {1,2,3,5};
           int[] test4 = new int[] {};


        System.out.println(Arrays.toString(newArr(test1)));

    }

    public static int[] newArr(int[] arr) {


        int dest = 0;

      if  (Arrays.stream(arr).anyMatch(a -> a == 4)) {

          final int i1 = IntStream.range(0, arr.length).filter(i -> arr[i] == 4).max().orElse(0);
          dest = arr.length - (i1 + 1);
          int[] result = new int[dest];
          System.arraycopy(arr,(i1 + 1),result,0,dest);


          return result;
      }
        else throw new RuntimeException();

    }



}
