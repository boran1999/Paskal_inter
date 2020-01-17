import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements NodeVisitor{
    private Parser parser;

    public Interpreter(Parser parser) throws Exception {
        this.parser = parser;
    }

    @Override
    public float visit(Node node) throws Exception {
        if (node.getClass().equals(BinOp.class)) {
            return visitBinOp(node);
        }
        else if (node.getClass().equals(Number.class)) {
            return visitNumber(node);
        }
        else if (node.getClass().equals(NameVar.class)) {
            return visitString(node);
        }
        throw new Exception("Interpreter error");
    }

    public float visitBinOp(Node node) throws Exception {
        BinOp binOp = (BinOp) node;
//        System.out.println("Visit " + binOp);
        if (binOp.getOp().getType().equals(TokenType.PLUS)) {
            return visit(binOp.getLeft()) + visit(binOp.getRight());
        }
        else if (binOp.getOp().getType().equals(TokenType.MINUS)) {
            return visit(binOp.getLeft()) - visit(binOp.getRight());
        }
        else if (binOp.getOp().getType().equals(TokenType.DIV)) {
            return visit(binOp.getLeft()) / visit(binOp.getRight());
        }
        else if (binOp.getOp().getType().equals(TokenType.MUL)) {
            return visit(binOp.getLeft()) * visit(binOp.getRight());
        }
        else if (binOp.getOp().getType().equals(TokenType.RAV)) {
            return visit(binOp.getRight());
        }

        throw new Exception("Interpreter error");
    }

    public float visitNumber(Node node) {
        Number number = (Number) node;
//        System.out.println("Visit " + node);
        return Float.parseFloat(number.getToken().getValue());
    }

    public float visitString(Node node) {
        NameVar v = (NameVar) node;
        if (parser.hashmap.containsKey(v.toString())) {
            return parser.hashmap.get(v.toString());
        }
        else {
            return 0;
        }
    }

    public float interpret() throws Exception {
        Node tree = parser.parse();
        float answer = visit(tree);
        parser.hashmap.put(parser.varible, answer);
        return answer;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Float> hashmap = new HashMap<String, Float>();

        while (true) {
            System.out.print("in> ");
            String text = reader.readLine();
//            System.out.println(": " + text);

            if (text.equals("END.") | text.length() < 1)
            {
                break;
            }

            if (text.equals("BEGIN") || text.equals("END;")) {
                continue;
            }

            Interpreter interp = new Interpreter(new Parser(new Lexer(text)));
            interp.parser.hashmap = hashmap;
//            System.out.print("out> ");
            try {
                float result = interp.interpret();
//                System.out.println(interp.parser.hashmap);
                hashmap = interp.parser.hashmap;
//                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
//            Token t = interp.nextToken();
//
//            while (!t.getType().equals(TokenType.EOL)) {
//                System.out.println(t);
//                t = interp.nextToken();
//            }
        }

        for (Map.Entry entry: hashmap.entrySet()) {
            System.out.println(entry.getKey().toString() + ": " + entry.getValue());
        }
    }

}
