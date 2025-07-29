package br.com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum TopicStatus {
    NOT_ANSWERED("Não Respondido"),
    IN_DISCUSSION("Em Discussão"),
    SOLVED("Solucionado"),
    CLOSED("Fechado");

    private final String displayName;

    TopicStatus(String displayName) {
        this.displayName = displayName;
    }
}