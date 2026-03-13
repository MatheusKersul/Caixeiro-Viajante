package PCV_Genetico;

import java.util.ArrayList;
import java.util.Random;
import Interfaces.*;
import PCV_Genetico.Mutacoes.TipoDeMutacao;
import Construtores.*;
public class Genetico {
    

    private Individuo melhorIndividuo;
    private ArrayList<Integer> caminho;
    private int Npopulacao, Ngeracao, taxaMutacao;
    private TipoDeMutacao tipoMutacao;

    public Genetico(int populacao, int geracao, int mutacao, TipoDeMutacao tipoMutacao){

        this.Npopulacao = populacao;
        this.Ngeracao = geracao;
        this.taxaMutacao = mutacao;
        this.tipoMutacao = tipoMutacao;
    }
    
    public void CalcularGenetico(Grafo grafo){


        this.melhorIndividuo = null;
        Double menorFitness = Double.MAX_VALUE;
        this.caminho = new ArrayList<>();
        ArrayList<Individuo> populacao = new ArrayList<>();
        int tamanhoDaPopulacao = Npopulacao;
       

            while (populacao.size() < tamanhoDaPopulacao){

                Individuo i = new Individuo(grafo);
                if (i.ValidarPai()){
                    populacao.add(i);
                    if (i.GetFitness() < menorFitness){
                        menorFitness = i.GetFitness();
                        melhorIndividuo = i;
                    }

                }
            }
        


        Mutacoes mutacoes = new Mutacoes(grafo);
        Vertice cidadeInicial = grafo.vertices().get(0);
        ArrayList<Individuo> novaGeracao = new ArrayList<>();
        int numeroMaximoDeGeracoes = Ngeracao; 
        int geracaoAtual = 0;
        int pai1= 0;
        int pai2= 0;
        Random random = new Random();

        while (geracaoAtual < numeroMaximoDeGeracoes){

            novaGeracao.add(melhorIndividuo);                           //NÃO ESQUECER DE SALVAR O MELHOR INDIVIDUO
            while(novaGeracao.size() < tamanhoDaPopulacao - 1){

                Individuo candidato1 = null;
                Individuo candidato2 = null;
                pai1 = random.nextInt(tamanhoDaPopulacao - 2) + 1;
                pai2 = random.nextInt(tamanhoDaPopulacao - 2) + 1;
                

                if (populacao.get(pai1).GetFitness() < populacao.get(pai2).GetFitness()){

                    candidato1 = populacao.get(pai1);
                }
                else{

                    candidato1 = populacao.get(pai2);
                }
                /*========================== CANDIDATO 2========================== */
                pai1 = random.nextInt(tamanhoDaPopulacao - 2) + 1;
                pai2 = random.nextInt(tamanhoDaPopulacao - 2) + 1;

                if (populacao.get(pai1).GetFitness() < populacao.get(pai2).GetFitness()){

                    candidato2 = populacao.get(pai1);
                }
                else{

                    candidato2 = populacao.get(pai2);
                }

                Individuo filho1 = new Individuo(grafo, true); 
                Individuo filho2 = new Individuo(grafo, true);

                filho1.AddGenes(cidadeInicial); //primeira cidade
                filho2.AddGenes(cidadeInicial);

                int totalCidades = candidato1.getGenes().size();

                int divisaoMaxima = totalCidades - 2;
                if (divisaoMaxima <= 1)
                    divisaoMaxima = 1;
                /*========================== PRIMEIRO FILHO========================== */

                int fatia = random.nextInt(divisaoMaxima) + 1;
                boolean[] visitados = new boolean[candidato1.getTamanho()];
                visitados[cidadeInicial.id()] = true;

                for (int i = 1; i < fatia; i++){

                    filho1.AddGenes(candidato1.getGenes().get(i));
                    visitados[candidato1.getGenes().get(i).id()] = true;

                }

                for (int i = 1; i< candidato2.getTamanho(); i++){

                    if (!visitados[candidato2.getGenes().get(i).id()]){
                        filho1.AddGenes(candidato2.getGenes().get(i));
                        visitados[candidato2.getGenes().get(i).id()] = true;
                    }
                }
                filho1.AddGenes(cidadeInicial); //ultima cidade
                /*========================== SEGUNDO FILHO========================== */

                boolean[] visitados2 = new boolean[candidato1.getTamanho()];
                visitados2[cidadeInicial.id()] = true;
                int fatia2 = random.nextInt(divisaoMaxima) + 1;

                for (int i = 1; i < fatia2; i++){

                    filho2.AddGenes(candidato2.getGenes().get(i));
                    visitados2[candidato2.getGenes().get(i).id()] = true;

                }

                for (int i = 1; i< candidato1.getTamanho(); i++){

                    if (!visitados2[candidato1.getGenes().get(i).id()]){
                        filho2.AddGenes(candidato1.getGenes().get(i));
                        visitados2[candidato1.getGenes().get(i).id()] = true;
                    }
                }
                filho2.AddGenes(cidadeInicial);
                int taxa = random.nextInt(100);
                if (taxa <= taxaMutacao){

                    filho1 = mutacoes.IniciarMutacao(this.tipoMutacao, filho1);;
                    filho2 = mutacoes.IniciarMutacao(this.tipoMutacao,filho2);
                }

                if (filho1.ValidarPai()){

                    novaGeracao.add(filho1);
                } 
                else{
                    
                    novaGeracao.add(new Individuo(candidato1)); 
                }
                if (filho2.ValidarPai()){

                    novaGeracao.add(filho2);
                }
                else{
                    
                    novaGeracao.add(new Individuo(candidato2));
                }
            }

            for (Individuo ind : novaGeracao){
       
                if (ind.GetFitness() < menorFitness){
                    menorFitness = ind.GetFitness(); // Atualiza o recorde numérico
                    melhorIndividuo = ind;           // Atualiza o objeto (DNA)
                }
            }
            
            geracaoAtual ++;
            populacao = new ArrayList<>(novaGeracao);
            novaGeracao.clear();
        }


        for(int i = 0; i < melhorIndividuo.getTamanho() - 1; i++){

            caminho.add(melhorIndividuo.getGenes().get(i).id());
        }
        caminho.add(melhorIndividuo.getGenes().get(0).id());
        System.out.println("\nMelhor caminho: " + melhorIndividuo.GetFitness());
        System.out.println(("caminho: " + getCaminho()));
    }

    public double GetMelhorCaminho(){

        return melhorIndividuo.GetFitness();
    }

    public ArrayList<Integer> getCaminho(){

        return this.caminho;
    }
}