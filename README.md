#Gomoku#
Implementação em Java do jogo de tabuleiro Gomoku. [Wikipédia](http://pt.wikipedia.org/wiki/Gomoku)

A comunicação entre os jogadores se dá através de portas seriais.

Para realizar os testes em uma mesma máquina, pode ser usado o [Free Virtual Serial Ports Emulator](http://www.eterlogic.com/Products.VSPE.html).

##Exportar projeto no Windows##

* Com o projeto devidamente configurado no Eclipse, vá em "File" > "Export";
* Na caixa de diálogo que surgir, selecione a opção "Java" > "Runnable JAR file", clicando logo após em "Next";
* No campo "Export destination", clique em "Browse". Crie um diretório para a exportação do arquivo JAR e salve-o lá, com o nome "Gomoku.jar";
* No campo "Library handling", selecione a opção "Package required libraries into generated JAR";
* Clique em "Finish" e confirme os próximos alertas que surgirem;
* Copie o diretório "dll" do projeto para o mesmo diretório onde foi salvo o arquivo JAR;
* No diretório onde o JAR foi exportado, crie um arquivo de texto com o nome "Gomoku.vbs", e com o seguinte conteúdo:

```
#!vbs

CreateObject("Wscript.Shell").Run "java -Djava.library.path=dll -jar Gomoku.jar", 0, False
```

* Execute o arquivo "Gomoku.vbs" para rodar a aplicação.