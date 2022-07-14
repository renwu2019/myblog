package com.wyy.myblog.dto;

/**
 * created by 伍猷煜 on 2022/7/14 21:07 星期四
 */
public enum BlogCRUDEnum {

    SAVE(1, "save"),
    UPDATE(2, "update"),
    DELETE(3, "delete")
    ;

    private int type;

    private String typeName;

    BlogCRUDEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
