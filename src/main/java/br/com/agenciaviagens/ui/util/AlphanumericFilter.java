package br.com.agenciaviagens.ui.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class AlphanumericFilter extends DocumentFilter {

    private final int maxLength;

    public AlphanumericFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int currentLength = fb.getDocument().getLength();
        int newLength = currentLength - length + (text == null ? 0 : text.length());

        // Converte o texto para maiúsculas antes de validar e inserir
        String upperCaseText = text == null ? null : text.toUpperCase();

        // Se o novo texto não ultrapassar o tamanho máximo e contiver apenas letras MAIÚSCULAS/números...
        if (newLength <= maxLength && (upperCaseText == null || upperCaseText.matches("[A-Z0-9]*"))) {
            super.replace(fb, offset, length, upperCaseText, attrs);
        } else {
            // Beep para indicar entrada inválida
            Toolkit.getDefaultToolkit().beep();
        }
    }
}