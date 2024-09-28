import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class TestLexer {
    public static void main(String[] args) throws IOException {
        // Cargar el archivo de entrada
        CharStream input = CharStreams.fromFileName("input.txt");

        // Crear el lexer basado en el archivo de entrada
        SimpleLexer lexer = new SimpleLexer(input);

        // Obtener los tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Recorrer y mostrar los tokens
        tokens.fill();
        for (Token token : tokens.getTokens()) {
            String tokenName = SimpleLexer.VOCABULARY.getSymbolicName(token.getType());
            System.out.printf("[Numero de token: %d, Atributo reconocido: '%s', Nombre del token: <%s>]\n",
                token.getTokenIndex(),
                token.getText(),
                tokenName
                );
        }
    }
}
