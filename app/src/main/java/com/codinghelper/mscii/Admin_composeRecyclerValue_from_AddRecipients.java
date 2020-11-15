package com.codinghelper.mscii;

import java.io.Serializable;

public class Admin_composeRecyclerValue_from_AddRecipients implements Serializable {

    public String departmentName;
    public String sessionName;

    public Admin_composeRecyclerValue_from_AddRecipients() {

    }

    public Admin_composeRecyclerValue_from_AddRecipients(String departmentName, String sessionName) {
        this.departmentName = departmentName;
        this.sessionName = sessionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
