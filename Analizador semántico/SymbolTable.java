import java.util.HashMap;
import java.util.Map;

class SymbolTable {
    private Map<String, Variable> variables; // Nombre de variable y objeto Variable
    private Map<String, String> modules; // Nombre de módulos y tipo de retorno

    public SymbolTable() {
        variables = new HashMap<>();
        modules = new HashMap<>();
    }

    public void defineVariable(String name, String type, String value) {
        Variable variable = new Variable(name, type, value);
        variables.put(name, variable);
    }

    public void defineModule(String name, String returnType) {
        modules.put(name, returnType);
    }

    public Variable lookupVariable(String name) {
        return variables.get(name);
    }

    public String lookupModule(String name) {
        return modules.get(name);
    }

    // Método para verificar si una variable ya está definida
    public boolean variableExists(String name) {
        return variables.containsKey(name);
    }

    // Método para actualizar el valor de una variable existente
    public void updateVariableValue(String name, String newValue) {
        if (variableExists(name)) {
            Variable variable = lookupVariable(name);
            variable.setValue(newValue);
        } else {
            System.err.println("Error: La variable " + name + " no está definida.");
        }
    }

    public String lookupVariableValue(String name) {
        Variable variable = lookupVariable(name);
        if (variable != null) {
            return variable.getValue(); // Devolver solo el valor de la variable
        } else {
            System.err.println("Error: La variable " + name + " no está definida.");
            return null; // O un valor por defecto
        }
    }
}
