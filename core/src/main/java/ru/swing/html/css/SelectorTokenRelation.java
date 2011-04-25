package ru.swing.html.css;

/**
 * Тип связи от текущего токена к следующему.
 */
public enum SelectorTokenRelation {
    ANY(""), PARENT(">"), SUBLING("+");

    private String symbol;

    SelectorTokenRelation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "SelectorTokenRelation{" +symbol+'}';
    }
}
