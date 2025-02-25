package com.raulrh.tiendatelevisiones.base;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A custom implementation of PlainDocument that limits the number of characters
 * that can be inserted into the document.
 * This class enforces a maximum length constraint on the text.
 */
public class LimitedDocument extends PlainDocument {
    private final int limit;

    /**
     * Constructs a LimitedDocument with the specified character limit.
     *
     * @param limit The maximum number of characters allowed in the document.
     */
    public LimitedDocument(int limit) {
        this.limit = limit;
    }

    /**
     * Inserts a string into the document at the specified offset, ensuring that
     * the total length of the document does not exceed the specified limit.
     *
     * @param offset The offset at which to insert the string.
     * @param str    The string to insert.
     * @param attr   The attributes for the inserted string.
     * @throws BadLocationException If the given offset is invalid.
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}