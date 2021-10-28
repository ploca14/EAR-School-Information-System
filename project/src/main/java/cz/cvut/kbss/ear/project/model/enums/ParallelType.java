package cz.cvut.kbss.ear.project.model.enums;

public enum ParallelType {
    LECTURE("PARALLEL_TYPE_LECTURE"),
    EXCERCISE("PARALLEL_TYPE_EXCERSISE");

    private final String name;

    ParallelType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
