import javax.swing.*;
import java.awt.*;
import java.util.List;

class GanttChart extends JFrame {

    private final List<TimelineSegment> timeline;
    private final String chartName; // Title for the chart

    public GanttChart(List<TimelineSegment> timeline, String chartName) {
        this.timeline = timeline;
        this.chartName = chartName; // Set the chart name
        setTitle("Gantt Chart Visualization");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GanttPanel());
    }

    class GanttPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the chart title
            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics metrics = g.getFontMetrics();
            int titleWidth = metrics.stringWidth(chartName);
            int titleX = (getWidth() - titleWidth) / 2; // Center title horizontally
            int titleY = 30; // Position title slightly below the top
            g.drawString(chartName, titleX, titleY);

            // Calculate total width of the chart
            int totalWidth = 0;
            for (TimelineSegment segment : timeline) {
                totalWidth += (segment.getEndTime() - segment.getStartTime()) * 20; // Scale time to width
            }

            // Get panel dimensions
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Calculate starting positions to center the chart
            int x = (panelWidth - totalWidth) / 2;
            int y = (panelHeight - 30) / 2 + 20; // Center vertically, adjust for title height

            for (TimelineSegment segment : timeline) {
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
