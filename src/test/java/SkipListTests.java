import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkipListTests {
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

    @Test
    void add() {
        assertTrue(addTest(78));
        assertTrue(addTest(0));

        assertTrue(addSize(65.0, 65.0));
        assertFalse(addSize(42.47, 98.86));
    }

    static boolean removeTest(int num1, int num2) {
        SkipList<Integer> set = new SkipList<>();
        set.add(num1);
        set.remove(num2);

        return !set.contains(num1);
    }

    static void exceptRemove(String str) {
        SkipList<String> set = new SkipList<>();
        set.remove(str);
    }

    static boolean removeSize(Double num1, Double num2) {
        SkipList<Double> set = new SkipList<>();
        set.add(num1);
        set.remove(num2);
        return set.size() == 0;
    }

    @Test
    void remove() {
        assertTrue(removeTest(76, 76));
        assertTrue(removeTest(43, 43));
        assertFalse(removeTest(23, 5));
        assertFalse(removeSize(34.37, 4.91));
        assertTrue(removeSize(98.9, 98.9));
        assertThrows(NullPointerException.class, () -> exceptRemove("qwerty"));
    }
}
