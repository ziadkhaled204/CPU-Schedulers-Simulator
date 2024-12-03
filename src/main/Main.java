import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    List<Process> processes = new ArrayList<>();
    processes.add(new Process("P1", 0, 17, 4, "Red"));
    processes.add(new Process("P2", 3, 6, 9, "Blue"));
    processes.add(new Process("P3", 4, 10, 3, "Green"));
    processes.add(new Process("P4", 29, 4, 8, "Yellow"));
    for (Process process : processes) {
        System.out.println(process);
    }
//    System.out.print("Enter the number of processes: ");
//    int numProcesses = scanner.nextInt();
//
//    for (int i = 0; i < numProcesses; i++) {
//        System.out.println("Enter details for process " + (i + 1) + ":");
//
//        System.out.print("Name: ");
//        String name = scanner.next();
//
//        System.out.print("Arrival Time: ");
//        int arrivalTime = scanner.nextInt();
//
//        System.out.print("Burst Time: ");
//        int burstTime = scanner.nextInt();
//
//        System.out.print("Priority: ");
//        int priority = scanner.nextInt();
//
//        System.out.print("Color: ");
//        String color = scanner.next();
//
//        Process process = new Process(name, arrivalTime, burstTime, priority, color);
//        processes.add(process);
//    }


    Scheduler scheduler = new SRTFSchedular();
    scheduler.schedule(processes);

    scanner.close();
}
