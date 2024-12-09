import javax.swing.*;
import java.awt.*;
import java.util.List;

class GanttChart extends JFrame {

    private final List<TimelineSegment> timeline;

    public GanttChart(List<TimelineSegment> timeline) {
        this.timeline = timeline;
        setTitle("Gantt Chart Visualization");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GanttPanel());
    }

    class GanttPanel extends JPanel {
        private int currentSegmentIndex = 0; // Track the segment being displayed

        public GanttPanel() {
            Timer timer = new Timer(1000, e -> {
                if (currentSegmentIndex < timeline.size()) {
                    currentSegmentIndex++; // Progressively display more segments
                    repaint(); // Redraw the panel to show updated segments
                } else {
                    ((Timer) e.getSource()).stop(); // Stop the timer when all segments are displayed
                }
            });
            timer.start(); // Start the timer
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int x = 10; // Initial X position
            int y = 50; // Initial Y position

            // Draw segments up to the current index
            for (int i = 0; i < currentSegmentIndex; i++) {
                TimelineSegment segment = timeline.get(i);
                int width = (segment.getEndTime() - segment.getStartTime()) * 20; // Scale time to width
                g.setColor(Color.decode(segment.getColor()));
                g.fillRect(x, y, width, 30); // Draw filled rectangle
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, 30); // Draw rectangle border
                g.drawString(segment.getProcessName(), x + 5, y + 20); // Add process label
                x += width; // Move X position for the next segment
            }
        }
    }
}