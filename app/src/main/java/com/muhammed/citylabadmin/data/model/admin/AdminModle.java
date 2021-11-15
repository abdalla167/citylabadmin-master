package com.muhammed.citylabadmin.data.model.admin;

public class AdminModle {
    String name;
    String password;
    String id;
    boolean admin_or_no;

    public AdminModle(String name, String password, String id, boolean admin_or_no) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.admin_or_no = admin_or_no;
    }

    public AdminModle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAdmin_or_no() {
        return admin_or_no;
    }

    public void setAdmin_or_no(boolean admin_or_no) {
        this.admin_or_no = admin_or_no;
    }
}
