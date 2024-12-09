public class TimelineSegment {
    private final int startTime;
    private final int endTime;
    private final String processName;
    private final String color;

    public TimelineSegment(int startTime, int endTime, String processName, String color) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.processName = processName;
        this.color = color;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getProcessName() {
        return processName;
    }

    public String getColor() {
        return color;
    }
}
