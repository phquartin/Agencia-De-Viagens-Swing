package br.com.agenciaviagens.ui.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LettersOnlyFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // Permite apenas letras e espaços
        if (text == null || text.matches("[a-zA-Z\\s]*")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}