package com.example.myapplication.bean;


public class BeanBrand {
    private int primaryId;
    private int id;
    private String name;
    private String description;
    private String created_at;
    private boolean status;

    public BeanBrand() {
    }

    public BeanBrand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        created_at = createdAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BeanBrand{" +
                "primaryId=" + primaryId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", status=" + status +
                '}';
    }
}
