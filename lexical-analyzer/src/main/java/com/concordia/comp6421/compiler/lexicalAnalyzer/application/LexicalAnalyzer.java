package com.concordia.comp6421.compiler.lexicalAnalyzer.application;

import com.concordia.comp6421.compiler.common.ReservedDictionary;
import com.concordia.comp6421.compiler.lexicalAnalyzer.Exception.NoSuchLexElemException;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.TokenType;
import lombok.Getter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class LexicalAnalyzer {

    private boolean isBackup;
    private Character backupCharacter;
    private Queue<Character> buffer;
    private int location;
    private int handlingLocation  = 1;
    private static StringBuilder tokenBuilder  = new StringBuilder();
    @Getter
    private String lineContent;

    public LexicalAnalyzer(String str){
        lineContent = str;
        List<Character> list = str.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        buffer = new ArrayDeque<>(list);
    }

    public void setLineContent(String lineContent) {
        handlingLocation  = 1;
        this.lineContent = lineContent;
        buffer = new ArrayDeque<>(lineContent.chars().mapToObj(e->(char)e).collect(Collectors.toList()));
    }

    public LexicalAnalyzer(){
        buffer = new ArrayDeque<Character>();
    }

    public Token nextToken() {
        Token token = null;
        try {
            token = nextTokenAux();
            while (token != null) {
                token = nextTokenAux();
            }
        } catch (NoSuchLexElemException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean hasNext(){
        return !lineContent.isEmpty() && !buffer.isEmpty(); //&& isBackup == false
    }

    private Token nextTokenAux() throws NoSuchLexElemException {
        Token token = null;
        Character ch = nextChar();

        if (ch == null) {
            backupChar();
            return null;
        }
        // if the first character is a space just ignore it
        if (handlingLocation  != location)
            handlingLocation  = location;

        while (ch == ' ') {
            ch = nextChar();
        }

        // if the first character is a letter, it should be a keyword or an identifier
        if (Character.isLetter(ch)) {
            tokenBuilder.append(ch);
            ch = nextChar();
            while ((Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
                    && handlingLocation  == location) {
                tokenBuilder.append(ch);
                ch = nextChar();
            }

            String word = tokenBuilder.toString();
            if (isKeyWord(word)) {
                token = new Token(Enum.valueOf(TokenType.class, word.toUpperCase()), word, handlingLocation );
            } else {
                token = new Token(TokenType.IDENTIFIER, word, handlingLocation );
            }
        }
        // if the first character is a number, there may be two situation:
        // one is that it is an integer, the other is that it is a floating number
        else if (Character.isDigit(ch)) {
            boolean isZero = true;
            boolean isFloat = false;
            boolean hasDot = false;
            boolean hasExp = false;
            boolean hasSign = false;

            if (ch == '0')
                isZero = true;
            tokenBuilder.append(ch);

            // take the next character
            ch = nextChar();
            if (Character.isDigit(ch) && isZero) {
                backupChar();
                return new Token(TokenType.INTEGER, "0", handlingLocation );
            }

            while ((Character.isDigit(ch) || ch == '.' || ch == 'e' ||
                        ch == '+' || ch == '-') && handlingLocation  == location) {

                if (Character.isDigit(ch)) {
                    tokenBuilder.append(ch);
                    ch = nextChar();
                }
                // if ch is '.', we need to check the following character
                else if (ch == '.') {
                    // if the '.' didn't show up before
                    if (!hasDot) {
                        hasDot = true;
                        ch = nextChar();
                        if (!Character.isDigit(ch)) {
                            // if the following character is not a number
                            addPrevCharBack('.',ch);
                            return new Token(TokenType.INTEGER, tokenBuilder.toString(),
                                    location);
                        } else {
                            isFloat = true;
                            tokenBuilder.append(".").append(ch);
                            ch = nextChar();
                        }
                    }
                    // if the '.' did show up before
                    else {
                        String str = tokenBuilder.toString();
                        backupChar();
                        return new Token(TokenType.FLOAT, str, handlingLocation );
                    }
                }
                else if (ch == 'e'){
                    // if the 'e' didn't show up before
                    if (!hasExp) {
                        hasExp = true;
                        ch = nextChar();
                        if (!Character.isDigit(ch) && !Character.isLetter('-')
                                                    && !Character.isLetter('+')) {
                            // if the following character is not a number and not +/-
                            addPrevCharBack('e',ch);
                            return new Token(TokenType.INTEGER, tokenBuilder.toString(),
                                    location);
                        } else {
                            if (hasDot)
                                isFloat = true;
                            else
                                isFloat = false;
                            tokenBuilder.append("e").append(ch);
                            ch = nextChar();
                        }
                    }
                    // if the 'e' did show up before
                    else {
                        String str = tokenBuilder.toString();
                        backupChar();
                        return new Token(isFloat ? TokenType.FLOAT : TokenType.INTEGER, str, handlingLocation);
                    }
                }
                else if (ch == '+' || ch == '-'){
                    // if the '+/-' didn't show up before
                    if (!hasSign) {
                        hasSign = true;
                        char prevChar = ch;
                        ch = nextChar();
                        if (!Character.isDigit(ch) ) {
                            // if the following character is not a number
                            addPrevCharBack(prevChar,ch);
                            return new Token(TokenType.INTEGER, tokenBuilder.toString(),
                                    location);
                        } else {
                            if (hasDot)
                                isFloat = true;
                            else
                                isFloat = false;
                            tokenBuilder.append("e").append(ch);
                            ch = nextChar();
                        }
                    }
                    // if the '+/-' did show up before
                    else {
                        String str = tokenBuilder.toString();
                        backupChar();
                        return new Token(isFloat ? TokenType.FLOAT : TokenType.INTEGER, str, handlingLocation);
                    }
                }
            }
            String num = tokenBuilder.toString();
            token = new Token(isFloat ? TokenType.FLOAT: TokenType.INTEGER, num, handlingLocation);
        }
        // if the first character is neither a letter nor a number
        else {
            if(isWaitFurther(ch)) {
                switch (ch) {
                    case '=':
                        ch = nextChar();
                        if (ch == '=') {
                            token = new Token(TokenType.EQEQ, "==", location);
                        } else {
                            token = new Token(TokenType.EQ, "=", location);
                            backupChar();
                        }
                        break;
                    case '>':
                        ch = nextChar();
                        if (ch == '=') {
                            token = new Token(TokenType.GEQ, ">=", location);
                        } else {
                            token = new Token(TokenType.GT, ">", location);
                            backupChar();
                        }
                        break;
                    case '<':
                        ch = nextChar();
                        if (ch == '=') {
                            token = new Token(TokenType.LEQ, "<=", location);
                        } else if (ch == '>') {
                            token = new Token(TokenType.LTGT, "<>", location);
                        } else {
                            token = new Token(TokenType.LT, "<", location);
                            backupChar();
                        }
                        break;
                    case ':':
                        ch = nextChar();
                        if (ch == ':') {
                            token = new Token(TokenType.COLONCOLON, "::", location);
                        } else {
                            token = new Token(TokenType.COLON, ":", location);
                            backupChar();
                        }
                        break;
                    default:
                        token = new Token(TokenType.ERROR,
                                "Unsupported lexical value " + String.valueOf(ch),
                                location);
                }
            }
            else {
                switch (ch) {
                    case '*':
                        token = new Token(TokenType.MULTIPLICATION, "*", location);
                        break;
                    case '+':
                        token = new Token(TokenType.ADDITION, "+", location);
                        break;
                    case '-':
                        token = new Token(TokenType.SUBTRACTION, "-", location);
                        break;
                    case '/':
                        token = new Token(TokenType.DIVISION, "/", location);
                        break;
                    case '(':
                        token = new Token(TokenType.LP, "(", location);
                        break;
                    case ')':
                        token = new Token(TokenType.RP, ")", location);
                        break;
                    case '{':
                        token = new Token(TokenType.LCP, "{", location);
                        break;
                    case '}':
                        token = new Token(TokenType.RCP, "}", location);
                        break;
                    case '[':
                        token = new Token(TokenType.LSP, "[", location);
                        break;
                    case ']':
                        token = new Token(TokenType.RSP, "]", location);
                        break;
                    case ';':
                        token = new Token(TokenType.SEMICOLON, ";", location);
                        break;
                    case '.':
                        token = new Token(TokenType.DOT, ".", location);
                        break;
                    case ',':
                        token = new Token(TokenType.COMMA, ",", location);
                        break;
                    case '!':
                        token = new Token(TokenType.EXCLAMATION, "!", location);
                        break;
                    case '&':
                        ch = nextChar();
                        if (ch == '&') {
                            token = new Token(TokenType.ANDAND, "&&", location);
                        } else {
                            token = new Token(TokenType.ERROR, "Not Supported Lexical Symbol" + ch, location);
                            backupChar();
                        }
                        break;
                    case '|':
                        ch = nextChar();
                        if (ch == '|') {
                            token = new Token(TokenType.OROR, "||", location);
                        } else {
                            token = new Token(TokenType.ERROR, "Not Supported Lexical Symbol", location);
                            backupChar();
                        }
                        break;
                    default:
                        token = new Token(TokenType.ERROR,
                                "Not supported Lexical Symbol" + String.valueOf(ch), location);
                }
            }
        }
        return token;
    }

    private void addPrevCharBack(char prevChar, char curChar) {
        Queue<Character> tmp = new ArrayDeque<Character>();
        tmp.add(prevChar);
        tmp.add(curChar);
        while (!buffer.isEmpty()) {
            tmp.add(buffer.remove());
        }
        buffer = tmp;
    }

    private void backupChar() {
        isBackup = true;
    }

    private boolean isKeyWord(String word ){
        return ReservedDictionary.KEY_WORDS.stream().anyMatch(str -> str.trim().equalsIgnoreCase(word));
    }

    private boolean isWaitFurther(Character character ){
        return ReservedDictionary.WAIT_FURTHER.stream().anyMatch(character1 -> character1 == character);
    }

    private Character nextChar() {
        if (!isBackup) {
            // if buffer is not empty, dequeue the first one
            backupCharacter = buffer.remove();
            location = lineContent.length() - buffer.size();
        }
        // if there is a backup character, just return the backup
        else {
            isBackup = false;
        }
        return backupCharacter;
    }

}
