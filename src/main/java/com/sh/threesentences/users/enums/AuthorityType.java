package com.sh.threesentences.users.enums;

public enum AuthorityType {

    BASIC("BASIC", "일반 사용자"), ADMIN("ADMIN", "관리자");

    private final String name;
    private final String nameKor;

    AuthorityType(String name, String nameKor) {
        this.name = name;
        this.nameKor = nameKor;
    }

    public String getName() {
        return name;
    }

    public String getNameKor() {
        return nameKor;
    }
}
