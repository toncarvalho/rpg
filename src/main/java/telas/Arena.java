package telas;

import controle.Jogo;
import eventos.RPGEvent;
import eventos.RPGEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.IPersonagem;
import modelo.Placar;


public class Arena extends Application implements RPGEventListener {

    private Jogo jogo = Jogo.getInstance();

    private BorderPane pane = new BorderPane();


    private VBox getRightNode() {
        VBox vBox = new VBox();
        vBox.paddingProperty().setValue(new Insets(5));
        vBox.fillWidthProperty().setValue(true);
        vBox.getChildren().add(new Label("Equipe MÁQUINA"));
        vBox.fillWidthProperty().setValue(true);
        vBox.prefHeightProperty().setValue(780);
        return vBox;
    }

    private StackPane getCenterNode() {
        StackPane pane = new StackPane();
        pane.paddingProperty().setValue(new Insets(5));

        return pane;
    }

    private VBox getLeftNode() {

        VBox vBox = new VBox();
        vBox.paddingProperty().setValue(new Insets(5));
        vBox.fillWidthProperty().setValue(true);
        vBox.getChildren().add(new Label("Equipe HUMANOS"));
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

        jogo.getEquipeMaquinas().stream().forEach(personagem -> {
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

        IPersonagem alvo = (IPersonagem) event.getSource();

        jogo.getEventSource().removeListener(alvo);
        jogo.getEventSource().removeListener(jogo.getAtaque().getAtacante());

        jogo.getEventSource().addListener(alvo);

        System.out.println(" executando a seleção e, exibição de alvos");

        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.TOP_CENTER);
        box.getChildren().add(new Label("EQUIPE HUMANA ATACA!!!"));


        PainelPersonagemNaEquipe pAtacante = new PainelPersonagemNaEquipe(jogo.getAtaque().getAtacante());

        pAtacante.getScreen().setPrefSize(200, 200);
        pAtacante.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAtacante.getScreen());

        box.getChildren().add(new Label(jogo.getAtaque().getAtacante().getNome() + " X " + alvo.getNome()));


        PainelPersonagemNaEquipe pAlvo = new PainelPersonagemNaEquipe(alvo);

        pAlvo.getScreen().setPrefSize(200, 200);
        pAlvo.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAlvo.getScreen());

        Button btnContinuar = new Button("Continua");
        btnContinuar.setOnAction(action -> {

            jogo.getEventSource().disparaExecucaoAtaque(jogo);

        });

        box.getChildren().add(btnContinuar);

        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);

    }

    @Override
    public void executaAtaque(RPGEvent event) {

        jogo.getEventSource().disparaSofrimentoAtaque(jogo.getAtaque().getAtacante());

        exibePersonagensEquipeJogador();
        exibePersonagensEquipeAdversaria();

        jogo.getEventSource().removeListener(jogo.getAtaque().getAlvo());


    }

    @Override
    public void sofreAtaque(RPGEvent event) {


        /**
         * iniciando contra-ataque, ou seja após sofrer um ataque, o sistema deve revidar, ou seja disparar um outro ataque
         * É preciso, selecionar um personagem da lista de personagens da equipe da máquina, e depois selecionar uma ação(atacar ou iniciaCura )
         * e depois selecionar um alvo, que deve ser da equipe do jogardor humano. e então disparar um ataque.
         */
        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.CENTER);

        Label lblMsg = new Label();

        box.getChildren().add(lblMsg);


        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        jogo.selecionarAtacanteParaContraAtaque();
        //     jogo.iniciarAcao(jogo.getAtaque().getAtacante());
        jogo.selecionaAlvoContraAtaque();
        jogo.getEventSource().disparaSelecaoDeAlvoContaAtaque(jogo.getAtaque().getAlvo());

    }

    @Override
    public void selecionarAlvoContraAtaque(final RPGEvent event) {

        final IPersonagem alvo = (IPersonagem) event.getSource();

        jogo.getEventSource().removeListener(alvo);
        jogo.getEventSource().removeListener(jogo.getAtaque().getAtacante());


        System.out.println(" executando a seleção e, exibição de alvos para Contra-ataque");

        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.TOP_CENTER);
        box.getChildren().add(new Label("EQUIPE MÁQUINA ATACA!!!"));


        PainelPersonagemNaEquipe pAtacante = new PainelPersonagemNaEquipe(jogo.getAtaque().getAtacante());

        pAtacante.getScreen().setPrefSize(200, 200);
        pAtacante.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAtacante.getScreen());
        box.getChildren().add(new Label(jogo.getAtaque().getAtacante().getNome() + " X " + alvo.getNome()));


        PainelPersonagemNaEquipe pAlvo = new PainelPersonagemNaEquipe(alvo);
        // PainelPersonagemNaEquipe pAlvo = new PainelPersonagemNaEquipe((jogo.getAtaque().getAlvo()));

        pAlvo.getScreen().setPrefSize(200, 200);
        pAlvo.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAlvo.getScreen());

        Button btnContinuar = new Button("Continua");
        btnContinuar.setOnAction(action -> {

            jogo.getEventSource().addListener(alvo);
            jogo.getEventSource().disparaExecucaoAtaque(jogo);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    StackPane ba = getCenterNode();

                    ba.alignmentProperty().setValue(Pos.TOP_CENTER);

                    VBox b = new VBox();
                    b.alignmentProperty().setValue(Pos.CENTER);

                    Label lblMsg = new Label("Sua vez equipe HUMANO!!!");

                    b.getChildren().add(lblMsg);


                    ba.getChildren().add(b);

                    pane.setCenter(ba);

                    exibePersonagensEquipeAdversaria();
                    exibePersonagensEquipeJogador();


                }
            });


        });

        box.getChildren().add(btnContinuar);

        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                exibePersonagensEquipeAdversaria();
                exibePersonagensEquipeJogador();


            }
        });


    }

    @Override
    public void atualizaSituacaoJogo(RPGEvent event) {

        System.out.println(" personagem morto, atualizando a situação do jogo:");

        Placar placar = jogo.analizaSituacaoJogo();

        System.out.println(" personagem morto, atualizando a situação do jogo:" + placar);

        if (placar.getJogoFinalizado()) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    StackPane ba = getCenterNode();

                    ba.alignmentProperty().setValue(Pos.TOP_CENTER);

                    VBox b = new VBox();
                    b.alignmentProperty().setValue(Pos.CENTER);

                    Label lblMsg = new Label("Parabéns equipe: " + placar.getVencedor());

                    b.getChildren().add(lblMsg);


                    ba.getChildren().add(b);

                    pane.setCenter(ba);

                    exibePersonagensEquipeAdversaria();
                    exibePersonagensEquipeJogador();


                }
            });
        }

    }

    @Override
    public void selecionarAlvoCura(RPGEvent event) {
        System.out.println("executando selecionarAlvoCura na arena");


        IPersonagem alvo = (IPersonagem) event.getSource();

        jogo.getEventSource().removeListener(alvo);
        jogo.getEventSource().removeListener(jogo.getAtaque().getAtacante());

        jogo.getEventSource().addListener(alvo);

        System.out.println(" executando a seleção e, exibição de alvo para curar");

        StackPane boxArena = getCenterNode();

        boxArena.alignmentProperty().setValue(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.alignmentProperty().setValue(Pos.TOP_CENTER);
        box.getChildren().add(new Label("EQUIPE RESTAURANDO VIDA!!!"));


        PainelPersonagemNaEquipe pAtacante = new PainelPersonagemNaEquipe(jogo.getAtaque().getAtacante());

        pAtacante.getScreen().setPrefSize(200, 200);
        pAtacante.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAtacante.getScreen());

        box.getChildren().add(new Label(jogo.getAtaque().getAtacante().getNome() + " X " + alvo.getNome()));


        PainelPersonagemNaEquipe pAlvo = new PainelPersonagemNaEquipe(alvo);

        pAlvo.getScreen().setPrefSize(200, 200);
        pAlvo.getScreen().setMaxSize(200, 200);

        box.getChildren().add(pAlvo.getScreen());

        Button btnContinuar = new Button("Continua com cura");
        btnContinuar.setOnAction(action -> {

            jogo.getEventSource().disparaExecucaoCura(jogo);

        });

        box.getChildren().add(btnContinuar);

        boxArena.getChildren().add(box);

        pane.setCenter(boxArena);
    }


    @Override
    public void iniciaCuraListener(RPGEvent event) {
        System.out.println(" executando inicia cura na arena");
        exibePersonagensEquipeJogadorCura();
    }

    private void exibePersonagensEquipeJogadorCura() {
        VBox box = getLeftNode();

        jogo.getEquipeJogador().stream().forEach(personagem -> {
            PainelPersonagemNaArenaParaCura p = new PainelPersonagemNaArenaParaCura(personagem);

            p.getScreen().maxWidthProperty().setValue(100);
            p.getScreen().maxHeightProperty().setValue(100);

            p.getScreen().prefWidthProperty().setValue(100);
            p.getScreen().prefHeightProperty().setValue(100);

            p.getScreen().paddingProperty().setValue(new Insets(5));

            box.getChildren().add(p.getScreen());


        });

        pane.setLeft(box);
        pane.setRight(null);
        pane.setCenter(null);

    }

    @Override
    public void incrementaVida(RPGEvent event) {
        System.out.println(" executando incrementaVida na arena");
        //voltando ao estado do jogo.

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                StackPane ba = getCenterNode();

                ba.alignmentProperty().setValue(Pos.TOP_CENTER);

                VBox b = new VBox();
                b.alignmentProperty().setValue(Pos.CENTER);

                Label lblMsg = new Label("Sua vez equipe HUMANO!!!");

                b.getChildren().add(lblMsg);


                ba.getChildren().add(b);

                pane.setCenter(ba);

                exibePersonagensEquipeAdversaria();
                exibePersonagensEquipeJogador();


            }
        });
    }


}
