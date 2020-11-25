package com.codinghelper.mscii;

public class admin_composeTo_getter_setter {
    private String reciversDepartmentName;
    private String reciversSessionName;

    public admin_composeTo_getter_setter(String reciversDepartmentName, String reciversSessionName) {
        this.reciversDepartmentName = reciversDepartmentName;
        this.reciversSessionName = reciversSessionName;
    }

    public String getReciversDepartmentName() {
        return reciversDepartmentName;
    }

    public void setReciversDepartmentName(String reciversDepartmentName) {
        this.reciversDepartmentName = reciversDepartmentName;
    }

    public String getReciversSessionName() {
        return reciversSessionName;
    }

    public void setReciversSessionName(String reciversSessionName) {
        this.reciversSessionName = reciversSessionName;
    }
}
