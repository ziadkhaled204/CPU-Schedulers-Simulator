import java.util.*;

public class SRTFScheduler implements Scheduler {
    public static final int contextSwitchCost = 2;
    public static int contextSwitchTime = 0;
    private static final int agingTime = 6; // Time after which aging occurs
    private static final int agingIncrement = 1; // The amount to reduce the burst time

    @Override
    public List<TimelineSegment> schedule(List<Process> processes) {
        List<TimelineSegment> timeline = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        List<Process> readyQueue = new ArrayList<>();
        String prevProcessName = null;
        int startTime = 0;

        List<String> log = new ArrayList<>(); // Log for tracking process execution


        while (completedProcesses < totalProcesses) {

            // Add newly arrived processes to the ready queue
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && process.getRemainingBurstTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                    //log.add("Time " + currentTime + ": Process " + process.getName() + " added to the ready queue.");
                }
            }

            // If no processes are ready, just advance time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            for (Process process : readyQueue) {
                if (currentTime - process.getArrivalTime() >= agingTime) {
                    process.setRemainingBurstTime(Math.max(1, process.getRemainingBurstTime() - agingIncrement));
                    //log.add("Time " + currentTime + ": Aging applied to " + process.getName() + ", new remaining burst time: " + process.getRemainingBurstTime());
                }
            }

            // Sort the ready queue by remaining burst time (SRTF)
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingBurstTime));

            Process currentProcess = readyQueue.get(0);
            int remainingTime = currentProcess.getRemainingBurstTime();

            // Log context switch if there is a change in the process being executed
            if (!currentProcess.getName().equals(prevProcessName)) {
                if (prevProcessName != null) {
                    log.add("Time " + startTime + " -> " + currentTime + ": Executing: " + prevProcessName);
                    contextSwitchTime += contextSwitchCost;
                }

                startTime = currentTime;
                prevProcessName = currentProcess.getName();
            }

            // Execute the process for one time unit
            currentTime++;
            currentProcess.setRemainingBurstTime(remainingTime - 1);

            // Log process execution
            //log.add("Time " + currentTime + ": Executing " + currentProcess.getName() + " for 1 unit. Remaining burst time: " + currentProcess.getRemainingBurstTime());

            // If the process completes, remove it from the ready queue
            if (currentProcess.getRemainingBurstTime() == 0) {
                completedProcesses++;
                int completionTime = currentTime;
                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                // Log process completion details
                log.add("Time " + currentTime + ": Process " + currentProcess.getName() + " completed at time " + completionTime +
                        " | Turnaround Time: " + turnaroundTime + ", Waiting Time: " + waitingTime);
                readyQueue.remove(currentProcess);
            }
        }

        // Log the final execution of the last process
        log.add("Time " + startTime + " -> " + currentTime + ": Executing: " + prevProcessName);

        // Calculate and log averages
        double averageWaitingTime = (double) totalWaitingTime / totalProcesses;
        double averageTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;

        // Print execution log
        log.forEach(System.out::println);

        // Print averages
        System.out.println("\nAverage Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Context Switching Time: " + contextSwitchTime);
        return timeline;
    }
}
