package buildengine.core.scene.director;

public enum ExecutionPhase {

    DEFAULT(0), PRE(-1), POST(1);

    final int priority;

    ExecutionPhase(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
