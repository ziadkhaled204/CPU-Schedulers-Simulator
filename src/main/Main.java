import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();

        // Static process list
        processes.add(new Process("P1", 0, 17, 4, "#FF5733", 4));  // Red
        processes.add(new Process("P2", 3, 6, 9, "#33FF57", 3));   // Green
        processes.add(new Process("P3", 4, 10, 3, "#3357FF", 5));  // Blue
        processes.add(new Process("P4", 29, 4, 8, "#FFFF33", 2));  // Yellow


        // Print processes
        for (Process process : processes) {
            System.out.println(process);
        }

        // Scheduler implementation (choose one to see the chart)

//        SJFScheduler scheduler = new SJFScheduler();
//        List<TimelineSegment> timeline = scheduler.schedule(processes);

        FCAIScheduler scheduler = new FCAIScheduler();
        List<TimelineSegment> timeline = scheduler.schedule(processes);

//        SRTFScheduler scheduler = new SRTFScheduler();
//        List<TimelineSegment> timeline = scheduler.schedule(processes);

//        PriorityScheduler scheduler = new PriorityScheduler();
//        List<TimelineSegment> timeline = scheduler.schedule(processes);


        // Create and show the Gantt Chart
        SwingUtilities.invokeLater(() -> {
            GanttChart chart = new GanttChart( timeline,"FCAI Scheduling Gantt Chart");
            chart.setVisible(true);
        });

        scanner.close();
    }
}
