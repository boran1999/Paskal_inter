public class Lexer {
    private String text;
    private int pos = -1;
    private Character currentChar;

    public Token nextToken() throws Exception {
        while (currentChar != null) {

            if (Character.isSpaceChar(currentChar))
            {
                skip();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                return new Token(TokenType.INTEGER, number());
            }

            if (currentChar == '+') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.PLUS, "" + temp);
            }

            if (currentChar == '-') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.MINUS, "" + temp);
            }

            if (currentChar == '*') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.MUL, "" + temp);
            }

            if (currentChar == '/') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.DIV, "" + temp);
            }

            if (currentChar == '(') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.LPAREN, "" + temp);
            }

            if (currentChar == ')') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.RPAREN, "" + temp);
            }

            if (currentChar == '^') {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.POW, "" + temp);
            }

            if (Character.isLetter(currentChar)) {
                Character temp = currentChar;
                forward();
                return new Token(TokenType.VAR, "" + temp);
            }

            if (currentChar == ':') {
                forward();
                if (currentChar == '=') {
                    Character temp = currentChar;
                    forward();
                    return new Token(TokenType.RAV, "" + temp);
                }
            }

            throw new Exception("Parsing error!");
        }
        return new Token(TokenType.EOL, null);
    }

    private void skip() {
        while ((currentChar != null) && Character.isSpaceChar(currentChar)) {
            forward();
        }
    }

    private void skipAl() {
        while (currentChar != '=') {
            forward();
        }
    }

    private String number() throws Exception{
        String result = "";
        int dot = 0;
        while ((currentChar != null) && (Character.isDigit(currentChar) || currentChar.equals('.'))) {
            if (currentChar.equals('.')) {
                dot++;
                if (dot > 1) {
                    throw new Exception("Too many dot!!!");
                }
            }
            result += currentChar;
            forward();
        }
        return result;
    }

    private void forward() {
        pos += 1;
        if (pos > text.length() - 1) {
            currentChar = null;
        }
        else {
            currentChar = text.charAt(pos);
        }
    }

    public Lexer(String text) {
        this.text = text;
        forward();
    }
}