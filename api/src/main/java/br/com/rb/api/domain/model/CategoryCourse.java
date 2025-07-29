package br.com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum CategoryCourse {
    PROGRAMMING("Programação"),
    FRONT_END("Front-end"),
    DATA_SCIENCE("Data Science"),
    DEVOPS("DevOps"),
    UX_DESIGN("UX & Design"),
    MOBILE("Mobile");

    private final String displayName;

    CategoryCourse(String displayName) {
        this.displayName = displayName;
    }
}