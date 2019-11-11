REL_NAME = relazione
all:
	pandoc --self-contained $(REL_NAME).md -o $(NAME).tex
	pandoc  --pdf-engine=xelatex $(REL_NAME).tex -o $(NAME).pdf

