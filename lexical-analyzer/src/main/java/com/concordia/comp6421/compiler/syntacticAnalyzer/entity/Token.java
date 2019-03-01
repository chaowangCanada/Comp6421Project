package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;
import jdk.nashorn.internal.objects.NativeNumber;
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

    @Getter
    @Setter
    private int lineNum;

}
