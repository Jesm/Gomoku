#Gomoku#
Implementação em Java do jogo de tabuleiro Gomoku. [Wikipédia](http://pt.wikipedia.org/wiki/Gomoku)

A comunicação entre os jogadores se dá através de portas seriais.

Para realizar os testes em uma mesma máquina, pode ser usado o [Free Virtual Serial Ports Emulator](http://www.eterlogic.com/Products.VSPE.html).

##Exportar projeto no Windows##

* Com o projeto devidamente configurado no Eclipse, vá em "File" > "Export";
* Na caixa de diálogo que surgir, selecione a opção "Java" > "Runnable JAR file", clicando logo após em "Next";
* No campo "Export destination", selecione o local de exportação do arquivo JAR;
* No campo "Library handling", selecione a opção "Package required libraries into generated JAR";
* Clique em "Finish" e confirme os próximos alertas que surgirem;
* Copie o conteúdo do diretório "dll" do projeto para o seu desktop;
* Execute o arquivo JAR para rodar a aplicação.