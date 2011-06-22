package ru.swing.html.css;

/**
 * The relation type of previous token to next
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
