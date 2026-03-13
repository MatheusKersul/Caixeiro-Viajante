package PCV_Genetico;

import java.util.ArrayList;
import Interfaces.*;
import Construtores.*;

public class Individuo{

    private ArrayList<Vertice> genes;
    private double fitness;
    private Grafo grafo;
    private static java.util.HashMap<Integer, java.util.HashMap<Integer, Double>> cacheArestas = null;
    
    public Individuo(Grafo g){
        this.grafo = g;
        this.genes = new ArrayList<>();
        GerarIndividuos();

        if (!ValidarPai()){
            this.fitness = Double.MAX_VALUE;
        }
    }

    public Individuo(Grafo g, boolean vazio) {
        this.grafo = g;
        this.genes = new ArrayList<>();
        this.fitness = 0.0;
        // NÃO chama GerarIndividuos()
    }

    public void GerarIndividuos() {
        java.util.Random random = new java.util.Random();
        boolean caminhoEncontrado = false;
        int totalCidades = grafo.vertices().size();

        while (!caminhoEncontrado) {
            this.genes.clear();
            java.util.HashSet<Integer> visitados = new java.util.HashSet<>();

            Vertice atual = grafo.vertices().get(0);
            this.genes.add(atual);
            visitados.add(atual.id());

            boolean becoSemSaida = false;

            while (this.genes.size() < totalCidades) {
                java.util.ArrayList<Vertice> candidatos = new java.util.ArrayList<>();
                
                for (Aresta aresta : grafo.arestas(atual)) {
                    Vertice destino = aresta.destino(); 
                    if (!visitados.contains(destino.id())) {
                        candidatos.add(destino);
                    }
                }

                if (candidatos.isEmpty()) {
                    becoSemSaida = true;
                    break;
                }

                int indiceSorteado = random.nextInt(candidatos.size());
                atual = candidatos.get(indiceSorteado);

                this.genes.add(atual);
                visitados.add(atual.id());
            }

            if (!becoSemSaida) {
                boolean retornoPossivel = false;
                for (Aresta aresta : grafo.arestas(atual)) {
                    if (aresta.destino().id() == 0) {
                        retornoPossivel = true;
                        break;
                    }
                }

                if (retornoPossivel) {
                    this.genes.add(grafo.vertices().get(0));
                    caminhoEncontrado = true;
                }
            }
        }
    }

    public boolean ValidarPai() {
        
        // 2. CARREGA O CACHE UMA ÚNICA VEZ (Lazy Loading)
        if (cacheArestas == null) {
            cacheArestas = new java.util.HashMap<>();
            // Varre a Lista de Adjacência do Grafo e salva no HashMap
            for (Vertice v : grafo.vertices()) {
                java.util.HashMap<Integer, Double> destinos = new java.util.HashMap<>();
                for (Aresta a : grafo.arestas(v)) {
                    destinos.put(a.destino().id(), a.peso());
                }
                cacheArestas.put(v.id(), destinos);
            }
        }

        // 3. VALIDAÇÃO O(1) - INSTANTÂNEA
        this.fitness = 0.0;

        // Checagem de tamanho
        if (this.genes == null || this.genes.size() < this.grafo.vertices().size()) {
            this.fitness = Double.MAX_VALUE;
            return false;
        }

        int tamanho = genes.size();
        for (int i = 0; i < tamanho - 1; i++) {
            
            int idOrigem = genes.get(i).id();
            int idDestino = genes.get(i + 1).id();

            // Busca direta no mapa (sem loops, sem criar listas)
            // Verifica se a cidade de origem tem saídas e se tem conexão com o destino
            if (!cacheArestas.containsKey(idOrigem) || !cacheArestas.get(idOrigem).containsKey(idDestino)) {
                this.fitness = Double.MAX_VALUE;
                return false; // Caminho não existe
            }
            
            // Soma o peso recuperado do mapa
            this.fitness += cacheArestas.get(idOrigem).get(idDestino);
        }
        
        return true;
    }

    public Double GetFitness(){

        

        if (fitness == 0.0 || fitness == Double.MAX_VALUE){

            ValidarPai();
        }
        return fitness;
        
    }

    public ArrayList<Vertice> getGenes(){

        return genes;
    }

    public void AddGenes(Vertice v){

        this.genes.add(v);
        
    }

    public int getTamanho (){

        return this.genes.size();
    }

    public Individuo(Individuo outro) {
    
        this.grafo = outro.grafo;

        if (outro.getGenes() != null){

            this.genes = new ArrayList<>(outro.getGenes());
        } 
        else{

            this.genes = new ArrayList<>();
        }
    }
    
    public static void resetCache(){
    cacheArestas = null;
}
}
