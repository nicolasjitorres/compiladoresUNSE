import java.util.ArrayList;
import java.util.List;

public class Module {
    private String name; // Nombre del módulo
    private String typeReturn; // Tipo de retorno del módulo
    private String valueReturn; // Valor de retorno del módulo
    private List<Parametro> parametros;

    // Constructor
    public Module(String name, String typeReturn, String valueReturn) {
        this.name = name;
        this.typeReturn = typeReturn;
        this.valueReturn = valueReturn;
        this.parametros = new ArrayList<>();
    }

    public void addParametro(String type, String name, String value) {
        Parametro parametro = new Parametro(type, name, value);
        parametros.add(parametro);
    }

    // Obtener la lista de parámetros
    public List<Parametro> getParametros() {
        return parametros;
    }

    // Métodos getter
    public String getNombre() {
        return name;
    }

    public String getTypeReturn() {
        return typeReturn;
    }

    public String getValueReturn() {
        return valueReturn;
    }

    // Métodos setter
    public void setNombre(String name) {
        this.name = name;
    }

    public void setTypeReturn(String typeReturn) {
        this.typeReturn = typeReturn;
    }

    public void setValueReturn(String valueReturn) {
        this.valueReturn = valueReturn;
    }

    // Método para mostrar información del módulo
    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", typeReturn='" + typeReturn + '\'' +
                ", valueReturn='" + valueReturn + '\'' +
                '}';
    }
}