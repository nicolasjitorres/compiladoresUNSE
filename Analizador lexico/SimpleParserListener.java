// Generated from SimpleParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SimpleParser}.
 */
public interface SimpleParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SimpleParser#inicio}.
	 * @param ctx the parse tree
	 */
	void enterInicio(SimpleParser.InicioContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#inicio}.
	 * @param ctx the parse tree
	 */
	void exitInicio(SimpleParser.InicioContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#moduloInicio}.
	 * @param ctx the parse tree
	 */
	void enterModuloInicio(SimpleParser.ModuloInicioContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#moduloInicio}.
	 * @param ctx the parse tree
	 */
	void exitModuloInicio(SimpleParser.ModuloInicioContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#nullOrNombre}.
	 * @param ctx the parse tree
	 */
	void enterNullOrNombre(SimpleParser.NullOrNombreContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#nullOrNombre}.
	 * @param ctx the parse tree
	 */
	void exitNullOrNombre(SimpleParser.NullOrNombreContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#parametro}.
	 * @param ctx the parse tree
	 */
	void enterParametro(SimpleParser.ParametroContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#parametro}.
	 * @param ctx the parse tree
	 */
	void exitParametro(SimpleParser.ParametroContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#parametroRestante}.
	 * @param ctx the parse tree
	 */
	void enterParametroRestante(SimpleParser.ParametroRestanteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#parametroRestante}.
	 * @param ctx the parse tree
	 */
	void exitParametroRestante(SimpleParser.ParametroRestanteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#cuerpo}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo(SimpleParser.CuerpoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#cuerpo}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo(SimpleParser.CuerpoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#cuerpo1}.
	 * @param ctx the parse tree
	 */
	void enterCuerpo1(SimpleParser.Cuerpo1Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#cuerpo1}.
	 * @param ctx the parse tree
	 */
	void exitCuerpo1(SimpleParser.Cuerpo1Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#accion}.
	 * @param ctx the parse tree
	 */
	void enterAccion(SimpleParser.AccionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#accion}.
	 * @param ctx the parse tree
	 */
	void exitAccion(SimpleParser.AccionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#si}.
	 * @param ctx the parse tree
	 */
	void enterSi(SimpleParser.SiContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#si}.
	 * @param ctx the parse tree
	 */
	void exitSi(SimpleParser.SiContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#mientras}.
	 * @param ctx the parse tree
	 */
	void enterMientras(SimpleParser.MientrasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#mientras}.
	 * @param ctx the parse tree
	 */
	void exitMientras(SimpleParser.MientrasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(SimpleParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(SimpleParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#tipo}.
	 * @param ctx the parse tree
	 */
	void enterTipo(SimpleParser.TipoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#tipo}.
	 * @param ctx the parse tree
	 */
	void exitTipo(SimpleParser.TipoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#tipodato}.
	 * @param ctx the parse tree
	 */
	void enterTipodato(SimpleParser.TipodatoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#tipodato}.
	 * @param ctx the parse tree
	 */
	void exitTipodato(SimpleParser.TipodatoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#tipodatonum}.
	 * @param ctx the parse tree
	 */
	void enterTipodatonum(SimpleParser.TipodatonumContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#tipodatonum}.
	 * @param ctx the parse tree
	 */
	void exitTipodatonum(SimpleParser.TipodatonumContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#operacion}.
	 * @param ctx the parse tree
	 */
	void enterOperacion(SimpleParser.OperacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#operacion}.
	 * @param ctx the parse tree
	 */
	void exitOperacion(SimpleParser.OperacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#operacion1}.
	 * @param ctx the parse tree
	 */
	void enterOperacion1(SimpleParser.Operacion1Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#operacion1}.
	 * @param ctx the parse tree
	 */
	void exitOperacion1(SimpleParser.Operacion1Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#termino}.
	 * @param ctx the parse tree
	 */
	void enterTermino(SimpleParser.TerminoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#termino}.
	 * @param ctx the parse tree
	 */
	void exitTermino(SimpleParser.TerminoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#termino1}.
	 * @param ctx the parse tree
	 */
	void enterTermino1(SimpleParser.Termino1Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#termino1}.
	 * @param ctx the parse tree
	 */
	void exitTermino1(SimpleParser.Termino1Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(SimpleParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(SimpleParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#imprimir}.
	 * @param ctx the parse tree
	 */
	void enterImprimir(SimpleParser.ImprimirContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#imprimir}.
	 * @param ctx the parse tree
	 */
	void exitImprimir(SimpleParser.ImprimirContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#imprimir1}.
	 * @param ctx the parse tree
	 */
	void enterImprimir1(SimpleParser.Imprimir1Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#imprimir1}.
	 * @param ctx the parse tree
	 */
	void exitImprimir1(SimpleParser.Imprimir1Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#imprimir2}.
	 * @param ctx the parse tree
	 */
	void enterImprimir2(SimpleParser.Imprimir2Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#imprimir2}.
	 * @param ctx the parse tree
	 */
	void exitImprimir2(SimpleParser.Imprimir2Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#llamada}.
	 * @param ctx the parse tree
	 */
	void enterLlamada(SimpleParser.LlamadaContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#llamada}.
	 * @param ctx the parse tree
	 */
	void exitLlamada(SimpleParser.LlamadaContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#argumentoLl}.
	 * @param ctx the parse tree
	 */
	void enterArgumentoLl(SimpleParser.ArgumentoLlContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#argumentoLl}.
	 * @param ctx the parse tree
	 */
	void exitArgumentoLl(SimpleParser.ArgumentoLlContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#argumento}.
	 * @param ctx the parse tree
	 */
	void enterArgumento(SimpleParser.ArgumentoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#argumento}.
	 * @param ctx the parse tree
	 */
	void exitArgumento(SimpleParser.ArgumentoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#condicion}.
	 * @param ctx the parse tree
	 */
	void enterCondicion(SimpleParser.CondicionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#condicion}.
	 * @param ctx the parse tree
	 */
	void exitCondicion(SimpleParser.CondicionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#condicion1}.
	 * @param ctx the parse tree
	 */
	void enterCondicion1(SimpleParser.Condicion1Context ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#condicion1}.
	 * @param ctx the parse tree
	 */
	void exitCondicion1(SimpleParser.Condicion1Context ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#terminoLogico}.
	 * @param ctx the parse tree
	 */
	void enterTerminoLogico(SimpleParser.TerminoLogicoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#terminoLogico}.
	 * @param ctx the parse tree
	 */
	void exitTerminoLogico(SimpleParser.TerminoLogicoContext ctx);
}