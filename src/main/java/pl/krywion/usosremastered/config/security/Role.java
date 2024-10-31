package pl.krywion.usosremastered.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permissions.ADMIN_READ,
                    Permissions.ADMIN_WRITE,
                    Permissions.ADMIN_UPDATE,
                    Permissions.ADMIN_DELETE,
                    Permissions.STUDENT_READ,
                    Permissions.STUDENT_WRITE,
                    Permissions.STUDY_PLAN_READ,
                    Permissions.STUDY_PLAN_WRITE
            )
    ),
    STUDENT(
            Collections.emptySet()
    ),
    EMPLOYEE(Collections.emptySet());

    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new java.util.ArrayList<>(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
