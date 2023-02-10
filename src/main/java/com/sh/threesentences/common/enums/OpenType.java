package com.sh.threesentences.common.enums;

public enum OpenType {

    PUBLIC("PUBLIC", "공개"), PRIVATE("PRIVATE", "비공개");

    private final String name;
    private final String nameKOR;

    OpenType(String name, String nameKOR) {
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
