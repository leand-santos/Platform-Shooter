
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

class JogoBase extends JFrame {
  Image img[] = new Image[8];
  Desenho des = new Desenho();
  final int FUNDO = 0;
  final int PARADO = 1;
  final int ANDA0 = 2;
  final int ANDA1 = 3;
  final int SOCO0 = 4;
  final int SOCO1 = 5;
  final int SOCO2 = 6;
  final int ARBUSTO = 7;
  int pos = 100;
  int estado = PARADO;
  int dir = 1;

  class Desenho extends JPanel {

    Desenho() { // define o tamanho e carrega a imagem
      try {
        setPreferredSize(new Dimension(1000, 600));
        img[FUNDO] = ImageIO.read(new File("fundo.jpeg"));// leitura de imagem
        img[PARADO] = ImageIO.read(new File("Parado.PNG"));
        img[ANDA0] = ImageIO.read(new File("Andando1.PNG"));
        img[ANDA1] = ImageIO.read(new File("Andando2.PNG"));
        img[SOCO0] = ImageIO.read(new File("soco0.gif"));
        img[SOCO1] = ImageIO.read(new File("soco1.gif"));
        img[SOCO2] = ImageIO.read(new File("soco2.gif"));
        img[ARBUSTO] = ImageIO.read(new File("arbusto.png"));
      } catch (IOException e) { // caso não exista
        JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro",
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
    }

    public void paintComponent(Graphics g) { // desenha os componentes
      super.paintComponent(g); // chama a função JComponent do JPanel
      g.drawImage(img[FUNDO], 0, 0, getSize().width, getSize().height, this);
      g.drawImage(img[estado], pos, getSize().height - (img[estado].getHeight(this) * 3) - 20,
          img[estado].getWidth(this) * dir * 3, img[estado].getHeight(this) * 3, this); // personagem 1

      g.drawImage(img[PARADO], 900, getSize().height - img[PARADO].getHeight(this) - 20, this); // personagem 2

      g.drawImage(img[ARBUSTO], 500, getSize().height - img[ARBUSTO].getHeight(this) - 20, this);// arbusto
      Toolkit.getDefaultToolkit().sync(); // sync sincroniza com a placa de vídeo
    }
  }

  void vida() {
    if (estado == ANDA0) {
      estado = ANDA1;
    } else if (estado == ANDA1) {
      estado = PARADO;
    } else if (estado == SOCO0) {
      estado = SOCO1;
    } else if (estado == SOCO1) {
      estado = SOCO2;
    } else if (estado == SOCO2) {
      estado = PARADO;
    }
  }

  JogoBase() {
    super("Trabalho");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    add(des);
    pack();
    setVisible(true);

    new Thread() {
      public void run() {
        while (true) {
          try {
            vida();
            repaint();
            sleep(50);
          } catch (InterruptedException ex) {
          }
        }
      }
    }.start();

    addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_RIGHT:
          if (dir == -1)
            pos += 10 - img[estado].getWidth(null) * 3;
          pos += 10;
          dir = 1;
          estado = ANDA0;
          break;
        case KeyEvent.VK_LEFT:
          if (dir == 1)
            pos -= 10 - img[estado].getWidth(null) * 3;
          // pos -= 10-img[estado].getWidth(null); // tentou evitar o salto
          pos -= 10;
          dir = -1;
          estado = ANDA0;
          break;
        case KeyEvent.VK_W:
          estado = SOCO0;
          break;
        }
        System.out.println(pos);
        repaint();
      }

    });
  }

  static public void main(String[] args) {
    JogoBase f = new JogoBase();
  }
}
