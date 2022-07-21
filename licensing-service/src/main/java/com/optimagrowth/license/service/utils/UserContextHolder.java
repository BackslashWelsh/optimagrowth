package com.optimagrowth.license.service.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

@UtilityClass
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public UserContext getContext() {
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public UserContext createEmptyContext() {
        return new UserContext();
    }
}
