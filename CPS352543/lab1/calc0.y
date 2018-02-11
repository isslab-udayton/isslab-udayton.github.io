/* definitions, e.g., included libraries, variables, tokens...  */
%{
    #include <stdio.h>
    void yyerror(char *);
    int yylex(void);
%}

%token NUMBER 
%token PLUS MINUS

%%

program:
        program expr '\n'         { printf("Valid syntax\n"); }
        | 
        ;

expr:
        NUMBER
        | expr PLUS expr           {  }
        | expr MINUS expr          {  }
        ;

%%
/* default functions */
void yyerror(char *s) {
    fprintf(stderr, "%s\n", s);
}

int main(void) {
    yyparse();
    return 0;
}