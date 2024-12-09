import java.util.List;

public interface Scheduler {
    List<TimelineSegment> schedule(List<Process> processes);
}
