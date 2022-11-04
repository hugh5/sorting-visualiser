import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class SortingPanel extends JComponent {

    private Map<Integer, Color> map;
    private int[] array;
    private int max;
    private final Random r;
    private int speed;

    private static final Color CURRENT = Color.ORANGE;
    private static final Color SORTED = Color.GREEN;
    private static final Color MIN = Color.RED;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public SortingPanel() {
        r = new Random(System.nanoTime());
        generateArray(MainWindow.MAX / 2);
        speed = 5;
    }

    public void paint(Graphics g) {
        int y =  getHeight() - 20;
        int scalar = getHeight() / max;
        int width = Math.min(getWidth() / array.length, 100);
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        for (int i = 0; i < array.length; i++) {
            g.setColor(map.get(i));
            g.fillRect(i * width, y, width - 1, -scalar * array[i]);
            g.setColor(Color.BLACK);
//            g.drawString(String.valueOf(array[i]), i * width + width / 4, y + 15);
        }
    }

    public void setArray(int[] array) {
        this.array = array;
        map = new HashMap<>(array.length);
        max = getMax(array);
        logger.log(Level.INFO, Arrays.toString(array));
        this.repaint();
    }

    public void generateArray(int size) {
        size = Math.min(Math.max(size, MainWindow.MIN), MainWindow.MAX);
        List<Integer> list = new LinkedList<>();
        IntStream.range(MainWindow.MIN, size + 1).forEach(list::add);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.remove(r.nextInt(list.size()));
        }
        map = new HashMap<>(array.length);
        max = size;
        logger.log(Level.INFO, Arrays.toString(array));
        this.repaint();
    }

    public void generateArray() {
        generateArray(array.length);
    }

    private int getMax(int[] arr) {
        int max = 0;
        for (int j : arr) {
            if (j > max) max = j;
        }
        return max;
    }

    public void start(SortingMethod sortingMethod) {
        logger.log(Level.INFO, String.valueOf(sortingMethod));
        map.clear();
        if (sortingMethod == SortingMethod.SELECTION) {
            selectionSort();
        } else if (sortingMethod == SortingMethod.INSERTION) {
            insertionSort();
        } else if (sortingMethod == SortingMethod.MERGE) {
            mergeSort(Arrays.copyOf(array, array.length), 0, array.length - 1);
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

    private int[] mergeSort(int[] arr, int lower, int upper) {
        int diff = upper - lower;
        logger.log(Level.INFO, String.format("Lower:%d, Upper:%d, Diff:%d", lower, upper, diff));
        Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        if (diff == 0) {
            map.put(lower, c);
            updateScreen();
            return new int[]{arr[lower]};
        }  else if (diff == 1) {
            map.put(lower, c);
            map.put(upper, c);
            updateScreen();
            if (arr[lower] < arr[upper]) {
                return new int[]{arr[lower], arr[upper]};
            } else {
                array[lower] = arr[upper];
                array[upper] = arr[lower];
                updateScreen();
                return new int[]{arr[upper], arr[lower]};
            }
        } else {
            int[] a = mergeSort(arr, lower, upper - (diff / 2) - 1);
            int[] b = mergeSort(arr, upper - (diff / 2), upper);
            return mergeArray(a, b, lower, c);
        }
    }

    private int[] mergeArray(int[] a, int[] b, int lower, Color color) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length)
        {
            Color part1 = map.put(lower + i, MIN);
            Color part2 = map.put(lower + a.length + j, MIN);
            updateScreen();
            map.put(lower + i, part1);
            map.put(lower + a.length + j, part2);
            if (a[i] < b[j]) {
                c[k++] = a[i++];
            } else {
                c[k++] = b[j++];
            }
        }
        while (i < a.length) {
            Color part1 = map.put(lower + i, MIN);
            updateScreen();
            map.put(lower + i, part1);
            c[k++] = a[i++];
        }
        while (j < b.length) {
            Color part2 = map.put(lower + a.length + j, MIN);
            updateScreen();
            map.put(lower + a.length + j, part2);
            c[k++] = b[j++];
        }
        for (int n = 0; n < c.length; n++) {
            array[lower + n] = c[n];
            map.put(lower + n, color);
            updateScreen();
        }
        return c;
    }
}
