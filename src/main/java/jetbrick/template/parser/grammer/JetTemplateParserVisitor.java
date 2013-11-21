// Generated from JetTemplateParser.g4 by ANTLR 4.1

package jetbrick.template.parser.grammer;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JetTemplateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JetTemplateParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_list(@NotNull JetTemplateParser.Expression_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#invalid_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvalid_directive(@NotNull JetTemplateParser.Invalid_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#else_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_directive(@NotNull JetTemplateParser.Else_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(@NotNull JetTemplateParser.TypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_hash_map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_hash_map(@NotNull JetTemplateParser.Expr_hash_mapContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#continue_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_directive(@NotNull JetTemplateParser.Continue_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_compare_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_condition(@NotNull JetTemplateParser.Expr_compare_conditionContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_field_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_field_access(@NotNull JetTemplateParser.Expr_field_accessContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_instanceof}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_instanceof(@NotNull JetTemplateParser.Expr_instanceofContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_function_call(@NotNull JetTemplateParser.Expr_function_callContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_compare_equality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_equality(@NotNull JetTemplateParser.Expr_compare_equalityContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#type_array_suffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_array_suffix(@NotNull JetTemplateParser.Type_array_suffixContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull JetTemplateParser.ValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_math_binary_bitwise}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_bitwise(@NotNull JetTemplateParser.Expr_math_binary_bitwiseContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#hash_map_entry_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_map_entry_list(@NotNull JetTemplateParser.Hash_map_entry_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirective(@NotNull JetTemplateParser.DirectiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_compare_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_not(@NotNull JetTemplateParser.Expr_compare_notContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(@NotNull JetTemplateParser.TemplateContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(@NotNull JetTemplateParser.TextContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_identifier(@NotNull JetTemplateParser.Expr_identifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#if_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_directive(@NotNull JetTemplateParser.If_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_math_unary_suffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_unary_suffix(@NotNull JetTemplateParser.Expr_math_unary_suffixContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_new_array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_new_array(@NotNull JetTemplateParser.Expr_new_arrayContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#type_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_name(@NotNull JetTemplateParser.Type_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#type_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_list(@NotNull JetTemplateParser.Type_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#include_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude_directive(@NotNull JetTemplateParser.Include_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull JetTemplateParser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_array_get}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_array_get(@NotNull JetTemplateParser.Expr_array_getContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_compare_relational}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_compare_relational(@NotNull JetTemplateParser.Expr_compare_relationalContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#type_arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_arguments(@NotNull JetTemplateParser.Type_argumentsContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#define_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefine_directive(@NotNull JetTemplateParser.Define_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_class_cast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_class_cast(@NotNull JetTemplateParser.Expr_class_castContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_math_binary_basic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_basic(@NotNull JetTemplateParser.Expr_math_binary_basicContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#set_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_expression(@NotNull JetTemplateParser.Set_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#tag_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag_directive(@NotNull JetTemplateParser.Tag_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#set_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_directive(@NotNull JetTemplateParser.Set_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_new_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_new_object(@NotNull JetTemplateParser.Expr_new_objectContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(@NotNull JetTemplateParser.ConstantContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#put_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPut_directive(@NotNull JetTemplateParser.Put_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_array_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_array_list(@NotNull JetTemplateParser.Expr_array_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_conditional_ternary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_conditional_ternary(@NotNull JetTemplateParser.Expr_conditional_ternaryContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_method_invocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_method_invocation(@NotNull JetTemplateParser.Expr_method_invocationContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#for_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_expression(@NotNull JetTemplateParser.For_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#define_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefine_expression(@NotNull JetTemplateParser.Define_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_math_binary_shift}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_binary_shift(@NotNull JetTemplateParser.Expr_math_binary_shiftContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#stop_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStop_directive(@NotNull JetTemplateParser.Stop_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#break_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_directive(@NotNull JetTemplateParser.Break_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_math_unary_prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_math_unary_prefix(@NotNull JetTemplateParser.Expr_math_unary_prefixContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#for_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_directive(@NotNull JetTemplateParser.For_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#elseif_directive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseif_directive(@NotNull JetTemplateParser.Elseif_directiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_group(@NotNull JetTemplateParser.Expr_groupContext ctx);

	/**
	 * Visit a parse tree produced by {@link JetTemplateParser#expr_constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_constant(@NotNull JetTemplateParser.Expr_constantContext ctx);
}