package br.com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum StackTeacher {
    JAVA_SPRING("Java & Spring"),
    TS_ANGULAR("TypeScript & Angular"),
    TS_NEXT("TypeScript & Next.js"),
    PYTHON_DJANGO("Python & Django"),
    DOTNET(".NET"),
    PHP_LARAVEL("PHP & Laravel");

    private final String displayName;

    StackTeacher(String displayName) {
        this.displayName = displayName;
    }
}