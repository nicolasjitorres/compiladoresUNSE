import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TestParser {
    public static void main(String[] args) throws Exception {
        // Leer el archivo de entrada o usar una cadena de entrada si lo prefieres
        CharStream input = CharStreams.fromFileName("entrada.txt"); // Usa un archivo de entrada
        // Si prefieres entrada por consola, utiliza: CharStream input = CharStreams.fromStream(System.in);

        // Crear el lexer a partir de la entrada
        SimpleLexer lexer = new SimpleLexer(input);

        // Crear un buffer de tokens del lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Crear el parser a partir de los tokens
        SimpleParser parser = new SimpleParser(tokens);

        // Agregar un Listener para manejar errores y ver advertencias
        parser.removeErrorListeners(); // Elimina los listeners por defecto
        parser.addErrorListener(new DiagnosticErrorListener()); // Agrega un listener detallado
        parser.addErrorListener(new ConsoleErrorListener()); // Agrega errores en la consola

        // Iniciar la regla inicial de tu gramática
        ParseTree tree = parser.inicio(); // Aquí debes usar la regla inicial de tu gramática

        // Imprimir el árbol de análisis resultante
        System.out.println(tree.toStringTree(parser)); // Imprime el árbol en formato textual
    }
}

