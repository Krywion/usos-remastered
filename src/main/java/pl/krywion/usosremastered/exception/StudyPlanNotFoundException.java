package pl.krywion.usosremastered.exception;

public class StudyPlanNotFoundException extends RuntimeException {
    public StudyPlanNotFoundException(Long id) {
        super("Study plan with id: " + id + " not found");
    }
}
