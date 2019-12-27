import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

class SkipListTests {
    boolean addTest(int t) {
        SkipList<Integer> set = new SkipList<>();
        set.add(t);
        return set.contains(t);
    }


    boolean addSize(Double t1, Double t2) {
        SkipList<Double> set1 = new SkipList<>();
        SkipList<Double> set2 = new SkipList<>();
        set1.add(t1);
        set1.add(t2);
        set2.add(t1);
        return set1.size() == set2.size();
    }


    static boolean removeTest(int num1, int num2) {
        SkipList<Integer> set = new SkipList<>();
        set.add(num1);
        set.remove(num2);

        return !set.contains(num1);
    }


    static void exceptRemove() {
        SkipList<String> set = new SkipList<>();
        set.add("qwerty");
        set.remove("qwerty");
        set.remove("qwerty");
    }


    static boolean removeSize(Double num1, Double num2) {
        SkipList<Double> set = new SkipList<>();
        set.add(num1);
        set.remove(num2);
        return set.size() == 0;
    }


    boolean compareTest(int first, int second) {
        SkipList<Integer> set = new SkipList<>();
        set.add(first);
        set.add(second);

        return set.comparator().compare(first, second) == 0;
    }


    Double firstTest(double first, double second, double third) {
        SkipList<Double> set = new SkipList<>();

        set.add(first);
        set.add(second);
        set.add(third);

        return set.first();
    }


    Double lastTest(double first, double second, double third) {
        SkipList<Double> set = new SkipList<>();

        set.add(first);
        set.add(second);
        set.add(third);

        return set.last();
    }


    boolean hasNextTest(int x, int y) {
        SkipList<Integer> set = new SkipList<>();

        set.add(x);
        set.add(y);

        return set.iterator().hasNext();
    }


    Double nextTest(double x, double y) {
        SkipList<Double> set = new SkipList<>();

        set.add(x);
        set.add(y);

        return set.iterator().next();
    }


    int sizeTest(int x, int y, int z) {
        SkipList<Integer> set = new SkipList<>();

        set.add(x);
        set.add(y);
        set.add(z);
        set.remove(z);

        return set.size;
    }

    @Test
    void test() {
        assertTrue(addTest(78));
        assertTrue(addTest(0));
        assertTrue(addSize(65.0, 65.0));
        assertFalse(addSize(42.47, 98.86));

        assertTrue(removeTest(76, 76));
        assertTrue(removeTest(43, 43));
        assertFalse(removeTest(23, 5));
        assertFalse(removeSize(34.37, 4.91));
        assertTrue(removeSize(98.9, 98.9));
        assertThrows(NullPointerException.class, SkipListTests::exceptRemove);

        assertTrue(compareTest(15, 15));
        assertFalse(compareTest(522, 0));

        assertEquals(9.48, firstTest(14.0, 18.43, 9.48));

        assertEquals(44.44, lastTest(5.19, 44.44, 44.43));

        assertTrue(hasNextTest(15, 543));
        assertFalse(hasNextTest(47, 47));

        assertEquals(900, nextTest(432, 900));

        assertEquals(2, sizeTest(0, 29, 53));
        assertEquals(1, sizeTest(43, 73, 43));
        assertEquals(0, sizeTest(90, 90, 90));
    }
}
