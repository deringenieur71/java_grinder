/**
 *  Java Grinder
 *  Author: Michael Kohn
 *   Email: mike@mikekohn.net
 *     Web: http://www.mikekohn.net/
 * License: GPLv3
 *
 * Copyright 2014-2018 by Michael Kohn
 *
 */

#ifndef JAVA_GRINDER_API_AMIGA_API_H
#define JAVA_GRINDER_API_AMIGA_API_H

#include "common/JavaClass.h"
#include "generator/Generator.h"

int amiga(JavaClass *java_class, Generator *generator, const char *method_name);
int amiga(JavaClass *java_class, Generator *generator, const char *method_name, int const_val);
int amiga(JavaClass *java_class, Generator *generator, const char *method_name, int const_val1, int const_val2);

#endif

