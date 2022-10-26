import javax.swing.*;
//Jframe biblioteca de interface gráficas

public class App extends JFrame {
    public static void main(String[] args) {
        new App();
    }
    App(){
        add(new Game());//Adicionar a classe que tem os elementos da tela
        setTitle("Snake");//título
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Comando padrão para fechar a janela
        setResizable(false);//Travar a tela não permite que o tamanho da tela seja alterado
        pack();//desenha a tela
        setVisible(true);//deixar a tela visivel
        setLocationRelativeTo(null);//A tela não é relativa a nada
    }
}
