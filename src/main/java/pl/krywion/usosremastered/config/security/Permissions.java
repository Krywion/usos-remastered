package pl.krywion.usosremastered.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),

    STUDY_PLAN_READ("study_plan:read"),
    STUDY_PLAN_WRITE("study_plan:write");

    private final String permission;


}
