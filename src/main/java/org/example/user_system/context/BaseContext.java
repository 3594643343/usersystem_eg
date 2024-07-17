package org.example.user_system.context;

import org.example.user_system.dto.CurrentUserDto;

public class BaseContext {

    public static ThreadLocal<CurrentUserDto> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(CurrentUserDto currentUserDto) {
        threadLocal.set(currentUserDto);
    }

    public static CurrentUserDto getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
