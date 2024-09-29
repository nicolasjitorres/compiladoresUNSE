import org.antlr.v4.runtime.tree.TerminalNode;

public class SimpleSemanticListener extends SimpleParserBaseListener {

    private SymbolTable globalTable;
    private SymbolTable currentModuleTable;

    public SimpleSemanticListener() {
        globalTable = new SymbolTable();
        currentModuleTable = null; // Inicia sin un módulo activo
    }

    @Override
    public void enterModuloInicio(SimpleParser.ModuloInicioContext ctx) {
        String moduleName = ctx.ID().getText();
        globalTable.defineModule(moduleName, ctx.nullOrNombre().getText());
        currentModuleTable = new SymbolTable(); // Crear nueva tabla de símbolos local para el módulo
        System.out.println("Se ha definido un nuevo modulo: " + moduleName);
    }

    @Override
    public void exitModuloInicio(SimpleParser.ModuloInicioContext ctx) {
        currentModuleTable = null; // Al salir del módulo, no hay módulo activo
    }

    @Override
    public void enterLlamada(SimpleParser.LlamadaContext ctx) {
        // Verifica si el módulo llamado ha sido definido
        // String moduleName = ctx.ID().getText(); // Obtener el nombre del módulo

        // // Asegúrate de que globalTable esté correctamente inicializado antes de
        // usarlo
        // if (globalTable.lookupModule(moduleName) == null) {
        // System.err.println("Error: El módulo " + moduleName + " no ha sido definido
        // antes de la llamada.");
        // } else {
        // System.out.println("Llamada a módulo detectada: " + moduleName);
        // }

        System.out.println(ctx.toString());
    }

    @Override
    public void enterVariable(SimpleParser.VariableContext context) {
        String variableName = context.ID().getText();
        String variableType = "";
        String variableValue = "";

        if (context.tipo() != null) {
            variableType = context.tipo().getText();
        } else if (context.TIPODATONUM() != null) {
            variableType = context.TIPODATONUM().toString();
        } else if (context.BOOLEAN() != null) {
            variableType = context.BOOLEAN().toString();
        } else if (context.STRING() != null) {
            variableType = context.STRING().toString();
        }

        if (context.CADENA() != null) {
            variableValue = context.CADENA().getText();
        } else if (context.BOOLEANO() != null) {
            variableValue = context.BOOLEANO().getText();
        } else if (context.operacion() != null) {
            variableValue = context.operacion().getText();
        } else if (context.variablePrima() != null) {
            variableValue = context.variablePrima().getText();
        }

        // Verificar si currentModuleTable está inicializada
        if (currentModuleTable == null) {
            // Usar la tabla global si no hay un módulo activo
            currentModuleTable = globalTable;
        }

        // Verificar si la variable ya existe en la tabla de símbolos
        if (currentModuleTable.variableExists(variableName)) {
            if (variableType == "") {
                currentModuleTable.updateVariableValue(variableName, variableValue);
            } else {
                System.err.println("\nError: La variable " + variableName + " ya esta definida.");
                return;
            }
        } else {
            // Definir la variable en la tabla de símbolos actual
            currentModuleTable.defineVariable(variableName, variableType, variableValue);
        }

        // Verificación opcional para el nombre de la variable
        if (!variableName.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            System.err.println("Error: El nombre de la variable " + variableName + " no es válido.");
        }

        if (variableType != "") {
            System.out.println("\nVariable detectada de nombre " + variableName
                    + " y tipo " + variableType);
        } else {
            System.out.println("\nVariable detectada de nombre " + variableName);
        }

        if (variableValue != "") {
            System.out.println("La variable tiene como valor: " + variableValue);
        } else {
            System.out.println("La variable no tiene valor asignado.");
        }

    }

    // @Override
    // public void enterVariable(SimpleParser.VariableContext ctx) {
    // String variableName = ctx.ID().getText();
    // String variableType = "";
    // System.out.println(ctx.tipo());
    // System.out.println(ctx.getText());
    // System.out.println(ctx.toString());
    // if (ctx.tipo() == null) {
    // } else {
    // variableType = ctx.tipo().getText();
    // }

    // // Mostrar mensaje en consola
    // System.out.println("Se ha definido una variable: " + variableName + " de tipo
    // " + variableType);

    // // Verificar si currentModuleTable está inicializada
    // if (currentModuleTable == null) {
    // currentModuleTable = globalTable; // Usar la tabla global si no hay un módulo
    // activo
    // }

    // // Definir la variable en la tabla de símbolos actual
    // currentModuleTable.defineVariable(variableName, variableType);

    // // Verificación opcional para el nombre de la variable
    // if (!variableName.matches("[a-zA-Z][a-zA-Z0-9]*")) {
    // System.err.println("Error: El nombre de la variable " + variableName + " no
    // es válido.");
    // }
    // }

    // Otros métodos, como la validación de números y operaciones, permanecen
    // iguales...
}
