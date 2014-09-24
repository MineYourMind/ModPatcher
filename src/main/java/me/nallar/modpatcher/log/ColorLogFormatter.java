package me.nallar.modpatcher.log;

public class ColorLogFormatter extends LogFormatter {
    protected boolean shouldColor() {
        return colorEnabled;
    }
}