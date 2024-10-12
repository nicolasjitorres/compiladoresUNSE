import java.util.ArrayList;
import java.util.List;

public class SimpleSemanticListener extends SimpleParserBaseListener {

    private SymbolTable globalTable;
    private SymbolTable currentModuleTable;

    public SimpleSemanticListener() {
        globalTable = new SymbolTable();
        currentModuleTable = null;
    }

    public void enterInicio(SimpleParser.InicioContext ctx) {
        if (ctx.cuerpo() != null) {
            ejecutarCuerpo(ctx.cuerpo());
        }
        if (ctx.moduloInicio() != null) {
            ejecutarModuloInicio(ctx.moduloInicio());
        }
        return;
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
        String valorIzquierdo;
        String valorDerecho;
        if (ctx.terminoLogico(0).operacion() != null) {
            valorIzquierdo = evaluarOperacion(ctx.terminoLogico(0).operacion());
        } else {
            valorIzquierdo = obtenerValor(terminoIzquierdo);
        }
        if (ctx.terminoLogico(1).operacion() != null) {
            valorDerecho = evaluarOperacion(ctx.terminoLogico(1).operacion());
        } else {
            valorDerecho = obtenerValor(terminoDerecho);
        }

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

    private String evaluarExpresion(String left, String operator, String right) throws DivisioCeroException {
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
                    throw new DivisioCeroException(
                            "Error: Division por cero. No se pudo ejecutar el programa correctamente...");
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

    private String evaluarOperacion(SimpleParser.OperacionContext ctx) {
        String result = evaluarTermino(ctx.termino());

        if (result == null) {
            return null;
        }

        try {
            SimpleParser.OperacionRecContext operacionRecCtx = ctx.operacionRec();
            while (operacionRecCtx != null) {
                String operator = operacionRecCtx.OPTERCERNIVEL().getText();
                String rightResult = evaluarTermino(operacionRecCtx.termino());
                result = evaluarExpresion(result, operator, rightResult.toString());
                operacionRecCtx = operacionRecCtx.operacionRec();
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return result;
    }

    private String evaluarTermino(SimpleParser.TerminoContext ctx) {
        String leftResult = evaluarFactor(ctx.factor());

        try {
            SimpleParser.TerminoRecContext terminoRecCtx = ctx.terminoRec();
            while (terminoRecCtx != null) {
                String operator = terminoRecCtx.OPSEGUNDONIVEL().getText();
                String rightResult = evaluarFactor(terminoRecCtx.factor());
                leftResult = evaluarExpresion(leftResult, operator, rightResult);
                terminoRecCtx = terminoRecCtx.terminoRec();
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return leftResult;
    }

    private String evaluarFactor(SimpleParser.FactorContext ctx) {
        if (ctx.LPAREN() != null) {
            return evaluarOperacion(ctx.operacion());
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
                String leftResult = evaluarFactor(ctx.factor(0)).toString();
                String rightResult = evaluarFactor(ctx.factor(1)).toString();
                String operator = ctx.OPPRIMERNIVEL().getText();
                return evaluarExpresion(leftResult, operator, rightResult);
            }
        } catch (DivisioCeroException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return null;
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
            if (currentModuleTable.variableExists(ctx.ID().getText())) {
                type = currentModuleTable.lookupVariable(ctx.ID().getText()).getType().toString();
            }
        }
        if (ctx.operacion() != null) {
            type = "INT";
        }
        return type;
    }

    private String obtenerTipoArg(SimpleParser.ArgumentoContext ctx) {

        String type = "";

        if (ctx.CADENA() != null) {
            type = "STRING";
        }
        if (ctx.ID() != null) {
            if (globalTable.variableExists(ctx.ID().getText())) {
                type = globalTable.lookupVariable(ctx.ID().getText()).getType().toString();
            }
        }
        if (ctx.operacion() != null) {
            type = "INT";
        }
        if (ctx.BOOLEANO() != null) {
            type = "BOOLEAN";
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

    public void procesarImprimirRec(SimpleParser.ImprimirRecContext ctx, boolean isFirst) {
        if (!isFirst) {
            System.out.print(", ");
        }

        if (ctx.CADENA() != null) {
            String cadena = ctx.CADENA().getText();
            System.out.print(cadena.substring(1, cadena.length() - 1));
        } else if (ctx.ID() != null) {
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

    private void guardarParametro(SimpleParser.ParametroContext ctx, String nombreModulo) {
        String tipo = ctx.tipo().getText();
        String nombre = ctx.ID().getText();

        if (currentModuleTable.variableExists(nombre)) {
            System.err.println("Error: El parametro " + nombre + " ya esta definido.");
            System.exit(0);
        }

        Module modulo = globalTable.lookupModule(nombreModulo);
        modulo.addParametro(tipo, nombre, null);

        currentModuleTable.defineVariable(nombre, tipo, null);

        if (ctx.parametroRestante() != null) {
            guardarParametroRestante(ctx.parametroRestante(), nombreModulo);
        }
    }

    private void guardarParametroRestante(SimpleParser.ParametroRestanteContext ctx, String nombreModulo) {
        String tipo = ctx.tipo().getText();
        String nombre = ctx.ID().getText();

        if (currentModuleTable.variableExists(nombre)) {
            System.err.println("Error: El parametro " + nombre + " ya esta definido.");
            return;
        }

        Module modulo = globalTable.lookupModule(nombreModulo);
        modulo.addParametro(tipo, nombre, null);

        currentModuleTable.defineVariable(nombre, tipo, null);
        if (ctx.parametroRestante() != null) {
            guardarParametroRestante(ctx.parametroRestante(), nombreModulo);
        }
    }

    private List<Parametro> procesarArgumentos(SimpleParser.ArgumentoLlContext ctx) {
        List<Parametro> argumentos = new ArrayList<>();

        if (ctx.argumento() != null) {
            String tipo = obtenerTipoArg(ctx.argumento());
            String nombre = null;
            String value = ctx.argumento().getText();
            if (ctx.argumento().ID() != null) {
                nombre = ctx.argumento().getText();
                value = globalTable.lookupVariableValue(nombre);
            }

            Parametro arg = new Parametro(tipo, nombre, value);
            argumentos.add(arg);
        }

        if (ctx.argumentoLl() != null) {
            argumentos.addAll(procesarArgumentos(ctx.argumentoLl()));
        }

        return argumentos;
    }

    public void ejecutarCuerpo(SimpleParser.CuerpoContext ctx) {
        if (ctx.accion() != null) {
            ejecutarAccion(ctx.accion());
        }
        if (ctx.cuerpoPrima() != null) {
            ejecutarCuerpoPrima(ctx.cuerpoPrima());
        }
    }

    public void ejecutarCuerpoPrima(SimpleParser.CuerpoPrimaContext ctx) {
        if (ctx.cuerpo() != null) {
            ejecutarCuerpo(ctx.cuerpo());
        }
    }

    public void ejecutarAccion(SimpleParser.AccionContext ctx) {
        if (ctx.si() != null) {
            ejecutarSi(ctx.si());
        }
        if (ctx.mientras() != null) {
            ejecutarMientras(ctx.mientras());
        }
        if (ctx.variable() != null) {
            ejecutarVariable(ctx.variable());
        }
        if (ctx.imprimir() != null) {
            ejecutarImprimir(ctx.imprimir());
        }
        if (ctx.llamada() != null) {
            ejecutarLlamada(ctx.llamada());
        }
    }

    public void ejecutarSi(SimpleParser.SiContext ctx) {
        try {
            boolean condicion = evaluarCondicion(ctx.condicion());
            if (condicion) {
                ejecutarCuerpo(ctx.cuerpo(0));
                return;
            }
            if (ctx.ELSE() != null) {
                ejecutarCuerpo(ctx.cuerpo(1));
                return;
            }

        } catch (CondicionInvalidaException e) {
            System.err.println("ERROR: la condicion ingresada es invalida.");
            System.exit(0);
        }
        return;
    }

    public void ejecutarMientras(SimpleParser.MientrasContext ctx) {
        try {
            boolean condicion = evaluarCondicion(ctx.condicion());
            while (condicion) {
                ejecutarCuerpo(ctx.cuerpo());
                condicion = evaluarCondicion(ctx.condicion());
            }
            return;
        } catch (CondicionInvalidaException e) {
            System.err.println("ERROR: la condicion ingresada es invalida.");
            System.exit(0);
        }
        return;
    }

    public String devolverTipoVar(SimpleParser.VariableContext context){
        if (context.tipo() != null) {
            return context.tipo().getText();
        } else if (context.TIPODATONUM() != null) {
            return context.TIPODATONUM().toString();
        } else if (context.BOOLEAN() != null) {
            return context.BOOLEAN().toString();
        } else if (context.STRING() != null) {
            return context.STRING().toString();
        }
        return "";
    }

    public void ejecutarVariable(SimpleParser.VariableContext context) {
        String variableName = context.ID().getText();
        String variableType = devolverTipoVar(context);
        String variableValue = "";

        System.out.println("Definiendo variable:");

        if (currentModuleTable == null) {
            currentModuleTable = globalTable;
        }

        if (context.CADENA() != null) {
            variableValue = context.CADENA().getText();
        } else if (context.BOOLEANO() != null) {
            variableValue = context.BOOLEANO().getText();
        } else if (context.operacion() != null) {
            if (context.TIPODATONUM().getText().equals("INT")) {
                String operationResult = evaluarOperacion(context.operacion());
                String[] partes = operationResult.split("\\.");
                String parteEntera = partes[0];
                variableValue = parteEntera;
            } else {
                String operationResult = evaluarOperacion(context.operacion());
                variableValue = operationResult;
            }
        } else if (context.variablePrima() != null) {
            if (currentModuleTable.variableExists(variableName)) {
                Variable variableExistente = currentModuleTable.lookupVariable(variableName);
                variableValue = ejecutarVariablePrima(context.variablePrima(), variableExistente.getType());
            }
        }

        if (currentModuleTable.variableExists(variableName)) {
            Variable variableExistente = currentModuleTable.lookupVariable(variableName);
            if (variableType != "") {
                System.err.println("\nError: La variable " + variableName + " ya esta definida.");
                System.exit(0);
            }

            if (!currentModuleTable.isValueTypeCompatible(variableExistente.getType(), variableValue)) {
                System.err
                        .println("\nError: La variable " + variableName + " es de tipo " + variableExistente.getType());
                System.exit(0);
            }
            currentModuleTable.updateVariableValue(variableName, variableValue);

        } else {
            System.out.println("Nombre: "+ variableName + ", tipo: " + variableType + ", valor: " + variableValue);
            currentModuleTable.defineVariable(variableName, variableType, variableValue);
        }

    }

    public String ejecutarVariablePrima(SimpleParser.VariablePrimaContext context, String type) {
        if (context.CADENA() != null && type.equals("STRING")) {
            return context.CADENA().getText();
        } else if (context.BOOLEANO() != null && type.equals("BOOLEAN")) {
            return context.BOOLEANO().getText();
        } else if (context.operacion() != null) {
            if (type.equals("INT")) {
                String operationResult = evaluarOperacion(context.operacion());
                String[] partes = operationResult.split("\\.");
                String parteEntera = partes[0];
                return parteEntera;
            }
            if (type.equals("FLOAT")){
                String operationResult = evaluarOperacion(context.operacion());
                return operationResult;
            }
        }

        System.err.println("ERROR: la variable es de tipo " + type);
        System.exit(0);
        return "";
    }


    public void ejecutarImprimir(SimpleParser.ImprimirContext ctx) {
        System.out.print("Iniciando instruccion de impresion: ");
        if (ctx.imprimirRec() != null) {
            procesarImprimirRec(ctx.imprimirRec(), true);
        }
        System.out.println("");
    }

    public void ejecutarLlamada(SimpleParser.LlamadaContext ctx) {
        String moduleName = "";

        System.out.println("Ejecutando llamada al modulo:");

        if (ctx.tipo() != null) {
            moduleName = ctx.ID(1).getText();
        } else {
            moduleName = ctx.ID(0).getText();
        }

        System.out.println("Nombre del modulo llamado: " + moduleName);

        if (!globalTable.moduleExists(moduleName)) {
            System.err.println("ERROR: El modulo " + moduleName + " no esta definido.");
            System.exit(0);
        }

        Module moduloALlamar = globalTable.lookupModule(moduleName);

        List<Parametro> parametros = moduloALlamar.getParametros();
        List<Parametro> argumentos = procesarArgumentos(ctx.argumentoLl());

        if (parametros.size() != argumentos.size()) {
            System.err.println("ERROR: La cantidad de parametros y argumentos es distinta.");
            System.exit(0);
        }

        for (int i = 0; i < moduloALlamar.getParametros().size(); i++) {
            Parametro parametro = parametros.get(i);
            Parametro argumento = argumentos.get(i);
            if (parametro.getType().toString().equals(argumento.getType().toString())) {
                parametro.setValue(argumento.getValue());
            } else {
                System.err.println("ERROR: No coinciden los tipos entre parametros y argumentos.");
                System.exit(0);
            }
        }
        currentModuleTable = new SymbolTable();
        for (int i = 0; i < moduloALlamar.getParametros().size(); i++) {
            Parametro parametro = parametros.get(i);
            if (currentModuleTable.variableExists(parametro.getName())) {
                Variable variableExistente = currentModuleTable.lookupVariable(parametro.getName());
                if (parametro.getType() != "") {
                    System.err.println("\nError: La variable " + parametro.getName() + " ya esta definida.");
                    System.exit(0);
                }
    
                if (!currentModuleTable.isValueTypeCompatible(variableExistente.getType(), parametro.getValue())) {
                    System.err
                            .println("\nError: La variable " + parametro.getName() + " es de tipo " + variableExistente.getType());
                    System.exit(0);
                }
                currentModuleTable.updateVariableValue(parametro.getName(), parametro.getValue());
    
            } else {
                currentModuleTable.defineVariable(parametro.getName(), parametro.getType(), parametro.getValue());
            }
        }
        ejecutarModulo(moduleName);
        this.currentModuleTable = null;
    }

    public void ejecutarModulo(String moduleName) {
        if (!globalTable.moduleExists(moduleName)) {
            System.err.println("ERROR: El modulo " + moduleName + " no esta definido.");
            System.exit(0);
        }

        Module modulo = globalTable.lookupModule(moduleName);

        ejecutarCuerpo(modulo.getContext().cuerpo());
    }

    public void ejecutarModuloInicio(SimpleParser.ModuloInicioContext ctx) {
        System.out.println("Definiendo moduloInicio:");
        String nombreModulo = ctx.ID().getText();
        String variableRetorno = ctx.nullOrNombre().getText();
        System.out.println("Nombre de modulo: " + nombreModulo);
        System.out.println("Variable de retorno: " + variableRetorno);
        if (globalTable.moduleExists(nombreModulo)) {
            System.err.println("ERROR: El modulo " + nombreModulo + " ya esta definido.");
            System.exit(0);
        }

        globalTable.defineModule(nombreModulo, variableRetorno, null, ctx);
        currentModuleTable = new SymbolTable();

        SimpleParser.ParametroContext parametro = ctx.parametro();
        guardarParametro(parametro, nombreModulo);

        this.currentModuleTable = null;
    }

}
