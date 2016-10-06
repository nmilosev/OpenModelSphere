#!/bin/bash

# Modelsphere startup script
# Oct 6 2016
# nmilosev@dmi.uns.ac.rs

# cleanup DSL files
rm file\:/ -rf

path="./resources.zip ./drivers/mysql-connector-java-5.1.40-bin.jar ./plugins/org.modelsphere.plugins.getdomainvalues.jar ./plugins/org.modelsphere.sms.plugins.integrity.jar ./plugins/bin/repository_functions.jar ./plugins/org.modelsphere.plugins.open_r2s.jar ./plugins/com.neosapiens.plugins.reverse.src.java.jar ./plugins/org.modelsphere.plugins.diagram.tree.jar ./plugins/org.modelsphere.plugins.diagram.statistics.jar ./plugins/org.modelsphere.plugins.ansi_forward.jar ./plugins/org.modelsphere.plugins.validation.jar ./plugins/org.modelsphere.plugins.propagatedomainvalues.jar ./plugins/org.modelsphere.plugins.ansi_forward_toolkit.jar ./plugins/com.neosapiens.plugins.layout.jar ./plugins/org.modelsphere.plugins.html.browser.jar ./plugins/com.neosapiens.plugins.diagramming.stretch.jar ./plugins/org.modelsphere.plugins.java.genmeta.jar ./plugins/com.neosapiens.plugins.diagramming.neighbors.jar ./plugins/com.neosapiens.plugins.reverse.java.jar ./plugins/org_modelsphere_sms_plugins_sqlshell.jar ./plugins/com.neosapiens.plugins.codegen.jar ./lib/velocity-1.6.1/lib/commons-collections-3.2.1.jar ./lib/velocity-1.6.1/lib/commons-lang-2.4.jar ./lib/velocity-1.6.1/velocity-1.6.1.jar ./lib/jgrapht-all/jgrapht-all.jar ./lib/lablib-checkboxtree-3.0.2.jar ./lib/jazzy-core/jazzy-core.jar ./lib/jakarta-regexp-1.5/jakarta-regexp-1.5.jar ./lib/jython-2.2.1/jython.jar ./lib/antlr-3.3-complete.jar ./modelsphere.jar"

libs=".:./resources:./targets"

for x in $path
do
  if [ ! -f $x ]
  then
     echo "Required file not found [$x]... Aborting startup..."
     exit 1
  else
     libs="$libs:$x"
  fi
done


type java > /dev/null 2>&1 || { echo >&2 "I require JRE (java >= 1.6.0) in \$PATH but it's not installed. Aborting."; exit 1; }

java -ms64m -mx512m -ss16m -classpath "$libs" org.modelsphere.sms.Application
