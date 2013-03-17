
package biz.metacode.calcscript.interpreter.source;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer implements Iterable<String> {

    private static final Pattern SYMBOL_PATTERN = Pattern
            .compile("[a-zA-Z_][a-zA-Z0-9_]*|'(?:\\.|[^'])*'?|\"(?:\\.|[^\"])"
                    + "*\"?|-?[0-9]*\\.[0-9]+|-?[0-9]+|#[^\n\r]*|.");

    private final CharSequence source;

    public Lexer(final CharSequence source) {
        this.source = source;
    }

    public Iterator<String> iterator() {
        return new LexerIterator(source);
    }

    private static final class LexerIterator implements Iterator<String> {

        private Matcher matcher;

        private boolean hasNextElement;

        private LexerIterator(final CharSequence source) {
            matcher = SYMBOL_PATTERN.matcher(source);
            hasNextElement = matcher.find();
        }

        public boolean hasNext() {
            return hasNextElement;
        }

        public String next() {
            String result = matcher.group(0);
            hasNextElement = matcher.find();
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
