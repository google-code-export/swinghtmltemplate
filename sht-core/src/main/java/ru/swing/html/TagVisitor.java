package ru.swing.html;

import ru.swing.html.tags.Tag;

public interface TagVisitor {
    public void visit(Tag tag);
}
