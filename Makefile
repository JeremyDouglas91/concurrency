# Tree Sum / Parallel Assignment 
# Jeremy du Plessis
# September 2018

JC = /usr/bin/javac
JFLAGS = -g -d

.SUFFIXES = .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JFLAGS) $(BINDIR) -cp $(BINDIR) $<

CLASSES = constants.class mutableBoolean.class YearCount.class \
	  Tree.class block.class Land.class SunData.class ForestPanel.class \
	  SumArray.class simThread.class treeSimulator.class
		 	
		
CLASS_FILES = $(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

run_main:
	java -cp $(BINDIR) treeSimulator $() $(input)

clean:
	rm $(BINDIR)/*.class
