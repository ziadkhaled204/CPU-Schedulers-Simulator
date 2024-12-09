import java.util.*;

public class SRTFScheduler implements Scheduler {
    public static final int context_switch_cost = 2;
    public static int context_switch_time = 0;

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

        for (Process process : processes) {
            process.setRemainingBurstTime(process.getBurstTime() + 1);
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
                    String prevProcessColor = null;
                    for (Process p : processes)
                    {
                        if(prevProcessName.equals(p.getName()))
                        {
                            prevProcessColor = p.getColor();
                        }
                    }
                    timeline.add(new TimelineSegment(startTime ,currentTime,prevProcessName,prevProcessColor));
                    context_switch_time +=context_switch_cost;
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
                System.out.println("Process " + currentProcess.getName() + " completed at time " + completionTime + " --> Turnaround Time: "
                        + turnaroundTime + ", Waiting Time: " + waitingTime);
                readyQueue.remove(currentProcess);
                timeline.add(new TimelineSegment(startTime ,currentTime,currentProcess.getName() , currentProcess.getColor()));
            }
        }
        System.out.println("Time " + startTime + " -> " + currentTime + " Executing: " + prevProcessName);
        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / totalProcesses);
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / totalProcesses);
        System.out.println("the context switching time : " + context_switch_time);
        return timeline;
    }
}