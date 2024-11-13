package pl.krywion.usosremastered.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextProvider {
    private static final String SYTEM_USER = "SYSTEM";

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return authentication.getName();
        }
        return SYTEM_USER;
    }
}
