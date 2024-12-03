import java.util.*;

public class PriorityScheduler implements Scheduler {

    @Override
    public void schedule(List<Process> processes) {
        // Sort processes by priority (lower priority number = higher priority)
        processes.sort(Comparator.comparingInt(Process::getPriority));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        List<String> log = new ArrayList<>(); // Execution log
        List<List<Integer>> executionHistory = new ArrayList<>();

        // Initialize execution history for each process
        for (int i = 0; i < processes.size(); i++) {
            executionHistory.add(new ArrayList<>());
        }

        System.out.println("\nPriority Scheduling Execution:");
        for (Process process : processes) {
            // Calculate waiting time and turnaround time for the process
            int waitingTime = Math.max(0, currentTime - process.getArrivalTime());
            int turnaroundTime = waitingTime + process.getBurstTime();

            // Log execution history
            int processIndex = processes.indexOf(process);
            executionHistory.get(processIndex).add(currentTime);

            // Simulate execution
            currentTime += process.getBurstTime();
            executionHistory.get(processIndex).add(currentTime);

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            // Log process details
            log.add(String.format("Process %s executed from %d to %d | Waiting Time: %d, Turnaround Time: %d",
                    process.getName(), currentTime - process.getBurstTime(), currentTime, waitingTime, turnaroundTime));
        }

        // Print execution log
        log.forEach(System.out::println);

        // Print execution history in the requested format
        System.out.println("\nExecution History:");
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            System.out.print(process.getName() + ": " + executionHistory.get(i));
            System.out.println();
        }

        // Print average waiting and turnaround times
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
