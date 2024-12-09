import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFScheduler implements Scheduler {

    @Override
    public List<TimelineSegment> schedule(List<Process> processes) {
        // Sort processes by arrival time initially
        List<TimelineSegment> timeline = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        List<String> log = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();

        System.out.println("\nNon-Preemptive SJF Scheduling Execution with Per-Unit-Time Check:");

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add newly arrived processes to the ready queue
            for (Process process : new ArrayList<>(processes)) {
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processes.remove(process);
                    log.add("Time " + currentTime + ": Process " + process.getName() + " added to the ready queue.");
                }
            }

            // If no processes are ready, advance time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Sort ready queue by burst time (shortest job first)
            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime).thenComparingInt(Process::getArrivalTime));

            // Select the process with the shortest burst time
            Process currentProcess = readyQueue.get(0);
            log.add("Time " + currentTime + ": Executing process " + currentProcess.getName());

            // Execute process for one time unit and check for better candidates
            int executedTime = 0;
            while (executedTime < currentProcess.getBurstTime()) {
                currentTime++;
                executedTime++;

                // Add newly arrived processes during execution
                for (Process process : new ArrayList<>(processes)) {
                    if (process.getArrivalTime() <= currentTime) {
                        readyQueue.add(process);
                        processes.remove(process);
                        log.add("Time " + currentTime + ": Process " + process.getName() + " added to the ready queue.");
                    }
                }

                // Check if a better process exists (shorter burst time)
                readyQueue.sort(Comparator.comparingInt(Process::getBurstTime).thenComparingInt(Process::getArrivalTime));
                if (readyQueue.get(0) != currentProcess) {
                    log.add("Time " + currentTime + ": Preempted process " + currentProcess.getName() +
                            " for process " + readyQueue.get(0).getName());
                    break;
                }
            }

            // If the current process is fully executed, remove it from the queue
            if (executedTime >= currentProcess.getBurstTime()) {
                readyQueue.remove(currentProcess);
                int completionTime = currentTime;
                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                log.add("Time " + currentTime + ": Process " + currentProcess.getName() + " completed " +
                        "| Waiting Time: " + waitingTime + ", Turnaround Time: " + turnaroundTime);
            }
        }

        // Calculate averages
        double averageWaitingTime = (double) totalWaitingTime / (log.size() / 2); // Divide log entries appropriately
        double averageTurnaroundTime = (double) totalTurnaroundTime / (log.size() / 2);

        // Print execution log
        log.forEach(System.out::println);

        // Print averages
        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        return timeline;
    }
}
