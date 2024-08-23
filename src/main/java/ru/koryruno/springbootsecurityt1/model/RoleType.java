package ru.koryruno.springbootsecurityt1.model;

import java.util.Optional;

public enum RoleType {
    USER, ADMIN;

    public static Optional<RoleType> from(String roleType) {
        for (RoleType type : values()) {
            if (type.name().equalsIgnoreCase(roleType))  {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
