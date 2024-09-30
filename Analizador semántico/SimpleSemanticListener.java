import java.util.HashMap;
import java.util.Map;

public class SimpleSemanticListener extends SimpleParserBaseListener {

    private SymbolTable globalTable;
    private SymbolTable currentModuleTable;
    private Map<String, Integer> variableValues = new HashMap<>();
    private String currentVariableName;

    public SimpleSemanticListener() {
        globalTable = new SymbolTable();
        currentModuleTable = null; // Inicia sin un módulo activo

    }

    @Override
    public void enterVariable(SimpleParser.VariableContext context) {
        currentVariableName = context.ID().getText();
        String variableName = context.ID().getText();
        String variableType = "";
        String variableValue = "";

        System.out.println("Estoy en enterVariable");

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
            System.out.println("Estoy adentrooo");
            // Caso TIPODATONUM ID OPERADORASIGNACION operacion
            if (context.TIPODATONUM().getText() == "FLOAT") { //Este camino no está cambiado
                Integer operationResult = evaluateOperacion(context.operacion()); 
                if (operationResult != null) {
                    variableValue = operationResult.toString();
                    System.out.println(variableValue);
                } else {
                    System.err.println("Error al evaluar la operación para la variable: " + variableName);
                    return;
                }
            } else {
                Integer operationResult = evaluateOperacion(context.operacion());
                if (operationResult != null) {
                    variableValue = operationResult.toString();
                    System.out.println(variableValue);
                } else {
                    System.err.println("Error al evaluar la operación para la variable: " + variableName);
                    return;
                }
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

    // @Override
    // public void enterCondicion(SimpleParser.CondicionContext ctx) {
    // // Este método se llama al entrar en una condición
    // System.out.println("Condicion detectada: " + ctx.getText());

    // // Para verificar las variables en la condición
    // for (SimpleParser.CondicionRecContext condicionRec : ctx.condicionRec()) {
    // // Acceder a los términos lógicos en cada condicionRec
    // if (condicionRec.terminoLogico().size() > 0) {
    // for (SimpleParser.TerminoLogicoContext term : condicionRec.terminoLogico()) {
    // if (term.operacion() != null) {
    // String varName = term.operacion().getText();
    // if (!globalTable.variableExists(varName) &&
    // (currentModuleTable == null || !currentModuleTable.variableExists(varName)))
    // {
    // System.err.println("Error: La variable " + varName + " no esta definida en la
    // condicion.");
    // }
    // System.out.println("Esta es una variable");
    // } else {
    // System.out.println("Este es un numero " + term.operacion().getText());
    // }
    // }
    // }
    // }
    // }

    // @Override
    // public void enterCondicionRec(SimpleParser.CondicionRecContext ctx) {
    // if (ctx.LPAREN() != null) {
    // System.out.println("Condicion anidada detectada.");
    // } else {
    // // Procesar el término lógico y la comparación
    // String terminoIzquierdo = ctx.terminoLogico(0).getText(); // Primer término
    // lógico
    // String operador = ctx.OPERADORCOMPARACION().getText(); // Operador de
    // comparación
    // String terminoDerecho = ctx.terminoLogico(1).getText(); // Segundo término
    // lógico
    // System.out.println("Termino logico y comparacion detectados: " +
    // terminoIzquierdo + " " + operador + " "
    // + terminoDerecho);
    // }
    // }

    // @Override
    // public void enterTerminoLogico(SimpleParser.TerminoLogicoContext ctx) {
    // System.out.println("Término lógico detectado: " + ctx.getText());

    // // Verificar si es un ID
    // if (ctx.operacion() != null) {
    // String varName = ctx.operacion().getText();
    // if (!globalTable.variableExists(varName) &&
    // (currentModuleTable == null || !currentModuleTable.variableExists(varName)))
    // {
    // System.err.println("Error: La variable " + varName + " no está definida en el
    // término lógico.");
    // }
    // }
    // // Verificar si es una operación
    // else if (ctx.operacion() != null) {
    // System.out.println("Este es un resultado de operación: " +
    // ctx.operacion().getText());
    // }
    // // Verificar si es una cadena
    // else if (ctx.CADENA() != null) {
    // System.out.println("Este es una cadena: " + ctx.CADENA().getText());
    // }
    // }

    // @Override
    // public void enterModuloInicio(SimpleParser.ModuloInicioContext ctx) {
    // String moduleName = ctx.ID().getText();
    // currentModuleTable = new SymbolTable();

    // // Definir el módulo sin valor de retorno aún
    // globalTable.defineModule(moduleName, "", "");
    // System.out.println("Se ha definido un nuevo modulo: " + moduleName);

    // enterCuerpo(ctx.cuerpo());

    // System.out.println(currentModuleTable.lookupVariableValue("aux1"));

    // if (currentModuleTable.variableExists(ctx.nullOrNombre().getText())) {
    // String returnValue =
    // currentModuleTable.lookupVariableValue(ctx.nullOrNombre().getText());
    // System.out.println("Valor de retorno del modulo: " + returnValue);
    // } else {
    // System.err.println("Error: La variable de retorno no esta definida.");
    // }
    // }

    // @Override
    // public void enterLlamada(SimpleParser.LlamadaContext ctx) {
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

    // System.out.println(ctx.toString());
    // }

    @Override
    public void enterCondicion(SimpleParser.CondicionContext ctx) {
        // Este método se llama al entrar en una condición
        if (currentModuleTable == null) {
            currentModuleTable = globalTable;
        }

        // Crea una variable para guardar el resultado del analisis de la condicion
        boolean resultadoCondicion = true;
        int index = -1;
        // Recorre una por una las condiciones para verificar el resultado
        for (SimpleParser.CondicionRecContext term : ctx.condicionRec()) {
            boolean condicion = evaluarCondicion(term);
            if (!ctx.OPERADORLOGICO().isEmpty() && index != -1) {
                if (ctx.OPERADORLOGICO(index).getText().matches("AND")) {
                    resultadoCondicion = resultadoCondicion && condicion;
                } else {
                    resultadoCondicion = resultadoCondicion || condicion;
                }
            } else {
                resultadoCondicion = resultadoCondicion && condicion;
            }
            index++;
        }

        // Evaluar la condición
        System.out.println("\nResultado de la condicion: " + resultadoCondicion);
    }

    private boolean evaluarCondicion(SimpleParser.CondicionRecContext ctx) {
        // Obtener los términos lógicos
        String terminoIzquierdo = ctx.terminoLogico(0).getText();
        String operador = ctx.OPERADORCOMPARACION().getText();
        String terminoDerecho = ctx.terminoLogico(1).getText();

        // Obtener los valores correspondientes
        String valorIzquierdo = obtenerValor(terminoIzquierdo);
        String valorDerecho = obtenerValor(terminoDerecho);

        // Obtener los tipos
        String tipoIzquierdo = "";
        String tipoDerecho = "";

        if (ctx.terminoLogico(0).CADENA() != null) {
            tipoIzquierdo = "STRING";
        }
        if (ctx.terminoLogico(1).CADENA() != null) {
            tipoIzquierdo = "STRING";
        }

        // Comparar según el operador
        switch (operador) {
            case "==":
                return valorIzquierdo.equals(valorDerecho);
            case "!=":
                return !valorIzquierdo.equals(valorDerecho);
            case "<":
                return Double.parseDouble(valorIzquierdo) < Double.parseDouble(valorDerecho);
            case "<=":
                return Double.parseDouble(valorIzquierdo) <= Double.parseDouble(valorDerecho);
            case ">":
                return Double.parseDouble(valorIzquierdo) > Double.parseDouble(valorDerecho);
            case ">=":
                return Double.parseDouble(valorIzquierdo) >= Double.parseDouble(valorDerecho);
            default:
                System.err.println("Error: Operador de comparación no soportado: " + operador);
                return false;
        }
    }

    private String obtenerValor(String nombre) {
        // Verificar si es una variable en la tabla actual o global
        if (currentModuleTable != null && currentModuleTable.variableExists(nombre)) {
            return currentModuleTable.lookupVariableValue(nombre);
        } else if (globalTable.variableExists(nombre)) {
            return globalTable.lookupVariableValue(nombre);
        }
        // Si no es una variable, asumir que es un valor numérico o cadena
        return nombre; // Retorna el término como está (podría ser un número)
    }

    private boolean isComparisonValid(String leftType, String rightType) {
        // Sólo permitimos comparaciones entre tipos compatibles
        if (leftType.equals(rightType)) {
            return true; // Si ambos lados tienen el mismo tipo
        }

        // Podríamos agregar más lógica para casos especiales
        System.err.println("Error: No se puede comparar " + leftType + " con " + rightType);
        return false;
    }

    private Integer evaluateExpression(String left, String operator, String right) {
        Integer leftValue = parseValue(left);
        Integer rightValue = parseValue(right);

        if (leftValue == null || rightValue == null)
            return null; // Manejo de error en la evaluación

        switch (operator) {
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "x":
                return leftValue * rightValue;
            case "/":
                return leftValue / rightValue;
            case "%":
                return leftValue % rightValue;
            case "^":
                return (int) Math.pow(leftValue, rightValue);
            default:
                return null; // o lanza un error si el operador no es válido
        }
    }

    // Método para parsear un valor
    private Integer parseValue(String value) {
        if (variableValues.containsKey(value)) {
            return variableValues.get(value); // Devuelve el valor de la variable
        } else {
            try {
                return Integer.parseInt(value); // Trata de convertir a entero
            } catch (NumberFormatException e) {
                return null; // Maneja el error
            }
        }
    }

    private Integer evaluateOperacion(SimpleParser.OperacionContext ctx) {
        System.out.println("Evaluando operación: " + ctx.getText()); // Impresión para depuración
        Integer result = evaluateTermino(ctx.termino());
        System.out.println("Este es el termino" + result);

        // Evaluación de operaciones recursivas
        SimpleParser.OperacionRecContext operacionRecCtx = ctx.operacionRec();
        while (operacionRecCtx != null) {
            String operator = operacionRecCtx.OPTERCERNIVEL().getText();
            Integer rightResult = evaluateTermino(operacionRecCtx.termino());
            System.out.println("Evaluando: " + result + " " + operator + " " + rightResult); // Impresión para depuración
            result = evaluateExpression(result.toString(), operator, rightResult.toString());
            operacionRecCtx = operacionRecCtx.operacionRec();
        }

        return result;
    }

    private Integer evaluateTermino(SimpleParser.TerminoContext ctx) {
        // Evaluar el factor primero
        Integer leftResult = evaluateFactor(ctx.factor());
        System.out.println("Adentro del evaluateTermino" + leftResult);

        // Evaluar términos recursivos si existen
        SimpleParser.TerminoRecContext terminoRecCtx = ctx.terminoRec();
        while (terminoRecCtx != null) {
            String operator = terminoRecCtx.OPSEGUNDONIVEL().getText(); // Obtener el operador
            Integer rightResult = evaluateFactor(terminoRecCtx.factor()); // Evaluar el factor a la derecha

            // Evaluar la expresión actual
            leftResult = evaluateExpression(leftResult.toString(), operator, rightResult.toString());

            // Avanzar al siguiente nivel recursivo, si lo hay
            terminoRecCtx = terminoRecCtx.terminoRec();
        }

        return leftResult;
    }

    private Integer evaluateFactor(SimpleParser.FactorContext ctx) {
        if (ctx.LPAREN() != null) {
            // Evaluar la operación dentro de los paréntesis
            return evaluateOperacion(ctx.operacion());
        } else if (ctx.ID() != null) {
            // Evaluar una variable
            String varName = ctx.ID().getText();
            return variableValues.get(varName);
        } else if (ctx.NUMERO() != null) {
            // Evaluar un número
            return Integer.parseInt(ctx.NUMERO().getText());
        } else if (ctx.OPPRIMERNIVEL() != null) {
            // Evaluar una operación de potencia
            Integer leftResult = evaluateFactor(ctx.factor(0)); // Lado izquierdo
            Integer rightResult = evaluateFactor(ctx.factor(1)); // Lado derecho
            String operator = ctx.OPPRIMERNIVEL().getText(); // Operador
            return evaluateExpression(leftResult.toString(), operator, rightResult.toString());
        }

        return null;
    }

    @Override
    public void enterOperacion(SimpleParser.OperacionContext ctx) {
        System.out.println("Entrando a la operación: " + ctx.getText());

        Integer result = evaluateOperacion(ctx);
        System.out.println("Resultado de la operación: " + result);

        if (result != null) {
            // Guardar el resultado en la tabla de símbolos
            if (currentVariableName != null && !currentVariableName.isEmpty()) {
                String resultValue = result.toString();
                if (currentModuleTable != null) {
                    currentModuleTable.updateVariableValue(currentVariableName, resultValue);
                    System.out
                            .println("Valor de la variable " + currentVariableName + " actualizado a: " + resultValue);
                } else {
                    System.err.println("Error: No se encontró la tabla de símbolos.");
                }
            } else {
                System.err.println("Error: No se pudo encontrar el nombre de la variable.");
            }
        } else {
            System.err.println("Error: No se pudo calcular el resultado de la operación.");
        }
    }

    // private String obtenerTipo(SimpleParser.FactorContext ctx){

    // }

}
