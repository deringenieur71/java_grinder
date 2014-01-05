/**
 *  Java Grinder
 *  Author: Michael Kohn
 *   Email: mike@mikekohn.net
 *     Web: http://www.mikekohn.net/
 * License: GPL
 *
 * Copyright 2014 by Michael Kohn
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "JavaClass.h"
#include "compile.h"
#include "Generator.h"
#include "ARM.h"
#include "DSPIC.h"
#include "M6502.h"
#include "MSP430.h"
#include "MSP430X.h"

#define STACK_LEN 65536

int main(int argc, char *argv[])
{
FILE *in;
Generator *generator;
JavaClass *java_class;
int index;

  if (argc != 4)
  {
    printf("Usage: %s <class> <outfile> <dspic/msp430/m6502/arm>\n", argv[0]);
    exit(0);
  }

  in = fopen(argv[1],"rb");
  if (in == NULL)
  {
    printf("Cannot open classfile %s\n", argv[1]);
    exit(1);
  }

  if (strcasecmp("msp430",argv[3]) == 0)
  {
    generator = new MSP430();
  }
    else
  if (strcasecmp("msp430x",argv[3]) == 0)
  {
    generator = new MSP430X();
  }
    else
  if (strcasecmp("dspic",argv[3]) == 0)
  {
    generator = new DSPIC();
  }
    else
  if (strcasecmp("m6502",argv[3]) == 0)
  {
    generator = new M6502();
  }
    else
  if (strcasecmp("arm",argv[3]) == 0)
  {
    generator = new ARM();
  }
    else
  {
    printf("Unknown cpu type: %s\n", argv[3]);
    exit(1);
  }

  if (generator->open(argv[2]) == -1)
  {
    exit(1);
  }

  java_class = new JavaClass(in);
#ifdef DEBUG
  java_class->print();
#endif

  for (index = 0; index < java_class->methods_count; index++)
  {
    compile_method(java_class, index, generator);
  }

  delete generator;

  fclose(in);

  return 0;
}

