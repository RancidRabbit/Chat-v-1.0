package RR;

import RR.testing.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TaskTest {


   private static Task task;
   private int[] input;
   private int[] expected;

    public TaskTest(int[] input, int[] expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
   public static Collection data() {
       return Arrays.asList(new Object[][] {

               {new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7},new int[]{1,7}},
               {new int[]{4,4},new int[]{}},
               {new int[]{1,4,2,4,3,4,5678,2},new int[]{5678,2}}

       });
   }


   @BeforeClass
   public static void init() {
       System.out.println("Действия перед тестированием");
       task = new Task();

    }

    @Before
    public void before() {
        System.out.println("Действие перед каждым тестом");
    }

    @Test
    public void Test1() {

        final int[] ints = Task.newArr(input);
        Assert.assertArrayEquals(expected, ints);

   }

}

/* hamcrest */