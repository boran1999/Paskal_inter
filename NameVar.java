public class NameVar extends Node {
    private Token token;

    public NameVar(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return String.format("String (%s)", token);
    }
}
