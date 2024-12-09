import java.util.*;

public class FCAIScheduler implements Scheduler {

    // Calculate the FCAI Factor for a process (using ceil for rounding)
    public double calculateFCAIFactor(Process process, double V1, double V2) {
        double priorityFactor = Math.ceil(10 - process.getPriority());
        double arrivalFactor = Math.ceil(process.getArrivalTime() / V1);
        double burstFactor = Math.ceil(process.getRemainingBurstTime() / V2);
        process.setFCAIFactor(priorityFactor + arrivalFactor + burstFactor);
        return process.getFCAIFactor();
    }

    @Override
    public List<TimelineSegment> schedule(List<Process> processes) {
        List<TimelineSegment> timeline = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort by arrival time

        int currentTime = 0;
        double V1, V2;
        List<Process> readyQueue = new ArrayList<>();
        List<String> log = new ArrayList<>();
        List<Integer> waitedTime = new ArrayList<>();
        int completedProcesses = 0;

        // Calculate V1 and V2
        V1 = (double) processes.getLast().getArrivalTime() / 10;
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(1);
        V2 = (double) maxBurstTime / 10;

        // Initial FCAI factor calculation
        for (Process p : processes) {
            p.setFCAIFactor(calculateFCAIFactor(p, V1, V2));
        }

        Process currentProcess = null;
        int executionTime = 0;

        while (completedProcesses < processes.size()) {
            // Add processes that have arrived to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() == currentTime && p.getRemainingBurstTime() > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            // If no process is ready, advance time
            if (currentProcess == null && readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // If no process is running, pick the next one from the ready queue
            if (currentProcess == null) {
                currentProcess = readyQueue.removeFirst();
            }

            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
            currentTime++;
            // Recalculate FCAI factors for all processes in the ready queue before execution or preemption
            for (Process p : readyQueue) {
                p.setFCAIFactor(calculateFCAIFactor(p, V1, V2));
            }

            // Check if the process has completed
            if (currentProcess.getRemainingBurstTime() == 0) {
                completedProcesses++;
                readyQueue.remove(currentProcess);
                currentProcess = null;// Release the processor
                executionTime=1;
                continue;
            }

            // Check if the process should be preempted
            boolean preempted = false;
            Process nextProcess = null;

            if (executionTime >= Math.ceil(currentProcess.getQuantum() * 0.4) ) {
                if(!readyQueue.isEmpty()) {
                    for (Process p : readyQueue) {
                        if (p.getFCAIFactor() < currentProcess.getFCAIFactor()) {
                            readyQueue.remove(p);
                            nextProcess = p;
                            preempted = true;
                            break;
                        }
                    }
                }

                if (preempted) {
                    // Update quantum of the current process
                    currentProcess.setQuantum(currentProcess.getQuantum() + (currentProcess.getQuantum() - executionTime));
                    readyQueue.add(currentProcess); // Re-add the current process to the queue
                    //log.add(currentTime -executionTime + " --> " + currentTime + " : " +currentProcess.getName());
                    timeline.add(new TimelineSegment(currentTime -executionTime ,currentTime,currentProcess.getName() , currentProcess.getColor()));
                    currentProcess = nextProcess;// Switch to the next process
                    executionTime = 0; // Reset execution time
                } else if (executionTime >= currentProcess.getQuantum()) {
                    // If quantum is fully used, update quantum and re -add to queue
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    //log.add(currentTime -executionTime + " --> " + currentTime + " : " +currentProcess.getName());
                    timeline.add(new TimelineSegment(currentTime -executionTime ,currentTime,currentProcess.getName() , currentProcess.getColor()));
                    readyQueue.add(currentProcess);
                    currentProcess = null;
                    executionTime = 0;// Release the processor
                }
            }

            executionTime++;




        }

        // Print the log
        for (TimelineSegment entry : timeline) {
            System.out.println(entry);
        }
        return timeline;
    }
}