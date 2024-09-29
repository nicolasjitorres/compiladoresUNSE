import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintWriter;

public class TestParser {
    public static void main(String[] args) throws Exception {
        // Lee el archivo de entrada
        String input = new String(Files.readAllBytes(Paths.get("input.txt")));
        // Crea un CharStream a partir del input
        CharStream charStream = CharStreams.fromString(input);

        // Crea el lexer con el CharStream
        SimpleLexer lexer = new SimpleLexer(charStream);

        // Usa un CommonTokenStream para alimentar los tokens al parser
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Crea el parser con el flujo de tokens
        SimpleParser parser = new SimpleParser(tokens);

        // Inicia el análisis llamando la regla inicial de tu parser
        ParseTree tree = parser.inicio();

        SimpleSemanticListener listener = new SimpleSemanticListener(); // Inicialización del listener
        ParseTreeWalker walker = new ParseTreeWalker(); // Inicialización del walker
        walker.walk(listener, tree); // Caminar por el árbol

        // Muestra el árbol de derivación en texto
        // System.out.println(tree.toStringTree(parser));

        // Generar archivo DOT
        // try (PrintWriter writer = new PrintWriter("tree.dot")) {
        // writer.println("digraph G {");
        // writer.println(" node [shape=record];");
        // writer.println(" " + generateDot(tree, parser));
        // writer.println("}");
        // }
    }

    private static String generateDot(ParseTree tree, Parser parser) {
        StringBuilder sb = new StringBuilder();
        String nodeName = "node" + tree.hashCode();

        // Comprobar si el nodo es un TerminalNode
        if (tree instanceof TerminalNode) {
            TerminalNode terminalNode = (TerminalNode) tree;
            String terminalText = terminalNode.getText(); // Obtener el texto del nodo terminal
            // Escapar caracteres problemáticos en el texto del nodo terminal
            terminalText = escapeLabel(terminalText);
            sb.append("  ").append(nodeName).append(" [label=\"").append(terminalText).append("\"];\n");
        } else {
            // Obtén el nombre de la regla sin "Context"
            String label = tree.getClass().getSimpleName();
            if (label.endsWith("Context")) {
                label = label.substring(0, label.length() - "Context".length());
            }
            // Ignorar el label si es "TerminalNodeImpl"
            if (!label.equals("TerminalNodeImpl")) {
                // Escapar caracteres problemáticos en la etiqueta
                label = escapeLabel(label);
                sb.append("  ").append(nodeName).append(" [label=\"").append(label).append("\"];\n");
            }
        }

        // Recursivamente añade los nodos hijos
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            String childName = "node" + child.hashCode();
            sb.append(generateDot(child, parser)); // Recursividad
            sb.append("  ").append(nodeName).append(" -> ").append(childName).append(";\n");
        }
        return sb.toString();
    }

    private static String escapeLabel(String label) {
        // Escapa comillas, barras invertidas y caracteres especiales
        return label.replace("\\", "\\\\") // Escapa barra invertida
                .replace("\"", "\\\"") // Escapa comillas dobles
                .replace("\n", "\\n") // Escapa nueva línea
                .replace("\r", "\\r") // Escapa retorno de carro
                .replace("\t", "\\t") // Escapa tabulaciones
                .replace("<", "\\<") // Escapa menor que
                .replace(">", "\\>") // Escapa mayor que
                .replace("&", "&amp;"); // Escapa ampersand
    }

}
