import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class SortingPanel extends JComponent {

    private Map<Integer, Color> map;
    private int[] array;
    private final Random r;
    private int speed;

    private static final Color CURRENT = Color.ORANGE;
    private static final Color SORTED = Color.GREEN;
    private static final Color MIN = Color.RED;

    public SortingPanel() {
        r = new Random(System.nanoTime());
        generateArray(MainWindow.MAX / 2);
        speed = 5;
    }

    public void paint(Graphics g) {
        int y =  getHeight() -20;
        int scalar = getHeight() / MainWindow.MAX - 1;
        int width = Math.min(getWidth() / array.length, 100);
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        for (int i = 0; i < array.length; i++) {
            g.setColor(map.get(i));
            g.fillRect(i * width, y, width - 2, -scalar * array[i]);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), (int) i * width + width / 4, y + 15);
        }
    }

    public void setArray(int[] array) {
        this.array = array;
        map = new HashMap<>(array.length);
        System.out.println(Arrays.toString(array));
        this.repaint();
    }

    public void generateArray(int size) {
        size = Math.min(Math.max(size, MainWindow.MIN), MainWindow.MAX);
        IntStream ints = r.ints(size, MainWindow.MIN, MainWindow.MAX);
        array = ints.toArray();
        map = new HashMap<>(size);
        System.out.println(Arrays.toString(array));
        this.repaint();
    }

    public void generateArray() {
        generateArray(array.length);
    }

    public void start(SortingMethod sortingMethod) {
        System.out.println("Starting: " + sortingMethod);
        if (sortingMethod == SortingMethod.SELECTION) {
            selectionSort();
        } else if (sortingMethod == SortingMethod.INSERTION) {
            insertionSort();
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void updateScreen() {
        paintImmediately(0, 0, getWidth(), getHeight());
        try {
            Thread.sleep((long) (2048 * Math.pow(2, -speed)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectionSort() {
        map.clear();
        int numSorted = 0;
        for (int n = 0; n < array.length; n++) {
            int minIndex = n;
            map.put(minIndex, MIN);
            for (int i = numSorted; i < array.length; i++) {
                if (array[i] < array[minIndex]) {
                    map.remove(minIndex);
                    map.put(i, MIN);
                    minIndex = i;
                } else {
                    if (i == numSorted) {
                        map.put(i, MIN);
                    } else {
                        map.put(i, CURRENT);
                    }
                }
                updateScreen();
                if (map.get(i) == CURRENT) map.remove(i);
            }
            int temp = array[numSorted];
            array[numSorted] = array[minIndex];
            array[minIndex] = temp;
            map.remove(minIndex);
            map.put(numSorted, SORTED);
            numSorted++;
            updateScreen();
        }
    }

    private void insertionSort() {
        map.clear();
        map.put(0, SORTED);
        for (int n = 1; n < array.length; n++) {
            for (int i = n; i > 0; i--) {
                if (array[i] < array[i - 1]) {
                    map.put(i - 1, MIN);
                    map.put(i, CURRENT);
                    updateScreen();
                    map.put(i-1, SORTED);
                    map.put(i, SORTED);
                    int temp = array[i - 1];
                    array[i - 1] = array[i];
                    array[i] = temp;
                } else {
                    map.put(i, SORTED);
                    updateScreen();
                    break;
                }
            }
            updateScreen();
        }
        repaint();
    }
}
