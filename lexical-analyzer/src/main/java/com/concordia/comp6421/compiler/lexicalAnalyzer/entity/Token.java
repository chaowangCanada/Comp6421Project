package com.concordia.comp6421.compiler.lexicalAnalyzer.entity;
import lombok.*;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Token {

    @Getter
    @Setter
    private TokenType tokenType;

    @Getter
    @Setter
    private String value;

    @Getter
    @Setter
    private int location;
}
