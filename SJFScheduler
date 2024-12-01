import java.util.Comparator;
import java.util.List;

public class SJFScheduler implements Scheduler {
    @Override
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getBurstTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nSJF Scheduling Execution:");
        for (Process process : processes) {
            int waitingTime = Math.max(0, currentTime - process.getArrivalTime());
            int turnaroundTime = waitingTime + process.getBurstTime();
            currentTime += process.getBurstTime();

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.println("Process " + process.getName() + 
                               " -> Waiting Time: " + waitingTime + 
                               ", Turnaround Time: " + turnaroundTime);
        }

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processes.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processes.size());
    }
}
