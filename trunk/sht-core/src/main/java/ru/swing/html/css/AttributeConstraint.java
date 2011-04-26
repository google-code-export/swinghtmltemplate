package ru.swing.html.css;

/**
 * http://www.w3.org/TR/CSS2/selector.html
 */
public enum AttributeConstraint {

    /**
     * E[foo]	Matches any E element with the "foo" attribute set (whatever the value).
     */
    HAS_ATTRIBUTE,
    /**
     * E[foo="warning"]	Matches any E element whose "foo" attribute value is exactly equal to "warning".
     */
    EQUALS,
    /**
     * E[lang|="en"]	Matches any E element whose "lang" attribute has a hyphen-separated list of values beginning
     * (from the left) with "en".
     */
    STARTS_WITH,
    /**
     * E[foo~="warning"]	Matches any E element whose "foo" attribute value is a list of space-separated values,
     * one of which is exactly equal to "warning".
     */
    HAS_VALUE

}
