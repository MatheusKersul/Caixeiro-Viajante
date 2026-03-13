package Algoritmos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


import Construtores.Aresta;

import Construtores.Vertice;
import Interfaces.Grafo;




public class AGM {
    

    private ArrayList<Aresta> todasArestas = new ArrayList<>();
    public AGM(Grafo g){

        try{
            for (Vertice v: g.vertices()){

                for( Vertice u : g.vertices()){

                    for (Aresta a : g.arestasEntre(v, u)){

                        todasArestas.add(a);
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Collection<Aresta> CalcularAGM(Grafo g){


    Collection<Aresta> ramos_da_arvore = new ArrayList<>();
    ArrayList<Aresta> todasArestas = new ArrayList<>();
    Map<Vertice, ArrayList<Vertice>> conjuntoDosVertices = new HashMap<>();

        try {
            for (Vertice u : g.vertices()){
                
                for (Vertice v : g.adjacentesDe(u)){

                    for(Aresta a : g.arestasEntre(u,v)){

                        todasArestas.add(a);
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        todasArestas.sort(Comparator.comparingDouble(Aresta::peso));

        

        for (Vertice u : g.vertices()){

            ArrayList<Vertice> lista = new ArrayList<>();
            lista.add(u);
            conjuntoDosVertices.put(u, lista);
        }



        for (Aresta a : todasArestas){

            ArrayList<Vertice> dVertices = null;
            ArrayList<Vertice> oVertices = null;

            for (ArrayList<Vertice> lista : conjuntoDosVertices.values()){

                if (lista.contains(a.origem())) oVertices = lista;

                if (lista.contains(a.destino())) dVertices = lista;

            }

                if (oVertices != null && dVertices != null && oVertices != dVertices){

                    oVertices.addAll(dVertices);

                    for (Vertice v : dVertices) {

                        conjuntoDosVertices.put(v, oVertices);
                    }

                    ramos_da_arvore.add(a);
                

               }

            }
        


        return ramos_da_arvore;
    }

    public double CustoAGM(Grafo g, Collection<Aresta> ramos_da_arvore){ 

        double custo = 0.0;

        if (ramos_da_arvore.isEmpty()) {
            CalcularAGM(g);
        }
        for (Aresta a : ramos_da_arvore){
            custo += a.peso();
        }

            return custo;
    
    }

    public Double calcularPesoSubgrafo(Grafo grafo, boolean[] visitados, Double limiteParaCorte) {

        int verticesLivres = 0;
        for(boolean v : visitados) {
            if(!v) verticesLivres++;
        }

        if (verticesLivres < 2) return 0.0;
        
        ArrayList<Aresta> arestasValidas = new ArrayList<>();
        Double pesoTotal = 0.0;
        
        for (Aresta a : todasArestas){ 

            int idOrigem = a.origem().id();   
            int idDestino = a.destino().id();
            if (!visitados[idOrigem] && !visitados[idDestino]){

                arestasValidas.add(a);
            }
        }
        
        if (arestasValidas.isEmpty()) return 0.0;
        Collections.sort(arestasValidas, new Comparator<Aresta>() {
            @Override
            public int compare(Aresta a1, Aresta a2) {
                return Double.compare(a1.peso(), a2.peso());
            }
        });

        int[] pais = new int[visitados.length]; 
        for(int i=0; i<pais.length; i++) pais[i] = i; // Inicializa Union-Find
        
        int arestasContadas = 0;
        
        for (Aresta a : arestasValidas) {
            int raizOrigem = find(pais, a.origem().id());
            int raizDestino = find(pais, a.destino().id());
            
            if (raizOrigem != raizDestino) {
                pesoTotal += a.peso();

                if (pesoTotal >= limiteParaCorte) {
                    return Double.MAX_VALUE; 
                }

                union(pais, raizOrigem, raizDestino);
                arestasContadas++;

                if (arestasContadas == verticesLivres - 1) {
                    break;
                }

            }
        }
        
        return pesoTotal;
    }

    // Métodos auxiliares do Union-Find (para o Kruskal funcionar)
    private int find(int[] pais, int i) {
        if (pais[i] == i) return i;
        return find(pais, pais[i]);
    }
    
    private void union(int[] pais, int i, int j) {
        int raizI = find(pais, i);
        int raizJ = find(pais, j);
        pais[raizI] = raizJ;
    }
}

