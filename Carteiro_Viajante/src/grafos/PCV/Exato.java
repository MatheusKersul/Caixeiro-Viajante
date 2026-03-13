package PCV;

import Interfaces.*;

import java.util.ArrayList;

import Algoritmos.*;
import Construtores.*;

public class Exato {

    private Double melhorCustoGlobal = Double.MAX_VALUE;
    private Grafo grafo;
    private AGM agm;
    private long inicio;
    private int tamanhoGrafo = 0;
    private ArrayList<Integer> melhorCaminhoGlobal = new ArrayList<>(); 
    private ArrayList<Integer> caminhoAtual = new ArrayList<>();

    
    public void CalcularExato(Grafo grafo, Double custoGenetico){

        this.grafo = grafo;
        this.inicio = System.currentTimeMillis();
        this.melhorCaminhoGlobal.clear();
        this.caminhoAtual.clear();

        if (custoGenetico != null && custoGenetico < melhorCustoGlobal){

            this.melhorCustoGlobal = custoGenetico;
            System.out.println("Estimativa inicial definida (Genético): " + this.melhorCustoGlobal);
        }



        this.agm = new AGM(grafo); 
        tamanhoGrafo = grafo.numeroDeVertices();

        if (grafo.vertices().isEmpty()) return;
        
        boolean[] visitados = new boolean[tamanhoGrafo];

        Vertice origem = grafo.vertices().get(0);
        visitados[origem.id()] = true; 
        caminhoAtual.add(origem.id());
        
        System.out.println("Iniciando algoritmo Exato com teto de corte: " + melhorCustoGlobal);


        buscarCaminho(origem, 0.0, visitados, 1);
        
        
        System.out.println("-------------------------------------------");
        System.out.println("Melhor custo exato encontrado: " + melhorCustoGlobal);
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - inicio) + "ms");
    }

    private void buscarCaminho(Vertice atual, Double custoAtual, boolean[] visitados, int qtdVisitados) {


        if (custoAtual >= melhorCustoGlobal) return;

        Double limite = melhorCustoGlobal - custoAtual;
        Double custoFuturoMinimo = agm.calcularPesoSubgrafo(grafo, visitados, limite); 
        
        if ((custoAtual + custoFuturoMinimo) >= melhorCustoGlobal){

            return; 
        }


        if (qtdVisitados < tamanhoGrafo){
            
            Double menorSaida = Double.MAX_VALUE;
            Double menorRetorno = Double.MAX_VALUE;
            Vertice verticeInicio = grafo.vertices().get(0);

            for (Aresta a : grafo.arestas(atual)){

                if (!visitados[a.destino().id()]){

                    if (a.peso() < menorSaida) menorSaida = a.peso();
                }
            }

            for (Vertice v : grafo.vertices()){

                if (!visitados[v.id()]){

                   
                    Aresta retorno = getArestaEntre(v, verticeInicio);
                    if (retorno != null && retorno.peso() < menorRetorno)

                        menorRetorno = retorno.peso();
                    
                }
            }

            if (menorSaida == Double.MAX_VALUE || menorRetorno == Double.MAX_VALUE) return;

            if ((custoAtual + custoFuturoMinimo + menorSaida + menorRetorno) >= melhorCustoGlobal) {
                return;
            }
        }



        if (qtdVisitados == tamanhoGrafo){

            Vertice inicio = grafo.vertices().get(0); 
            Aresta arestaVolta = getArestaEntre(atual, inicio);
            
            if (arestaVolta != null){

                Double custoTotal = custoAtual + arestaVolta.peso();
                if (custoTotal < melhorCustoGlobal){

                    melhorCustoGlobal = custoTotal;
                    this.melhorCaminhoGlobal = new ArrayList<>(this.caminhoAtual);
                    this.melhorCaminhoGlobal.add(grafo.vertices().get(0).id()); 
                    System.out.println("Novo recorde Exato encontrado: " + melhorCustoGlobal);
                }
            }
            return;
        }


        for (Aresta a : grafo.arestas(atual)){

            Vertice vizinho = a.destino();
            int idVizinho = vizinho.id();
            
            if (!visitados[idVizinho]) {
                
                visitados[idVizinho] = true; // Marca
                caminhoAtual.add(idVizinho);
                buscarCaminho(vizinho, custoAtual + a.peso(), visitados, qtdVisitados + 1);
                caminhoAtual.remove(caminhoAtual.size() - 1);
                visitados[idVizinho] = false; // Desmarca (Backtrack)
            }
        }
    }

    private Aresta getArestaEntre(Vertice v1, Vertice v2) {
        for(Aresta a : grafo.arestas(v1)) {
            if(a.destino().id() == v2.id()) return a;
        }
        return null;
    }

    public ArrayList<Integer> getCaminho(){

        return this.melhorCaminhoGlobal;
    }
    
    public Double getMelhorCusto(){
        
        return this.melhorCustoGlobal;
    }
}