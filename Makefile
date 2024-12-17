JAVA=java
C=gcc
ESXTENSION=exe


# Java:
DIR_OUTPUT=out
DIR_OUTPUT_CLASS=$(DIR_OUTPUT)/production/ServerGUI
DIR_SRC=src


J_CLASSPATH=-classpath ./$(DIR_OUTPUT_CLASS) Core
J_FLAGS=-Dsun.java2d.uiScale=1.0 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 $(J_CLASSPATH)
#-Dsun.java2d.uiScale=1.0 se añadio en java9

# C:
DIR_LIB_CSGUI=$(DIR_SRC)/CSGUI
DIR_LIB_CSGUI_SRC=$(DIR_LIB_CSGUI)/src
DIR_LIB_CSGUI_INCLUDE=$(DIR_LIB_CSGUI)/include
DIR_BIN=$(DIR_OUTPUT)


CFLAGS=-O3 -I$(DIR_LIB_CSGUI_INCLUDE)
CFLAGS_OBLECT=$(CFLAGS) -c
LD_FLAGS=


CLEAR_COMMAND=rm -rf
CLEAR_DIR=rmdir /s /q

run: ClientHandler.class Core.class
	$(JAVA) $(J_FLAGS)

c: code.$(ESXTENSION) cli.$(ESXTENSION)
	$(MAKE) code.$(ESXTENSION)

code.$(ESXTENSION): $(DIR_LIB_CSGUI)/code.c $(DIR_BIN)/csgui.o
	$(C) $(CFLAGS) $^ -o $@ $(LD_FLAGS)

cli.$(ESXTENSION): $(DIR_LIB_CSGUI)/cli.c $(DIR_BIN)/csgui.o
	$(C) $(CFLAGS) $^ -o $@ $(LD_FLAGS)

$(DIR_BIN)/csgui.o: $(DIR_LIB_CSGUI_SRC)/csgui.c
	$(C) $(CFLAGS_OBLECT) $^ -o $@

Core.class: Core.java
	javac -d $(DIR_OUTPUT_CLASS) $^

ClientHandler.class: $(DIR_SRC)/ClientHandler.java
	javac -d $(DIR_OUTPUT_CLASS) $^

install_jdk_linux:
	sudo apt install openjdk-21-jdk

# eliminar los .class generados con una JVM de otra version.
clear_class:
	$(CLEAR_COMMAND) $(DIR_OUTPUT_CLASS)/$(DIR_SRC)/*.class

clear:
	$(CLEAR_COMMAND) code.$(ESXTENSION) cli.$(ESXTENSION) "$(DIR_BIN)\*.o"  "$(DIR_BIN)\*$(ESXTENSION)"