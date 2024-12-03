import java.util.*;

public class SRTFScheduler implements Scheduler {

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

        for (Process process : processes) {
            process.setRemainingBurstTime(process.getBurstTime());
        }

        while (completedProcesses < totalProcesses) {

            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && process.getRemainingBurstTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(Process::getRemainingBurstTime));

            Process currentProcess = readyQueue.get(0);
            int remainingTime = currentProcess.getRemainingBurstTime();


            if (!currentProcess.getName().equals(prevProcessName)) {
                if (prevProcessName != null) {
                    System.out.println("Time " + startTime + " -> " + currentTime + " Executing: " + prevProcessName);
                }

                startTime = currentTime;
                prevProcessName = currentProcess.getName();
            }

            currentTime++;
            currentProcess.setRemainingBurstTime(remainingTime - 1);

            if (currentProcess.getRemainingBurstTime() == 0) {
                completedProcesses++;
                int completionTime = currentTime;
                int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                totalTurnaroundTime += turnaroundTime;
                totalWaitingTime += waitingTime;

                System.out.println("Process " + currentProcess.getName() + " completed at time " + completionTime + " | Turnaround Time: " + turnaroundTime + ", Waiting Time: " + waitingTime);

                readyQueue.remove(currentProcess);
            }

        }
        System.out.println("Time " + startTime + " -> " + currentTime + " Executing: " + prevProcessName);
        System.out.println("\nAverage Waiting Time: " + (double) totalWaitingTime / totalProcesses);
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / totalProcesses);
    }
}
