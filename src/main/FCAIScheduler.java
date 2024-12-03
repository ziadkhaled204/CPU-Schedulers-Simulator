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
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort by arrival time

        int currentTime = 0;
        double V1, V2;
        List<Process> readyQueue = new ArrayList<>();
        List<String> log = new ArrayList<>();
        int completedProcesses = 0;

        // Initialize data structures
        int[] waitingTimes = new int[processes.size()];
        int[] turnaroundTimes = new int[processes.size()];
        List<List<Integer>> quantumHistory = new ArrayList<>();

        // Initialize quantum history
        for (Process p : processes) {
            List<Integer> initialHistory = new ArrayList<>();
            initialHistory.add(p.getQuantum());
            quantumHistory.add(initialHistory);
        }

        // Calculate V1 and V2
        V1 = (double) processes.get(processes.size() - 1).getArrivalTime() / 10;
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(1);
        V2 = (double) maxBurstTime / 10;

        while (completedProcesses < processes.size()) {
            // Add processes that have arrived to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingBurstTime() > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            // If no process is ready, advance time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Recalculate FCAI factors and sort queue
            for (Process p : readyQueue) {
                p.setFCAIFactor(calculateFCAIFactor(p, V1, V2));
            }
            readyQueue.sort(Comparator.comparingDouble(Process::getFCAIFactor));

            // Get the process with the lowest FCAI factor
            Process currentProcess = readyQueue.remove(0);
            int processIndex = processes.indexOf(currentProcess);

            // Calculate execution time (non-preemptively for 40% of its quantum)
            int executionTime = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
            executionTime = Math.min(executionTime, currentProcess.getRemainingBurstTime());

            int startTime = currentTime;
            currentTime += executionTime;
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executionTime);

            // Update waiting times for other processes in the ready queue
            for (Process p : readyQueue) {
                int index = processes.indexOf(p);
                waitingTimes[index] += executionTime;
            }

            boolean preempted = false;

            // Check if another process should preempt the current process
            if (!readyQueue.isEmpty()) {
                Process bestProcess = readyQueue.get(0);
                if (bestProcess.getFCAIFactor() < currentProcess.getFCAIFactor()) {
                    preempted = true;
                }
            }

            int oldQuantum = currentProcess.getQuantum();
            if (preempted) {
                // Update quantum with unused portion and re-add to queue
                int unusedQuantum = oldQuantum - executionTime;
                currentProcess.setQuantum(oldQuantum + unusedQuantum);
                quantumHistory.get(processIndex).add(currentProcess.getQuantum());
                readyQueue.add(currentProcess);
            } else {
                // If not preempted and unfinished, update quantum
                if (currentProcess.getRemainingBurstTime() > 0) {
                    currentProcess.setQuantum(oldQuantum + 2);
                    quantumHistory.get(processIndex).add(currentProcess.getQuantum());
                    readyQueue.add(currentProcess);
                } else {
                    completedProcesses++;
                    turnaroundTimes[processIndex] = currentTime - currentProcess.getArrivalTime();
                }
            }

            // Log execution details
            log.add(String.format("Time %d-%d | Process: %s | Remaining Burst: %d | Quantum: %d -> %d",
                    startTime, currentTime, currentProcess.getName(), currentProcess.getRemainingBurstTime(),
                    oldQuantum, currentProcess.getQuantum()));
        }

        // Print execution log
        System.out.println("Execution Log:");
        log.forEach(System.out::println);

        // Calculate averages
        double totalWaitingTime = Arrays.stream(waitingTimes).sum();
        double totalTurnaroundTime = Arrays.stream(turnaroundTimes).sum();
        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // Print results
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);

        // Print quantum history
        System.out.println("Quantum History:");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println(processes.get(i).getName() + ": " + quantumHistory.get(i));
        }
    }
}
