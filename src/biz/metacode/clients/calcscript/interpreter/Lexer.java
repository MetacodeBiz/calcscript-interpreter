
package biz.metacode.clients.calcscript.interpreter;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer implements Iterable<String> {

    private static final Pattern pattern = Pattern
            .compile("[a-zA-Z_][a-zA-Z0-9_]*|'(?:\\.|[^'])*'?|\"(?:\\.|[^\"])*\"?|-?[0-9]+\\.[0-9]+|-?[0-9]+|#[^\n\r]*|.");

    private final CharSequence source;

    public Lexer(CharSequence source) {
        this.source = source;
    }

    @Override
    public Iterator<String> iterator() {
        return new LexerIterator(source);
    }

    private final static class LexerIterator implements Iterator<String> {

        private Matcher matcher;

        private boolean hasNextElement;

        private LexerIterator(CharSequence source) {
            matcher = pattern.matcher(source);
            hasNextElement = matcher.find();
        }

        @Override
        public boolean hasNext() {
            return hasNextElement;
        }

        @Override
        public String next() {
            String result = matcher.group(0);
            hasNextElement = matcher.find();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
