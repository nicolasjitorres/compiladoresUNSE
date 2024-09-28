lexer grammar SimpleLexer;

// Símbolos
LPAREN   : '(' ;
RPAREN   : ')' ;
COLON    : ':' ;
SEMICOLON: ';' ;
COMMA: ',' ;

// Operadores Aritméticos
OPPRIMERNIVEL: '^' ;
OPSEGUNDONIVEL: '%' | '×' | '/' ;
OPTERCERNIVEL: '+' | '-' ;

// Operadores de Asignacion
OPERADORASIGNACION: '=';

// Operadores de Comparación
OPERADORCOMPARACION: '==' | '!=' | '>' | '<' | '>=' | '<=' ;

// Operadores Lógicos
OPERADORLOGICO: 'AND' | 'OR' ;

// Booleanos
BOOLEANO: 'TRUE' | 'FALSE' ;

// Comentarios (estos los debería de omitir)
COMENTARIO: '/*' .*? '*/' -> skip ;

// Tipos de dato
BOOLEAN: 'BOOLEAN';
STRING: 'STRING';
// Tipos de dato numéricos
INT: 'INT';
FLOAT: 'FLOAT';
// Palabras reservadas
MODULE   : 'MODULE' ;
NULL     : 'NULL' ;
RETURN   : 'RETURN' ;
CALL     : 'CALL' ;
PRINT    : 'PRINT' ;
IF       : 'IF' ;
ELSE     : 'ELSE' ;
ENDIF    : 'ENDIF' ;
WHILE    : 'WHILE' ;
ENDWHILE : 'ENDWHILE' ;


// String
CADENA: '"' ( ~["\\] | '\\' . )* '"' ;

// Identificador
ID: [a-zA-Z][a-zA-Z0-9]* ;

// Número (incluye floats con coma)
NUMERO:  [+-]?([0-9]+)([,][0-9]+)? ;

// Espacios en blanco y nuevas líneas
WS: [ \t\r\n] -> skip ;
