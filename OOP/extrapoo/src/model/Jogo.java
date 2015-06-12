package model;

import exceptions.InvalidQtdPlayersException;

import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Jogo {

    private List<Integer> baralho = new CopyOnWriteArrayList<>();
    private List<Jogador> jogadores = new CopyOnWriteArrayList<>();

    public Jogo() {


    }

    private void Jogar() {

        Integer rodada = 0;

        while (jogadores.size() != 1) {

            System.out.println("Rodada nº " + rodada);
            darCartas();
            imprimeBaralho();
            imprimePontuacao();
            devolverCartas();
            removeMenor();
            limpaPontuacao();
            rodada++;

        }

        System.out.println("O Vencedor é: " + jogadores.get(0).getNome());

    }

    /* Method Facade */
    public void novoJogo() {

        try {
            novoBaralho();
            novosJogadores();
        } catch (InvalidQtdPlayersException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Número de Jogadores Inválido!");
            novoJogo();
        }

        Jogar();

    }

    private void novoBaralho() {

        baralho = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 50; i++)
            baralho.add(i);

        Collections.shuffle(baralho);

    }

    private void novosJogadores() throws InvalidQtdPlayersException {

        jogadores = new CopyOnWriteArrayList<>();

        Integer qtd = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de Jogadores ( Max 15 )"));

        if (qtd > 15 || qtd < 2)
            throw new InvalidQtdPlayersException("Quantidade inválida de Jogadores!");

        for (int i = 0; i < qtd; i++) {
            Jogador jogador = new Jogador();
            jogador.setNome(JOptionPane.showInputDialog(null, "Digite o nome do Jogador " + i));
            jogadores.add(jogador);
        }

    }

    private void darCartas() {

        System.out.println("-> Distribuindo Cartas");
        jogadores.stream().forEach(jogador -> {

                    for (int i = 0; i < 3; i++) {
                        jogador.getCartas().add(getBaralho().get(0));
                        getBaralho().remove(0);
                    }

                    jogador.somarPontos();
                }
        );

    }

    private void devolverCartas() {

        System.out.println("-> Recolhendo Cartas");

        jogadores.stream().forEach(jogador -> {
                    for (int i = 0; i < 3; i++) {
                        getBaralho().add(jogador.getCartas().get(0));
                        jogador.getCartas().remove(0);
                    }
                }
        );

        Collections.shuffle(baralho);
    }

    public void imprimeBaralho() {

        System.out.println("===== Imprimindo baralho =====");
        baralho.stream().forEachOrdered(b -> System.out.print(" " + b));
        System.out.println();

    }

    public void imprimePontuacao() {

        Comparator<Jogador> porPontuacao = (j1, j2) -> Integer.compare(
                j2.getPontos(), j1.getPontos() );

        System.out.println("===== PONTUAÇÃO =====");
        jogadores.stream()
                 .sorted(porPontuacao)
                 .forEach(j -> System.out.println(j.toString()));

    }

    public void removeMenor() {
        Jogador jogadorMin = Collections.min(jogadores);
        jogadores.remove(jogadorMin);
    }

    public void limpaPontuacao() {

        jogadores.stream().forEach(j -> j.setPontos(0));

    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public List<Integer> getBaralho() {
        return baralho;
    }

    public void setBaralho(List<Integer> baralho) {
        this.baralho = baralho;
    }
}
