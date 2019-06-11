# Trabalho-Jogo
*<p align="center">Desenvolvimento do projeto para a matéria de Técnicas de Programação, <strong>uso educacional</strong></p>*
## Resumo 
A criação de um **jogo de plataforma em 2D** em uma arquitetura cliente-servidor para a matéria de Técnicas de Programação, cujo objetivo é a eliminação do adversário com o uso de uma arma de fogo em um mapa de tamanho médio, onde há a possibilidade do jogador *pular, atirar, agachar e coletar Power-Ups (poderes especiais)*.
## Introdução
Um jogo de plataforma é uma categoria de jogos que permite ao jogador subir e descer plataformas que foram dispostas pelo mapa. Foi bastante popular no início da era dos jogos. Na figura 1, temos um exemplo de um jogo de plataforma. 

**<p align="center">Figura 1 - Donkey Kong (1981)</p>**

<p align="center">
  <img width="480" height="360" src="https://i.ytimg.com/vi/lwwmUqUTJQY/hqdefault.jpg">
</p>

Este é um jogo para dois jogadores real-time (sem turnos), onde cada jogador poderá andar com as teclas A-D para a direita ou esquerda, pular com o SPACE ou agachar com o SHIFT. Além de poder atirar com o botão esquerdo do MOUSE. 
 
Além das plataformas, o jogador terá a possibilidade de coletar poderes especiais para que ele possa: 
- Atirar mais rápido no oponente (Tiro Rápido); 
- Correr mais rápido (Corrida); 
- Adicionar uma vida a mais (Vida extra); 
- Escudo temporário (Escudo). 

Cada oponente, terá uma munição para tentar eliminar o adversário. Caso erre o tiro, a arma do jogador será automaticamente recarregada dentro de alguns segundos para que ele possa efetuar novamente a ação de atirar.
## Objetivo
Desenvolver os programas necessários para que duas pessoas, possivelmente remotamente, consigam interagir entre si, utilizando o teclado e o mouse, em uma interface gráfica. 
##Materiais e Métodos 
O jogo apresenta uma arquitetura cliente-servidor, onde dois jogadores remotos conseguem interagir entre si via servidor, utilizando um cliente idêntico. O programa será desenvolvido na linguagem JAVA. Na figura 2, podemos ver um exemplo de como será um mapa do jogo, no qual o jogador terá liberdade para percorrer o mapa em qualquer direção, assim como atirar em seu oponente. 

**<p align="center">Figura 2 - TowerFall Ascension (2013) </p>**

<p align="center">
  <img width="480" height="360" src="https://gocdkeys.pt/images/captures/towerfall-ascension-pc-cd-key-4.jpg">
</p>
                                                           
O programa do usuário será responsável pela interface gráfica. Sendo esta, um mapa onde o jogador consegue se locomover para cima, para baixo, para a direita e para a esquerda. Quando o jogador fizer qualquer ação, o cliente enviará para o servidor a ação pretendida pelo usuário, caso ele possa executar a ação, o servidor repassará a informação de confirmação da execução.  
O programa esperará receber uma atualização do estado da interface gráfica para poder ser atualizado. O servidor ficará aguardando as ações dos jogadores para que ele possa confirmá-las ou não. Quando alguém realizar uma ação, o servidor retornará ao cliente todas as informações necessárias para que a interface seja atualizada. 
## Considerações finais 
Desenvolvimento de um jogo de tiro de plataforma em JAVA com a arquitetura cliente-servidor a fim de que possa ser jogado remotamente. 
 
## Referências 
Greenslade, Amanda. Gamespeak: A glossary of gaming terms. Specusphere, jun. 2006. Disponível em: < https://web.archive.org/web/20070219082328/http://www.specusphere.com/joomla/index.p hp?option=com_content&task=view&id=232&Itemid=32 >. Acesso em 6 de abril de 2019. 
 
