package model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Jogador implements Comparable<Jogador> {

    private String nome;
    private List<Integer> cartas = new CopyOnWriteArrayList<>();
    private Integer pontos = 0;

    public Jogador() {

    }

    public void somarPontos(){

        for( Integer carta : cartas )
            pontos += carta;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public List<Integer> getCartas() {
        return cartas;
    }

    public void setCartas(List<Integer> cartas) {
        this.cartas = cartas;
    }

    @Override
    public int compareTo(Jogador o) {
        return pontos.compareTo(o.getPontos());
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + "\n" +
               "Pontuação: " + getPontos();
    }

}
