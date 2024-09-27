lexer grammar SimpleLexer;

// String
CADENA: '"' [^"\r\n]* '"';  

// Número (incluye floats con coma)
NUMERO: [+-]?([1-9][0-9]*|[0])([,][0-9]+)?;  

// Simbolos
LPAREN   : '(' ;
RPAREN   : ')' ;
COLON    : ':' ;
SEMICOLON: ';' ;

// Operadores Aritméticos
OPPRIMERNIVEL: '^';  
OPSEGUNDONIVEL: '%' | '×' | '/'; 
OPTERCERNIVEL: '+' | '-'; 

// Operadores de Comparación
OPERADORCOMPARACION: '==' | '!=' | '>' | '<' | '>=' | '<=';  

// Operadores Lógicos
OPERADORLOGICO: 'AND' | 'OR';  

// Booleanos
BOOLEANO: 'TRUE' | 'FALSE';  

// Comentarios (estos los deberia de omitir)
COMENTARIO: '/*' .*? '*/' -> skip;  

// Tipos de dato
TIPODATO: 'BOOLEAN' | 'STRING';  

// Tipos de dato numéricos
TIPODATONUM: 'INT' | 'FLOAT';  

// Palabras reservadas
PALCLAVE: 'RETURN' | 'MODULE' | 'NULL' | 'IF' | 'ELSE' | 'ENDIF' | 'WHILE' | 'ENDWHILE' | 'CALL MODULE';  

// Identificador
ID: [a-zA-Z][a-zA-Z0-9]*;  

// Espacios en blanco y nuevas líneas tiene que ignorar 
WS: [ \t\r\n] -> skip;  
