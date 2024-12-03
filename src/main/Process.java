public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String color;

    public Process(String name, int arrivalTime, int burstTime, int priority, String color) {
        setName(name);
        setArrivalTime(arrivalTime);
        setBurstTime(burstTime);
        setPriority(priority);
        setColor(color);
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", color='" + color + '\'' +
                '}';
    }
}
