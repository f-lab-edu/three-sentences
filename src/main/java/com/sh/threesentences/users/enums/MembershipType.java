package com.sh.threesentences.users.enums;

public enum MembershipType {

    FREE("FREE", "무료"), BASIC("BASIC", "베이직"), PREMIUM("PREMIUM", "프리미엄");

    private final String name;
    private final String nameKOR;

    MembershipType(String name, String nameKOR) {
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
