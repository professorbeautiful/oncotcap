#define BASEMODEL

#include <stdio.h>
#include <malloc.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>

#include "classes.h"

extern int is_a_substring( );
extern char *read_word( );
extern char *read_line( );
extern void eprint( );
extern void skip_blanks( );

char *read_rule_file( );
void write_rule_file( );
void clear_base_model( );
int parse_base_model( );
void display_summary( );
void evaluate_rule( );
void update_growth_rates( );
void change_mutation_rates( );
void change_kill_rates( );
void phenotype_selection( );
void flush_line();
int is_assignment( );
int update_cell_types( );

