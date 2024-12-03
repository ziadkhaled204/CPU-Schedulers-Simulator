import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFScheduler implements Scheduler {

    @Override
    public void schedule(List<Process> processes) {
        // Sort processes by burst time, breaking ties by arrival time
        processes.sort(Comparator.comparingInt(Process::getBurstTime).thenComparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        List<String> log = new ArrayList<>(); // Log for execution details

        System.out.println("\nSJF Scheduling Execution:");
        for (Process process : processes) {
            // Waiting time: Time from arrival to execution start
            int waitingTime = Math.max(0, currentTime - process.getArrivalTime());
            // Turnaround time: Total time from arrival to completion
            int turnaroundTime = waitingTime + process.getBurstTime();

            // Update current time to reflect execution of this process
            currentTime += process.getBurstTime();

            // Add waiting and turnaround times to totals
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            // Log process execution details
            log.add(String.format("Process %s -> Waiting Time: %d, Turnaround Time: %d",
                    process.getName(), waitingTime, turnaroundTime));
        }

        // Calculate and print averages
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        // Print execution log
        log.forEach(System.out::println);

        // Print averages
        System.out.printf("Average Waiting Time: %.2f%n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f%n", averageTurnaroundTime);
    }
}
