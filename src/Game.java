import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
public class Game extends JPanel implements ActionListener{
    //Constantes para definir as domenções da tela
    private static final int LARGURA_DA_TELA = 1200; 
    private static final int ALTURA_DA_TELA = 680;
    private static final int TAMANHO_DO_BLOCO = 50;
    private static final int UNIDADES = LARGURA_DA_TELA * ALTURA_DA_TELA / (TAMANHO_DO_BLOCO * TAMANHO_DO_BLOCO);//cria a malha de "pixels" na tela: tamanho da Tela / Tamanho do bloco
    // private static final int VELOCIDADE = 200;//Velocidade da cobrinha
    private static final String NOME_FONTE = "Arial";
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoY = new int[UNIDADES];

    //Var
    private int corpoCobra = 6;
    private int frutascomidas = 0;//Literalmente placar
    private int posX;//para definir a posição da fruta no eixo X
    private int posY;//posição da fruta no eixo Y
    private char direcao = 'D';//Direção inicial da cobra: direita
    private boolean rodar = false;
    private boolean estaMenu = true;
  
    
    private int vel = 200;
    Timer timer;//tempo para Atualizar a tela
    Random random;//Para definir as posições aleatorias das frutas
    //padrão de janela 
    Game(){
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_DA_TELA, ALTURA_DA_TELA));//Dimensionar a tela
        setBackground(Color.DARK_GRAY);//Cor do backGround da janele
        setFocusable(true);//foco da janela
        addKeyListener(new LeitorDeTeclasAdapter());//Leitor das teclas
    }
    public void menu(Graphics g){
        g.setColor(Color.GREEN);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 60));
        FontMetrics menu = getFontMetrics(g.getFont());
        g.drawString("Snake Game", (LARGURA_DA_TELA - menu.stringWidth("Snake Game")) / 2, 100);
        g.setColor(Color.GREEN);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 60));
        FontMetrics jogar = getFontMetrics(g.getFont());
        g.drawString("Aperte 'ENTER' Para jogar", (LARGURA_DA_TELA - jogar.stringWidth("Aperte 'Enter' Para jogar")) / 2, 300);
        // g.setColor(Color.RED);
        // g.setFont(new Font(NOME_FONTE, Font.BOLD, 60));
        // FontMetrics quit = getFontMetrics(g.getFont());
        // g.drawString("Aperte 'ESC' Para jogar", (LARGURA_DA_TELA - quit.stringWidth("Aperte 'ESC' Para jogar")) / 2, 500);
    }
    //Metodo para dar play
    public void iniciarJogo(){
        criarFruta();//cria a fruta
        rodar = true;//começar a rodar o jogo
        timer = new Timer(vel, this);//atualizar a velocidade da cobra nesse metodo
        timer.start();//iniciara o jogo
    }
    public void gameOver(Graphics g){
        //Desenha as mensagens de game over e a pontuação final
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Pontos: " + frutascomidas, (LARGURA_DA_TELA - metrics.stringWidth("Pontos: " + frutascomidas)) / 2, g.getFont().getSize());  

        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics fontLose = getFontMetrics(g.getFont());
        g.drawString("Game Over", (LARGURA_DA_TELA - metrics.stringWidth("Game Over")) / 2, ALTURA_DA_TELA / 2);
        g.drawString("Digite R para jogar novamente", (LARGURA_DA_TELA - fontLose.stringWidth("Digite R para jogar novamente")) / 2, 400);

 


    }
    //metodos para desenhar os componentes do jogo

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }
    public void desenhar(Graphics g){
        if (estaMenu) {
            menu(g);
        }
        if (!estaMenu) {
                if (rodar) {
                //desenho da maçã
                g.setColor(Color.red);
                g.fillOval(posX, posY, TAMANHO_DO_BLOCO - 15, TAMANHO_DO_BLOCO - 15);
                //Desenho da Cobra
                for (int i = 0; i < corpoCobra; i++) {
                    if (i == 0) {
                        g.setColor(new Color(0, 150, 0));
                        g.fillRect(eixoX[0], eixoY[0], TAMANHO_DO_BLOCO - 1, TAMANHO_DO_BLOCO - 1);
                    }else{
                        
                        g.setColor(Color.green);
                        g.fillRect(eixoX[i], eixoY[i], TAMANHO_DO_BLOCO - 1, TAMANHO_DO_BLOCO - 1);
                        
                        
                    
                    }
                }
                g.setColor(Color.red);
                g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Pontos: " + frutascomidas, (LARGURA_DA_TELA - metrics.stringWidth("Pontos: " + frutascomidas)) / 2, g.getFont().getSize());   


            }else{gameOver(g);}
        }

    }


    //setar as posições aleatorias da fruta
    private void criarFruta() {
        posX = random.nextInt(LARGURA_DA_TELA / TAMANHO_DO_BLOCO) * TAMANHO_DO_BLOCO;
        posY = random.nextInt(ALTURA_DA_TELA / TAMANHO_DO_BLOCO) * TAMANHO_DO_BLOCO;
        
        
    }
    //responsavel por atualizar as funções do jogo
    @Override
    public void actionPerformed(ActionEvent e) {
        if (rodar) {
            andar();//Movimentação da cobra
            comerFruta();
            morrer();
            
        }
        repaint();
        
    }
    public void andar(){
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }
        switch (direcao) {
            case 'W':
                eixoY[0] = eixoY[0] - TAMANHO_DO_BLOCO;
                break;
            case 'S':
                eixoY[0] = eixoY[0] + TAMANHO_DO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_DO_BLOCO;
                break;
            case 'A':
                eixoX[0] = eixoX[0] - TAMANHO_DO_BLOCO;
            default:
                break;
        }
    }
    public void comerFruta(){
        if (eixoX[0] == posX && eixoY[0] == posY) {
            frutascomidas++;
            corpoCobra++;
            criarFruta();
        }
    }
    public void morrer(){
        //A cabeça bateu no corpo?
        for (int i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]) {
                rodar = false;
                break;
            }
        }
        if (eixoX[0] < 0 || eixoX[0] > LARGURA_DA_TELA) {
            rodar = false;
        }
        if (eixoY[0] < 0 || eixoY[0] > ALTURA_DA_TELA) {
            rodar = false;
        }
        if (!rodar) {
            timer.stop();
        }
    }
    public class LeitorDeTeclasAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'A';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'A') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'S') {
                        direcao = 'W';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'W') {
                        direcao = 'S';
                    }
                    break;
                case KeyEvent.VK_R:
                    reset();
                    break;
                case KeyEvent.VK_ENTER:
                    iniciarJogo();
                    estaMenu = false;
                    break;
                
                default:
                    break;
            }
        }
    }
    public void reset(){
        if (!rodar) {
            corpoCobra = 6;
            frutascomidas = 0;
            eixoX[0] = 0;
            eixoY[0] = 0;
            direcao = 'D';
            rodar = true;
            iniciarJogo();
        }
    }
   
}
