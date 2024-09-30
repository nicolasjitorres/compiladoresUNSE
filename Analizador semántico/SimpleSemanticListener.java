import java.util.HashMap;
import java.util.Map;

public class SimpleSemanticListener extends SimpleParserBaseListener {

    private SymbolTable globalTable;
    private SymbolTable currentModuleTable;

    public SimpleSemanticListener() {
        globalTable = new SymbolTable();
        currentModuleTable = null; // Inicia sin un módulo activo

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
            if (context.TIPODATONUM().getText().equals("INT")) {
                String operationResult = evaluateOperacion(context.operacion());
                String[] partes = operationResult.split("\\.");
                String parteEntera = partes[0];
                variableValue = parteEntera;
                System.out.println(variableValue);
            } else {
                String operationResult = evaluateOperacion(context.operacion());
                variableValue = operationResult;
                System.out.println(variableValue);
            }
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
            Variable variableExistente = currentModuleTable.lookupVariable(variableName);
            if (variableType != "") {
                System.err.println("\nError: La variable " + variableName + " ya esta definida.");
                return;
            }

            if (!currentModuleTable.isValueTypeCompatible(variableExistente.getType(), variableValue)) {
                System.err
                        .println("\nError: La variable " + variableName + " es de tipo " + variableExistente.getType());
                return;
            }
            currentModuleTable.updateVariableValue(variableName, variableValue);

        } else {
            // Definir la variable en la tabla de símbolos actual
            currentModuleTable.defineVariable(variableName, variableType, variableValue);
        }

        if (variableType != "") {
            System.out.println("\nVariable detectada de nombre " + variableName
                    + " y tipo " + variableType);
        } else {
            System.out.println("\nVariable detectada de nombre " + variableName);
        }

        if (variableValue != "") {
            System.out.println("La variable tiene como valor: " + variableValue.replaceAll("\"", ""));
        } else {
            System.out.println("La variable no tiene valor asignado.");
        }

    }

    @Override
    public void enterImprimir(SimpleParser.ImprimirContext ctx) {
        System.out.print("Iniciando instruccion de impresion: ");
        if (ctx.imprimirRec() != null) {
            procesarImprimirRec(ctx.imprimirRec(), true); // 'true' para el primer elemento
        }
        System.out.println(); // Salto de línea al final
    }

    public void procesarImprimirRec(SimpleParser.ImprimirRecContext ctx, boolean isFirst) {
        // Si no es el primer elemento, agregar coma
        if (!isFirst) {
            System.out.print(", ");
        }

        if (ctx.CADENA() != null) {
            // Procesar cadenas
            String cadena = ctx.CADENA().getText();
            System.out.print(cadena.substring(1, cadena.length() - 1)); // Quitar comillas
        } else if (ctx.ID() != null) {
            // Procesar variables
            String variableName = ctx.ID().getText();
            if (currentModuleTable != null && currentModuleTable.variableExists(variableName)) {
                String variableValue = currentModuleTable.lookupVariableValue(variableName);
                System.out.print(variableValue);
            } else if (globalTable.variableExists(variableName)) {
                String variableValue = globalTable.lookupVariableValue(variableName);
                System.out.print(variableValue);
            } else {
                System.err.println("Error: La variable " + variableName + " no ha sido definida.");
            }
        }

        // Si hay más elementos a imprimir
        if (ctx.imprimirCont() != null) {
            procesarImprimirCont(ctx.imprimirCont());
        }
    }

    public void procesarImprimirCont(SimpleParser.ImprimirContContext ctx) {
        // Procesar la siguiente parte del PRINT
        if (ctx.imprimirRec() != null) {
            procesarImprimirRec(ctx.imprimirRec(), false); // 'false' para los siguientes elementos
        }
    }

    @Override
    public void enterCondicion(SimpleParser.CondicionContext ctx) {
        if (currentModuleTable == null) {
            currentModuleTable = globalTable;
        }

        try {
            System.out.println("\nCondicion: " + ctx.getText());
            System.out.println("Resultado de la condicion: " + evaluarCondicion(ctx));
        } catch (CondicionInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean evaluarCondicion(SimpleParser.CondicionContext ctx) throws CondicionInvalidaException {
        boolean resultadoCondicion;
        try {
            int index = 0;
            boolean primeraCondicion = evaluarCondicionRec(ctx.condicionRec(0));
            resultadoCondicion = primeraCondicion;
            for (int i = 1; i < ctx.condicionRec().size(); i++) {
                boolean condicion = evaluarCondicionRec(ctx.condicionRec(i));

                if (ctx.OPERADORLOGICO(index).getText().matches("AND")) {
                    resultadoCondicion = resultadoCondicion && condicion;
                } else {
                    resultadoCondicion = resultadoCondicion || condicion;
                }

                index++;
            }
            return resultadoCondicion;
        } catch (TipoIncompatibleException e) {
            throw new CondicionInvalidaException(
                    "La condicion no puede ser evaluada porque existen dos tipos de datos incompatibles.");
        } catch (OperadorInvalidoException e) {
            throw new CondicionInvalidaException(
                    e.getMessage());
        }
    }

    private boolean evaluarCondicionRec(SimpleParser.CondicionRecContext ctx)
            throws TipoIncompatibleException, OperadorInvalidoException {
        if (ctx.LPAREN() != null) {
            try {
                return evaluarCondicion(ctx.condicion());
            } catch (CondicionInvalidaException e) {
                throw new TipoIncompatibleException("Error, tipos incompatibles.");
            }
        }

        // Obtener los términos lógicos
        String terminoIzquierdo = ctx.terminoLogico(0).getText();
        String operador = ctx.OPERADORCOMPARACION().getText();
        String terminoDerecho = ctx.terminoLogico(1).getText();

        // Obtener los valores correspondientes
        String valorIzquierdo = obtenerValor(terminoIzquierdo);
        String valorDerecho = obtenerValor(terminoDerecho);

        // Obtener los tipos
        String tipoIzquierdo = obtenerTipo(ctx.terminoLogico(0));
        String tipoDerecho = obtenerTipo(ctx.terminoLogico(1));

        if (tipoIzquierdo.equals("INT") || tipoIzquierdo.equals("FLOAT")) {
            tipoIzquierdo = "NUM";
        }
        if (tipoDerecho.equals("INT") || tipoDerecho.equals("FLOAT")) {
            tipoDerecho = "NUM";
        }

        if (!tipoIzquierdo.equals(tipoDerecho)) {
            System.out.println("ERROR: Tipo izquierdo: " + tipoIzquierdo + " y tipo derecho: " + tipoDerecho);
            throw new TipoIncompatibleException("Error, tipos incompatibles.");
        }

        if (tipoIzquierdo.equals("STRING") || tipoIzquierdo.equals("BOOLEAN")) {
            switch (operador) {
                case "==":
                    return valorIzquierdo.equals(valorDerecho);
                case "!=":
                    return !valorIzquierdo.equals(valorDerecho);
                default:
                    throw new OperadorInvalidoException(
                            "No esta permitida la comparacion: " + operador + " entre tipos de dato " + tipoIzquierdo);
            }
        }

        switch (operador) {
            case "==":
                return Double.parseDouble(valorIzquierdo) == Double.parseDouble(valorDerecho);
            case "!=":
                return Double.parseDouble(valorIzquierdo) != Double.parseDouble(valorDerecho);
            case "<":
                return Double.parseDouble(valorIzquierdo) < Double.parseDouble(valorDerecho);
            case "<=":
                return Double.parseDouble(valorIzquierdo) <= Double.parseDouble(valorDerecho);
            case ">":
                return Double.parseDouble(valorIzquierdo) > Double.parseDouble(valorDerecho);
            case ">=":
                return Double.parseDouble(valorIzquierdo) >= Double.parseDouble(valorDerecho);
            default:
                return false;
        }
    }

    private String obtenerTipo(SimpleParser.TerminoLogicoContext ctx) {

        if (currentModuleTable == null) {
            currentModuleTable = globalTable;
        }

        String type = "";

        if (ctx.CADENA() != null) {
            type = "STRING";
        }
        if (ctx.ID() != null) {
            type = currentModuleTable.lookupVariable(ctx.ID().getText()).getType().toString();
        }
        if (ctx.operacion() != null) {
            type = "INT";
        }
        return type;
    }

    private String obtenerValor(String nombre) {
        if (currentModuleTable != null && currentModuleTable.variableExists(nombre)) {
            return currentModuleTable.lookupVariableValue(nombre);
        } else if (globalTable.variableExists(nombre)) {
            return globalTable.lookupVariableValue(nombre);
        }
        return nombre;
    }

    private String evaluateExpression(String left, String operator, String right) throws DivisioCeroException {
        // Convierte los operandos a Float
        Float leftValue = Float.parseFloat(left);
        Float rightValue = Float.parseFloat(right);
        String resultado;
        // Valor muy pequeño
        final float epsilon = 1e-7f;

        if (leftValue == null || rightValue == null)
            return null;

        // Realiza la operación según el operador
        switch (operator) {
            case "+":
                resultado = "" + (leftValue + rightValue);
                break;
            case "-":
                resultado = "" + (leftValue - rightValue);
                break;
            case "*":
                resultado = "" + (leftValue * rightValue);
                break;
            case "/":
                if (Math.abs(rightValue) < epsilon) {
                    throw new DivisioCeroException("Error: Division por cero. No se pudo ejecutar el programa correctamente...");
                }
                resultado = "" + (leftValue / rightValue);
                break;
            case "%":
                resultado = "" + (leftValue % rightValue);
                break;
            case "^":
                resultado = "" + (Math.pow(leftValue, rightValue));
                break;
            default:
                return null;
        }

        return resultado;
    }

    private String evaluateOperacion(SimpleParser.OperacionContext ctx) {
        System.out.println("Evaluando operacion: " + ctx.getText()); // Impresión para depuración
        String result = evaluateTermino(ctx.termino());
        
        if (result == null) {
            return null;
        }

        try {
            SimpleParser.OperacionRecContext operacionRecCtx = ctx.operacionRec();
            while (operacionRecCtx != null) {
                String operator = operacionRecCtx.OPTERCERNIVEL().getText();
                String rightResult = evaluateTermino(operacionRecCtx.termino());
                System.out.println("Evaluando: " + result + " " + operator + " " + rightResult); // Impresión para
                                                                                                 // depuración
                result = evaluateExpression(result.toString(), operator, rightResult.toString());
                operacionRecCtx = operacionRecCtx.operacionRec();
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return result;
    }

    private String evaluateTermino(SimpleParser.TerminoContext ctx) {
        String leftResult = evaluateFactor(ctx.factor());

        try {
            SimpleParser.TerminoRecContext terminoRecCtx = ctx.terminoRec();
            while (terminoRecCtx != null) {
                String operator = terminoRecCtx.OPSEGUNDONIVEL().getText();
                String rightResult = evaluateFactor(terminoRecCtx.factor());
                leftResult = evaluateExpression(leftResult, operator, rightResult);
                terminoRecCtx = terminoRecCtx.terminoRec();
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return leftResult;
    }

    private String evaluateFactor(SimpleParser.FactorContext ctx) {
        if (ctx.LPAREN() != null) {
            return evaluateOperacion(ctx.operacion());
        }

        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            return obtenerValor(varName);
        }

        if (ctx.NUMERO() != null) {
            return ctx.NUMERO().getText();
        }

        try {
            if (ctx.OPPRIMERNIVEL() != null) {
                String leftResult = evaluateFactor(ctx.factor(0)).toString();
                String rightResult = evaluateFactor(ctx.factor(1)).toString();
                String operator = ctx.OPPRIMERNIVEL().getText();
                return evaluateExpression(leftResult, operator, rightResult);
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return null;
    }

    @Override
    public void enterOperacion(SimpleParser.OperacionContext ctx) {
        System.out.println("Entrando a la operacion: " + ctx.getText());
        String result = evaluateOperacion(ctx);
        System.out.println("Resultado de la operacion: " + result);
    }

}
