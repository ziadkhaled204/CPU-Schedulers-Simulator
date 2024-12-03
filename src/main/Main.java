import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();

        // Static process list
        processes.add(new Process("P1", 0, 17, 4, "Red", 4));
        processes.add(new Process("P2", 3, 6, 9, "Blue", 3));
        processes.add(new Process("P3", 4, 10, 3, "Green", 5));
        processes.add(new Process("P4", 29, 4, 8, "Yellow", 2));

        // Uncomment if you want user input
        // System.out.print("Enter the number of processes: ");
        // int numProcesses = scanner.nextInt();
        //
        // for (int i = 0; i < numProcesses; i++) {
        //     System.out.println("Enter details for process " + (i + 1) + ":");
        //     System.out.print("Name: ");
        //     String name = scanner.next();
        //     System.out.print("Arrival Time: ");
        //     int arrivalTime = scanner.nextInt();
        //     System.out.print("Burst Time: ");
        //     int burstTime = scanner.nextInt();
        //     System.out.print("Priority: ");
        //     int priority = scanner.nextInt();
        //     System.out.print("Color: ");
        //     String color = scanner.next();
        //     Process process = new Process(name, arrivalTime, burstTime, priority, color, 0);
        //     processes.add(process);
        // }

        // Print processes
        for (Process process : processes) {
            System.out.println(process);
        }

        // Scheduler implementation
        Scheduler scheduler = new FCAIScheduler();
        scheduler.schedule(processes);

        scanner.close();
    }
}
