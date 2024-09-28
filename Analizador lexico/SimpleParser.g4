parser grammar SimpleParser;

options {
    tokenVocab = SimpleLexer;
}

// Regla inicial
inicio: (moduloInicio | cuerpo | COMENTARIO inicio)* EOF;

// Definición del módulo de inicio
moduloInicio: MODULE ID LPAREN (parametro)? RPAREN COLON cuerpo RETURN nullOrNombre SEMICOLON inicio;

// Opcionalmente nulo o un identificador
nullOrNombre: NULL | ID;

// Parámetros y lista de parámetros
parametro: tipo ID (parametroRestante)?; // Permitir al menos un parámetro
parametroRestante: COMMA tipo ID (parametroRestante)?; // Uso de recursión para parámetros

// Cuerpo de una estructura
cuerpo: (accion cuerpo1)* RETURN nullOrNombre SEMICOLON; // Asegurar que el cuerpo termine con RETURN
cuerpo1: (COMENTARIO cuerpo1 | cuerpo)*; // Permitir comentarios o más cuerpos

// Definición de las acciones posibles
accion: si | mientras | variable | imprimir | llamada;

// Estructura condicional IF
si: IF LPAREN condicion RPAREN COLON cuerpo (ELSE cuerpo)? ENDIF SEMICOLON;

// Estructura WHILE
mientras: WHILE LPAREN condicion RPAREN COLON cuerpo ENDWHILE SEMICOLON;

// Definición de variables
variable: tipo ID (OPERADORASIGNACION operacion)? SEMICOLON; // Ajustar para incluir asignación opcional
tipo: tipodato | tipodatonum;
tipodato: STRING | BOOLEAN;
tipodatonum: INT | FLOAT;

// Operaciones aritméticas
operacion: termino (operacion1)?; // Asegurarse de que haya un término inicial
operacion1: OPTERCERNIVEL termino (operacion1)?; // Asegurar la correcta encadenación
termino: factor (termino1)?; // Incluir la lógica de términos
termino1: OPSEGUNDONIVEL factor (termino1)?; // Ajustar
factor: LPAREN operacion RPAREN | ID | NUMERO | factor OPPRIMERNIVEL factor;

// Sentencia de impresión
imprimir: PRINT LPAREN imprimir1 RPAREN SEMICOLON; // Mantener la definición de imprimir
imprimir1: (CADENA imprimir2 | ID imprimir2); // Permitir una cadena o ID
imprimir2: (COMMA imprimir1)*; // Permitir múltiples impresiones

// Llamada a un módulo
llamada: tipo ID OPERADORASIGNACION CALL MODULE ID LPAREN argumentoLl RPAREN SEMICOLON | CALL MODULE ID LPAREN argumentoLl RPAREN SEMICOLON;

// Argumentos de llamada
argumentoLl: (argumento (COMMA argumento)*)?; // Permitir múltiples argumentos
argumento: ID | operacion | CADENA;

// Condiciones lógicas
condicion: condicion1 (OPERADORLOGICO condicion)?; // Permitir condiciones anidadas
condicion1: terminoLogico (OPERADORCOMPARACION terminoLogico)?; // Asegurar comparaciones
terminoLogico: operacion | LPAREN condicion RPAREN | CADENA; // Ajustar según sea necesario