import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private Token current;
    private Lexer lexer;
    public Map<String, Float> hashmap = new HashMap<String, Float>();
    public String varible;

    public Parser(Lexer lexer) throws Exception{
        this.lexer = lexer;
        current = this.lexer.nextToken();
    }

    public void  checkTokenType(TokenType type) throws Exception {
        if (current.getType() == type) {
            current = lexer.nextToken();
        }
        else {
            throw new Exception("Token error");
        }
    }

    private Node factor() throws Exception{
        Token token = current;
        if (token.getType().equals(TokenType.VAR)) {
            if (hashmap.containsKey(token.toString())) {
                token = new Token(TokenType.INTEGER, hashmap.get(token).toString());
                return new Number(token);
            }
            else {
                varible = token.toString();
                checkTokenType(TokenType.VAR);
                return new NameVar(token);
            }
        }
        else if (token.getType().equals(TokenType.INTEGER)) {
            checkTokenType(TokenType.INTEGER);
            return new Number(token);
        }
        else if (token.getType().equals(TokenType.LPAREN)) {
            checkTokenType(TokenType.LPAREN);
            Node result = expr();
            checkTokenType(TokenType.RPAREN);
            return result;
        }

        throw new Exception("Factor error");
    }

    private Node term() throws Exception {
        Node result = factor();
        List<TokenType> ops = Arrays.asList(TokenType.RAV, TokenType.DIV, TokenType.MUL);
        while (ops.contains(current.getType())) {
            Token token = current;
            if (token.getType().equals(TokenType.DIV)) {
                checkTokenType(TokenType.DIV);
            }
            else if (token.getType().equals(TokenType.MUL)) {
                checkTokenType(TokenType.MUL);
            }
            else if (token.getType().equals(TokenType.RAV)) {
                checkTokenType(TokenType.RAV);
            }
            result = new BinOp(result, token, factor());
        }
        return result;
    }

    public Node expr() throws Exception {
        List<TokenType> ops = Arrays.asList(TokenType.PLUS, TokenType.MINUS);
        Node result = term();
        while (ops.contains(current.getType())) {
            Token token = current;
            if (token.getType().equals(TokenType.PLUS)){
                checkTokenType(TokenType.PLUS);
            }
            else if (token.getType().equals(TokenType.MINUS)) {
                checkTokenType(TokenType.MINUS);
            }
            result = new BinOp(result, token, term());
        }
        return result;
    }

    public Node parse() throws Exception {
        return expr();
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("2 + (2 * 3) - 4");
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());
    }
}
