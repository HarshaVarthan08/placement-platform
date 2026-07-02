package com.placement.platform.job.dto;

public class JobLocationDto {
    private Long id;
    private String location;

    public JobLocationDto() {
    }

    public JobLocationDto(Long id, String location) {
        this.id = id;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
