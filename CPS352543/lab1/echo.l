/* definitions, e.g., patterns, include libraries */
%%
	/* set of pattern-action rules 
		RegularExpression { Action}
		(this comment must be indented)*/
%%
/* called by lex when EOF reached */
int yywrap (void) {
   /* convention is to return 1 */
   return 1;
}

int main (void) {
   /* main entry point for lex */
   yylex();
   return 0;
}
