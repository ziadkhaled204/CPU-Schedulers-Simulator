import java.util.*;

public class SJFScheduler implements Scheduler {

    @Override
    public List<TimelineSegment> schedule(List<Process> processes) {
        // Create a copy of the processes list to work with (no modification of the original list)
        List<TimelineSegment> timeline = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();
        List<String> log = new ArrayList<>();

        // Maps to track waiting and turnaround times for each process
        Map<Process, Integer> waitingTimeMap = new HashMap<>();
        Map<Process, Integer> turnaroundTimeMap = new HashMap<>();
        Map<Process, Integer> completionTimeMap = new HashMap<>();

        // Track execution order
        List<String> executionOrder = new ArrayList<>();

        int currentTime = 0;
        int completedProcess = 0;  // Count the number of completed processes

        // Initialize readyQueue with processes that have already arrived at the start
        for (Process p : processes) {
            if (p.getArrivalTime() <= currentTime) {
                readyQueue.add(p);
                waitingTimeMap.put(p, 0); // Initially set waiting time to 0
            }
        }

        // Iterate until all processes are completed
        while (completedProcess < processes.size()) {
            // Add newly arrived processes to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() == currentTime && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                    waitingTimeMap.put(p, 0); // Start waiting time for new processes
                }
            }

            // Sort ready queue by burst time (shortest job first)
            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime).thenComparingInt(Process::getArrivalTime));

            // If no process is ready increment time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Select the process with the shortest burst time
            Process currentProcess = readyQueue.get(0);

            // Track execution order
            executionOrder.add(currentProcess.getName());

            // Execute the process for one time unit
            int executedTime = 0;
            while (executedTime < currentProcess.getBurstTime()) {
                currentTime++;
                executedTime++;

                // Add arrived processes during execution to the ready queue
                for (Process process : processes) {
                    if (process.getArrivalTime() == currentTime && !readyQueue.contains(process)) {
                        readyQueue.add(process);
                        waitingTimeMap.put(process, 0); // Start waiting time for new processes
                    }
                }
            }

            // Process has finished execution, remove it from the queue
            completedProcess++;
            timeline.add(new TimelineSegment(currentTime - executedTime, currentTime, currentProcess.getName(), currentProcess.getColor()));
            readyQueue.remove(currentProcess);

            // Calculate completion time, turnaround time, and waiting time
            int completionTime = currentTime;
            int turnaroundTime = completionTime - currentProcess.getArrivalTime();
            int waitingTime = turnaroundTime - currentProcess.getBurstTime();

            // Store the completion time, waiting time, and turnaround time
            completionTimeMap.put(currentProcess, completionTime);
            turnaroundTimeMap.put(currentProcess, turnaroundTime);
            waitingTimeMap.put(currentProcess, waitingTime);

            log.add("Time " + currentTime + ": Process " + currentProcess.getName() + " completed | Waiting Time: " + waitingTime + ", Turnaround Time: " + turnaroundTime);
        }

        // Calculate averages
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        for (Process p : turnaroundTimeMap.keySet()) {
            totalWaitingTime += waitingTimeMap.get(p);
            totalTurnaroundTime += turnaroundTimeMap.get(p);
        }

        double averageWaitingTime = totalWaitingTime / waitingTimeMap.size();
        double averageTurnaroundTime = totalTurnaroundTime / turnaroundTimeMap.size();

        // Print execution log with detailed information
        log.add("Execution Order: " + String.join(" -> ", executionOrder));
        log.add("Average Waiting Time: " + averageWaitingTime);
        log.add("Average Turnaround Time: " + averageTurnaroundTime);

        // Add logs to the final output (you can replace this with any method to print or return logs)
        log.forEach(System.out::println);

        // Return the timeline (for visualization or further processing)
        return timeline;
    }
}
