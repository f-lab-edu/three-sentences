package com.sh.threesentences.readingspace.enums;

public enum UserRole {

    ADMIN("ADMIN", "어드민"), REDINGMATE("REDINGMATE", "리딩메이트");

    private final String name;
    private final String nameKOR;

    UserRole(String name, String nameKOR) {
        this.name = name;
        this.nameKOR = nameKOR;
    }

    public String getName() {
        return name;
    }

    public String getNameKOR() {
        return nameKOR;
    }
}
