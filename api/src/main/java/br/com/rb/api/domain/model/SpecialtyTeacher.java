package br.com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum SpecialtyTeacher {
    BACK_END("Back-end"),
    FRONT_END("Front-end"),
    DEVOPS("DevOps"),
    FULL_STACK("Full-Stack"),
    DATABASE("Banco de Dados");

    private final String displayName;

    SpecialtyTeacher(String displayName) {
        this.displayName = displayName;
    }
}