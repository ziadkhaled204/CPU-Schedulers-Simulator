import java.util.*;

public class PriorityScheduler implements Scheduler {
    public static final int contextSwitchCost = 2;  // Time taken for context switch
    public static int contextSwitchTime = 0;  // Total context switch time

    @Override
    public List<TimelineSegment> schedule(List<Process> processes) {
        // Sort processes by priority (lower priority number = higher priority)
        List<TimelineSegment> timeline = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getPriority));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        List<String> log = new ArrayList<>(); // Execution log
        List<List<Integer>> executionHistory = new ArrayList<>();  // Stores execution history

        // Initialize execution history for each process
        for (int i = 0; i < processes.size(); i++) {
            executionHistory.add(new ArrayList<>());
        }

        String prevProcessName = null;
        int startTime = 0;

        System.out.println("Priority Scheduling Execution:");
        for (Process process : processes) {
            // Calculate waiting time and turnaround time for the process
            int waitingTime = Math.max(0, currentTime - process.getArrivalTime());
            int turnaroundTime = waitingTime + process.getBurstTime();

            // If the process isn't the same as the last one, it means there's a context switch
            if (!process.getName().equals(prevProcessName)) {
                if (prevProcessName != null) {
                    log.add("Time " + startTime + " -> " + currentTime + ": Executing: " + prevProcessName);
                    timeline.add(new TimelineSegment(startTime ,currentTime,prevProcessName,process.getColor()));
                    contextSwitchTime += contextSwitchCost;  // Add context switch time
                }
                startTime = currentTime;
                prevProcessName = process.getName();
            }
            // Simulate execution
            currentTime += process.getBurstTime();
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            // Log process execution details
            log.add(String.format("Process %s executed from %d to %d | Waiting Time: %d, Turnaround Time: %d",
                    process.getName(), currentTime - process.getBurstTime(), currentTime, waitingTime, turnaroundTime));
        }

        // Log the final execution of the last process
        log.add("Time " + startTime + " -> " + currentTime + ": Executing: " + prevProcessName);
        String prevProcessColor = null;
        for (Process p : processes)
        {
            if(prevProcessName.equals(p.getName()))
            {
                prevProcessColor = p.getColor();
            }
        }
        timeline.add(new TimelineSegment(startTime ,currentTime,prevProcessName,prevProcessColor));

        // Print execution log
        log.forEach(System.out::println);

        // Calculate and print average waiting and turnaround times
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: "+ averageTurnaroundTime);

        // Print total context switch time
        System.out.println("Context Switching Time: " + contextSwitchTime);
        return timeline;
    }
}
