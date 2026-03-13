package main;


import java.util.Collection;

import Algoritmos.AGM;
import Construtores.Aresta;
import Construtores.TipoDeRepresentacao;
import Construtores.Vertice;
import Interfaces.AlgoritmosEmGrafos;
import Interfaces.Grafo;
import Metodos.Matriz_de_Adjacencia;
import Metodos.Lista_de_Adjacencia;

import java.util.ArrayList;

public class Algoritmos implements AlgoritmosEmGrafos {

    public Grafo carregarGrafo(String path, TipoDeRepresentacao t) throws Exception {

        FileManager fileManager = new FileManager();
        ArrayList<String> todasLinhas = fileManager.stringReader(path);

        if (todasLinhas == null || todasLinhas.isEmpty()) {
            throw new Exception("Erro: Arquivo está vazio ou não foi encontrado.");
        }

        boolean isMatrixFormat = false;
        String firstLine = todasLinhas.get(0).trim();

        if (firstLine.contains("-") || firstLine.contains(";")) {
            isMatrixFormat = true;
        }

        try {
            int numeroDeVertices;
            int loopStartIndex; 

            if (isMatrixFormat) {
                numeroDeVertices = todasLinhas.size();
                loopStartIndex = 0;
            } else {
                numeroDeVertices = Integer.parseInt(firstLine);
                loopStartIndex = 1;
            }

            Grafo grafo = null;
            if (t == TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA) {
                grafo = new Matriz_de_Adjacencia(numeroDeVertices);
            } 
            else if (t == TipoDeRepresentacao.LISTA_DE_ADJACENCIA) {
                grafo = new Lista_de_Adjacencia();
                for (int i = 0; i < numeroDeVertices; i++) {
                    grafo.adicionarVertice(new Vertice(i));
                }
            } 
            else {
                throw new IllegalArgumentException("Tipo de representação inválida.");
            }

            for (int i = loopStartIndex; i < todasLinhas.size(); i++) {
                
                String linha = todasLinhas.get(i).trim();
                if (linha.isEmpty()) continue; 

                int idOrigem;
                String arestasString;

                if (isMatrixFormat) {
                    idOrigem = i;
                    arestasString = linha;
                } else {
                    String[] partes = linha.split(" ", 2);
                    
                    idOrigem = Integer.parseInt(partes[0]);
                    
                    if (partes.length < 2) {
                        continue; 
                    }
                    arestasString = partes[1];
                }

                String arestasStringLimpa = arestasString.replaceAll("\\s+", "");
                String[] arestas = arestasStringLimpa.split(";");

                for (String aresta : arestas) {
                    if (!aresta.isEmpty()) {
                        String[] destinoPeso = aresta.split("-");
                        int idDestino = Integer.parseInt(destinoPeso[0]);

                        if (idOrigem >= numeroDeVertices || idDestino >= numeroDeVertices) {
                            System.err.println("Aviso: Vértice inválido na linha " + (i+1) + ". Aresta ("+idOrigem+"->"+idDestino+") ignorada.");
                            continue;
                        }

                        Vertice vOrigem = grafo.vertices().get(idOrigem);
                        Vertice vDestino = grafo.vertices().get(idDestino);

                        if (destinoPeso.length > 1) {
                            double peso = Double.parseDouble(destinoPeso[1]);
                            grafo.adicionarAresta(vOrigem, vDestino, peso);
                        } else {
                            grafo.adicionarAresta(vOrigem, vDestino);
                        }
                    }
                }
            } 

            return grafo;

        } catch (NumberFormatException e) {

            throw new Exception("Erro de formato numérico ao carregar o grafo: " + e.getMessage(), e);
        } catch (Exception e) {

            throw new Exception("Erro ao carregar o grafo: " + e.getMessage(), e);
        }
    
    }

    public Collection<Aresta> arvoreGeradoraMinima(Grafo g) { //finalizado
        
        AGM func = new AGM(g);
        Collection<Aresta> AGM = func.CalcularAGM(g);
        
        return AGM;
    }

    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) {//finalizado
        
        AGM func = new AGM(g);
        return func.CustoAGM(g, arestas);
    }

}
