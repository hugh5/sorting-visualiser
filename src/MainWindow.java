import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow implements ActionListener, ChangeListener {
    private final JFrame window;
    private MenuPanel menuPanel;
    private SortingPanel sortingPanel;
    public static final int MAX = 64;
    public static final int MIN = 2;


    public MainWindow() {
        window = new JFrame();
        window.setLayout(new BorderLayout(10, 5));
        window.setTitle("Sorting Visualiser");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(1200, 800);
        window.setLocationRelativeTo(null);

        createMenuButtons();
        createVisualiserPanel();
    }

    private void createMenuButtons() {
        menuPanel = new MenuPanel();
        menuPanel.addActionListeners(this);
        menuPanel.addChangeListeners(this);
        window.add(menuPanel.getNorthPanel(), BorderLayout.NORTH);
        window.add(menuPanel.getSouthPanel(), BorderLayout.SOUTH);
    }

    private void createVisualiserPanel() {
        sortingPanel = new SortingPanel();
        window.add(sortingPanel, BorderLayout.CENTER);
    }

    public JFrame getWindow() {
        return window;
    }

    public void show() {
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals("generate")) {
                sortingPanel.generateArray();
            } else if (e.getActionCommand().equals("start")) {
                sortingPanel.start(menuPanel.getSortingMethod());
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider) {
            if (((JSlider)e.getSource()).getName().equals("length")) {
                sortingPanel.generateArray(((JSlider) e.getSource()).getValue());
                System.out.println(((JSlider) e.getSource()).getValue() + ":" + e);
            } else if (((JSlider)e.getSource()).getName().equals("speed")) {
                sortingPanel.setSpeed(((JSlider) e.getSource()).getValue());
                System.out.println(((JSlider) e.getSource()).getValue() + ":" + e);
            }
        }
    }
}
