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
            currentModuleTable.updateVariableValue(variableName, variableValue.replaceAll("\"", ""));

        } else {
            // Definir la variable en la tabla de símbolos actual
            currentModuleTable.defineVariable(variableName, variableType, variableValue.replaceAll("\"", ""));
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
    //     // Este método se llama al entrar en una condición
    //     System.out.println("Condicion detectada: " + ctx.getText());

    //     // Para verificar las variables en la condición
    //     for (SimpleParser.CondicionRecContext condicionRec : ctx.condicionRec()) {
    //         // Acceder a los términos lógicos en cada condicionRec
    //         if (condicionRec.terminoLogico().size() > 0) {
    //             for (SimpleParser.TerminoLogicoContext term : condicionRec.terminoLogico()) {
    //                 if (term.operacion() != null) {
    //                     String varName = term.operacion().getText();
    //                     if (!globalTable.variableExists(varName) &&
    //                             (currentModuleTable == null || !currentModuleTable.variableExists(varName))) {
    //                         System.err.println("Error: La variable " + varName + " no esta definida en la condicion.");
    //                     }
    //                     System.out.println("Esta es una variable");
    //                 } else {
    //                     System.out.println("Este es un numero " + term.operacion().getText());
    //                 }
    //             }
    //         }
    //     }
    // }

    // @Override
    // public void enterCondicionRec(SimpleParser.CondicionRecContext ctx) {
    //     if (ctx.LPAREN() != null) {
    //         System.out.println("Condicion anidada detectada.");
    //     } else {
    //         // Procesar el término lógico y la comparación
    //         String terminoIzquierdo = ctx.terminoLogico(0).getText(); // Primer término lógico
    //         String operador = ctx.OPERADORCOMPARACION().getText(); // Operador de comparación
    //         String terminoDerecho = ctx.terminoLogico(1).getText(); // Segundo término lógico
    //         System.out.println("Termino logico y comparacion detectados: " + terminoIzquierdo + " " + operador + " "
    //                 + terminoDerecho);
    //     }
    // }

    // @Override
    // public void enterTerminoLogico(SimpleParser.TerminoLogicoContext ctx) {
    //     System.out.println("Término lógico detectado: " + ctx.getText());

    //     // Verificar si es un ID
    //     if (ctx.operacion() != null) {
    //         String varName = ctx.operacion().getText();
    //         if (!globalTable.variableExists(varName) &&
    //                 (currentModuleTable == null || !currentModuleTable.variableExists(varName))) {
    //             System.err.println("Error: La variable " + varName + " no está definida en el término lógico.");
    //         }
    //     }
    //     // Verificar si es una operación
    //     else if (ctx.operacion() != null) {
    //         System.out.println("Este es un resultado de operación: " + ctx.operacion().getText());
    //     }
    //     // Verificar si es una cadena
    //     else if (ctx.CADENA() != null) {
    //         System.out.println("Este es una cadena: " + ctx.CADENA().getText());
    //     }
    // }

    // @Override
    // public void enterModuloInicio(SimpleParser.ModuloInicioContext ctx) {
    //     String moduleName = ctx.ID().getText();
    //     currentModuleTable = new SymbolTable();

    //     // Definir el módulo sin valor de retorno aún
    //     globalTable.defineModule(moduleName, "", "");
    //     System.out.println("Se ha definido un nuevo modulo: " + moduleName);

    //     enterCuerpo(ctx.cuerpo());

    //     System.out.println(currentModuleTable.lookupVariableValue("aux1"));

    //     if (currentModuleTable.variableExists(ctx.nullOrNombre().getText())) {
    //         String returnValue = currentModuleTable.lookupVariableValue(ctx.nullOrNombre().getText());
    //         System.out.println("Valor de retorno del modulo: " + returnValue);
    //     } else {
    //         System.err.println("Error: La variable de retorno no esta definida.");
    //     }
    // }


    // @Override
    // public void enterLlamada(SimpleParser.LlamadaContext ctx) {
    //     Verifica si el módulo llamado ha sido definido
    //     String moduleName = ctx.ID().getText(); // Obtener el nombre del módulo

    //     // Asegúrate de que globalTable esté correctamente inicializado antes de
    //     usarlo
    //     if (globalTable.lookupModule(moduleName) == null) {
    //     System.err.println("Error: El módulo " + moduleName + " no ha sido definido
    //     antes de la llamada.");
    //     } else {
    //     System.out.println("Llamada a módulo detectada: " + moduleName);
    //     }

    //     System.out.println(ctx.toString());
    // }
    @Override
    public void enterCondicion(SimpleParser.CondicionContext ctx) {
        // Este método se llama al entrar en una condición
        if (currentModuleTable==null) {
            currentModuleTable=globalTable;
        }
        // Para verificar las variables en la condición
        for (SimpleParser.CondicionRecContext condicionRec : ctx.condicionRec()) {
            // Acceder a los términos lógicos en cada condicionRec
            if (condicionRec.terminoLogico().size() > 0) {
                for (SimpleParser.TerminoLogicoContext term : condicionRec.terminoLogico()) {
                    if (term.operacion().termino().factor().ID() != null) {
                        String varName = term.operacion().termino().factor().ID().getText();
                        if (!currentModuleTable.variableExists(varName)) {
                            System.err.println("Error: La variable " + varName + " no esta definida.");
                        }else{
                            System.out.println("Esta es una variable: "+varName);
                        }
                    } else {
                        System.out.println("Este es un numero: " + term.operacion().getText());
                    }
                }
            }
        }
    }

    @Override
    public void enterCondicionRec(SimpleParser.CondicionRecContext ctx) {
        if (ctx.LPAREN() != null) {
            System.out.println("Condicion anidada detectada.");
        } else {
            // Procesar el término lógico y la comparación
            String terminoIzquierdo = ctx.terminoLogico(0).getText(); // Primer término lógico
            String operador = ctx.OPERADORCOMPARACION().getText(); // Operador de comparación
            String terminoDerecho = ctx.terminoLogico(1).getText(); // Segundo término lógico
            System.out.println("\nTermino logico y comparacion detectados: " + "\nTermino Izquierdo: "+terminoIzquierdo + "\nOperador: " + operador + "\nTermino Derecho: "+ terminoDerecho);
        }


    }

}
