package pl.krywion.usosremastered.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextProvider {
    public String getCurrentUser() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
