import java.util.*;

public class SRTFSchedular implements Scheduler {
    @Override
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        int completedProcesses = 0;
        int totalProcesses = processes.size();
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        List<Process> readyQueue = new ArrayList<>();
        String prevProcessName = null;
        int startTime = 0;

        while (completedProcesses < totalProcesses) {

            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && process.getBurstTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime));

            Process currentProcess = readyQueue.get(0);

            if (!currentProcess.getName().equals(prevProcessName)) {
                if (prevProcessName != null) {
                    System.out.println("Time " + startTime + " -> " + currentTime + " Executing: " + prevProcessName);

                }
                startTime = currentTime;
                prevProcessName = currentProcess.getName();
            }

            currentTime++;
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

            if (currentProcess.getBurstTime() == 0) {
                completedProcesses++;
                int completionTime = currentTime;
                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getInitialBurstTime(); // Use initial burst time to calculate waiting time

                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                System.out.println("Process " + currentProcess.getName() + " completed at time " + completionTime +
                        " | Turnaround Time: " + turnaroundTime + ", Waiting Time: " + waitingTime);
                readyQueue.remove(currentProcess); // Remove the completed process from the ready queue
            }
        }

        System.out.println("\nAverage Waiting Time: " + (double) totalWaitingTime / totalProcesses);
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / totalProcesses);
    }
}
