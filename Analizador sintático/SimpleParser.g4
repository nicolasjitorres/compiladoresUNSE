parser grammar SimpleParser;

// Esto le indica al parser que use los tokens del lexer
options { tokenVocab=SimpleLexer; }  

// REGLAS SINTACTICAS
inicio: cuerpo | moduloInicio | COMENTARIO inicio | EOF;

moduloInicio: MODULE ID LPAREN parametro? RPAREN COLON cuerpo RETURN nullOrNombre SEMICOLON inicio;

nullOrNombre: NULL | ID;

parametro: tipo ID parametroRestante?;
parametroRestante: COMMA tipo ID parametroRestante? ;

cuerpo: (accion cuerpoPrima)?;
cuerpoPrima: COMENTARIO cuerpoPrima | cuerpo | ;
accion: si | mientras | variable | imprimir | llamada;

si: IF LPAREN condicion RPAREN COLON cuerpo (ELSE COLON cuerpo)? ENDIF SEMICOLON;

mientras: WHILE LPAREN condicion RPAREN COLON cuerpo ENDWHILE SEMICOLON;

variable: tipo ID | ID OPERADORASIGNACION variablePrima SEMICOLON 
| TIPODATONUM ID OPERADORASIGNACION operacion SEMICOLON | STRING ID OPERADORASIGNACION CADENA SEMICOLON 
| BOOLEAN ID OPERADORASIGNACION BOOLEANO SEMICOLON;

variablePrima: operacion | CADENA | BOOLEANO;

tipo: STRING | BOOLEAN | TIPODATONUM;

operacion: termino operacionRec?;
operacionRec: OPTERCERNIVEL termino operacionRec?;
termino: factor terminoRec?;
terminoRec: OPSEGUNDONIVEL factor terminoRec?;
factor: LPAREN operacion RPAREN | ID | NUMERO | factor OPPRIMERNIVEL factor;

imprimir: PRINT LPAREN imprimirRec RPAREN SEMICOLON;
imprimirRec: CADENA imprimirCont? | ID imprimirCont?;
imprimirCont: COMMA imprimirRec?;

llamada: (tipo ID OPERADORASIGNACION)? CALL MODULE ID LPAREN argumentoLl? RPAREN SEMICOLON;

argumentoLl: argumento (COMMA argumentoLl)?;
argumento: ID | operacion | CADENA;

condicion: condicionRec (OPERADORLOGICO condicionRec)*; // Permite múltiples condiciones unidas por AND/OR
condicionRec: LPAREN condicion RPAREN 
             | terminoLogico OPERADORCOMPARACION terminoLogico; // Permite la comparación y también condiciones anidadas
terminoLogico: operacion | CADENA; // Añade CADENA si es necesario