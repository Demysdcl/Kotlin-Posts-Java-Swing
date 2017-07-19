package ui;

import business.PostBusiness;
import entity.PostEntity;

import javax.swing.*;
import java.awt.*;

public class PostDetailForm extends JFrame {
    private JPanel rootPanel;
    private JLabel lblTitulo;
    private JLabel lblBody;

    // Faz acesso a dados
    private PostBusiness mPostBusiness = new PostBusiness();

    /**
     * Construtor - Já iniciado com o Id da postagem que deve ser carregado
     */
    public PostDetailForm(int id) {
        super();

        // Carrega postagem
        this.loadPost(id);

        this.setContentPane(rootPanel);
        this.setSize(500, 250);

        // Faz com que a janela seja iniciado no centro da tela
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        this.setVisible(true);

    }

    /**
     * Carrega postagem de acordo com Id passado
     */
    private void loadPost(int id) {

        // Carrega entidade
        PostEntity entity = this.mPostBusiness.getSinglePost(id);

        // Atribui valores
        this.lblTitulo.setText(entity.getTitle());
        this.lblBody.setText("<html>" + entity.getBody() + "</html>");

    }
}
