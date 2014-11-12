package telas;

import controle.Jogo;
import eventos.RPGEvent;
import eventos.RPGEventListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Arena extends Application implements RPGEventListener {

    private Jogo jogo = Jogo.getInstance();

    private BorderPane pane = new BorderPane();


    private VBox getRightNode() {
        VBox vBox = new VBox();
        vBox.fillWidthProperty().setValue(true);
        vBox.getChildren().add(new Label("Equipe Adversária (máquina)"));
        vBox.fillWidthProperty().setValue(true);
        vBox.prefHeightProperty().setValue(780);
        return vBox;
    }

    private StackPane getCenterNode() {
        StackPane pane = new StackPane();

        return pane;
    }

    private VBox getLeftNode() {

        VBox vBox = new VBox();
        vBox.fillWidthProperty().setValue(true);
        vBox.getChildren().add(new Label("Equipe do Jogador"));
        vBox.fillWidthProperty().setValue(true);
        vBox.prefHeightProperty().setValue(780);


        return vBox;
    }

    public BorderPane getScreen() {


        return pane;
    }


    @Override
    public void start(Stage stage) throws Exception {
        pane.setPrefSize(800, 600);
        pane.setLeft(getLeftNode());
        pane.setCenter(getCenterNode());
        pane.setRight(getRightNode());


        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.setTitle("ARENA");
        stage.show();

        jogo.buildEquipeAdversaria();

        //exibindo personagens da equipe do jogador
        exibePersonagensEquipeJogador();

        //exibindo personagens da equipe do jogador
        exibePersonagensEquipeAdversaria();


    }

    private void exibePersonagensEquipeAdversaria() {
        VBox boxAdversarios = getRightNode();

        jogo.getEquipeAdversaria().stream().forEach(personagem -> {
            PainelPersonagemAdversarioNaArena p = new PainelPersonagemAdversarioNaArena(personagem);

            p.getScreen().maxWidthProperty().setValue(100);
            p.getScreen().maxHeightProperty().setValue(100);

            p.getScreen().prefWidthProperty().setValue(100);
            p.getScreen().prefHeightProperty().setValue(100);

            p.getScreen().paddingProperty().setValue(new Insets(5));

            boxAdversarios.getChildren().add(p.getScreen());


        });

        pane.setRight(boxAdversarios);


    }

    private void exibePersonagensEquipeJogador() {
        VBox box = getLeftNode();

        jogo.getEquipeJogador().stream().forEach(personagem -> {
            PainelPersonagemNaArena p = new PainelPersonagemNaArena(personagem);

            p.getScreen().maxWidthProperty().setValue(100);
            p.getScreen().maxHeightProperty().setValue(100);

            p.getScreen().prefWidthProperty().setValue(100);
            p.getScreen().prefHeightProperty().setValue(100);

            p.getScreen().paddingProperty().setValue(new Insets(5));

            box.getChildren().add(p.getScreen());


        });

        pane.setLeft(box);
    }

    @Override
    public void iniciaAtaqueListener(RPGEvent e) {

        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.CENTER);

        PainelPersonagemNaEquipe p = new PainelPersonagemNaEquipe((modelo.IPersonagem) e.getSource());

        p.getScreen().setPrefSize(200, 200);
        p.getScreen().setMaxSize(200, 200);

        box.getChildren().add(p.getScreen());
        box.getChildren().add(new Label("ATACA!!!"));


        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);

    }

    @Override
    public void selecionarAlvo(RPGEvent event) {

        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.TOP_CENTER);


        PainelPersonagemNaEquipe pAtacante = new PainelPersonagemNaEquipe(jogo.getAtaque().getAtacante());

        pAtacante.getScreen().setPrefSize(200, 200);
        pAtacante.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAtacante.getScreen());
        box.getChildren().add(new Label("ATACA!!!"));


        PainelPersonagemNaEquipe pAlvo = new PainelPersonagemNaEquipe((modelo.IPersonagem) event.getSource());

        pAlvo.getScreen().setPrefSize(200, 200);
        pAlvo.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAlvo.getScreen());


        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);

    }
}
