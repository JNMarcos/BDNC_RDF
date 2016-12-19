
Arquivos contendo as regras que deveram ser convertidas para SPARQL

Todas as regras nos arquivos tem o seguinte formato:

cit_res(A,B),t_next(B,C),t_gpos(C,nn),t_next(C,A),t_orth(A,mixedcase)


cit_res(A,B) : cabeça da regra
t_next(B,C),t_gpos(C,nn),t_next(C,A),t_orth(A,mixedcase) : é o corpo da regra

Se tem que apenas converter o corpo da regra para SPARQL

------------
Exemplo:
------------

  desconsidera o primeiro predicado acima ( a cabeça da regra)  
    --> considera o resto como o corpo

  t_next(B,C),t_gpos(C,nn),t_next(C,A),t_orth(A,mixedcase)

O corpo da regra acima, que deverá ser convertida para uma consulta SPARQL. 
cada predicado binário relaciona TOKENS indicado por variáveis (letras maiúsculas)
os termons "nn" e "mixedcase" são constantes strings, ou valores.

Detalhando cada predicado acima:

  t_next(B,C)  --> B e C são variaveis do precidado indicando TOKENS --> Token A e Token B. 
                  
  t_gpos(C,nn) --> Este outro predicado o token C tem o valor da propriedade gpos igual a "nn"

  t_next(C,A)  

  t_orth(A,mixedcase)  --> o token A tem o valor da propriedade orth igual a "mixedcase"



/////////////////////////////////////////////////////////////
// USAR O PROGRAMA EDITPAD PARA ABRIR OS ARQUIVOS DE REGRAS
/////////////////////////////////////////////////////////////

https://www.editpadlite.com/download.html




