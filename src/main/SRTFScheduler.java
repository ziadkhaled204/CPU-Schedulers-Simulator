import java.util.*;

public class SRTFScheduler implements Scheduler {

    @Override
    public void schedule(List<Process> processes) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        List<Process> readyQueue = new ArrayList<>();
        List<String> log = new ArrayList<>(); // Execution log
        String prevProcessName = null;
        int startTime = 0;

        // Initialize remaining burst times
        for (Process process : processes) {
            process.setRemainingBurstTime(process.getBurstTime());
        }

        while (completedProcesses < totalProcesses) {
            // Add processes that have arrived to the ready queue
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && process.getRemainingBurstTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            // If no process is ready, advance time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort ready queue by remaining burst time
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingBurstTime));

            // Get the process with the shortest remaining time
            Process currentProcess = readyQueue.get(0);
            int remainingTime = currentProcess.getRemainingBurstTime();

            // Log context switch
            if (!currentProcess.getName().equals(prevProcessName)) {
                if (prevProcessName != null) {
                    log.add(String.format("Time %d -> %d Executing: %s", startTime, currentTime, prevProcessName));
                }
                startTime = currentTime;
                prevProcessName = currentProcess.getName();
            }

            // Execute the process for one unit of time
            currentTime++;
            currentProcess.setRemainingBurstTime(remainingTime - 1);

            // If the process finishes execution
            if (currentProcess.getRemainingBurstTime() == 0) {
                completedProcesses++;
                int completionTime = currentTime;
                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                log.add(String.format("Process %s completed at time %d | Turnaround Time: %d, Waiting Time: %d",
                        currentProcess.getName(), completionTime, turnaroundTime, waitingTime));

                readyQueue.remove(currentProcess);
            }
        }

        // Log the final execution segment
        if (prevProcessName != null) {
            log.add(String.format("Time %d -> %d Executing: %s", startTime, currentTime, prevProcessName));
        }

        // Print the execution log
        System.out.println("\nSRTF Scheduling Execution Log:");
        log.forEach(System.out::println);

        // Print average waiting and turnaround times
        double averageWaitingTime = (double) totalWaitingTime / totalProcesses;
        double averageTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;
        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
