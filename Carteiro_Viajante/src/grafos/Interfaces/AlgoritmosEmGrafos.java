package Interfaces;

import Construtores.Aresta;
import Construtores.TipoDeRepresentacao;
import java.util.Collection;

public interface AlgoritmosEmGrafos {

    /**
     * Carrega grafo do arquivo texto. O formato será definido do site da disciplina
     * @param path Caminho para o arquivo de texto que contém o grafo.
     * @param t Tipo de representação do grafo
     * @return um objeto grafo com as informações representadas no arquivo.
     * @throws java.lang.Exception Caminho inválido ou árquivo fora do padrão.
     */
    public Grafo carregarGrafo(String path, TipoDeRepresentacao t) throws Exception;

    /**
     * Retorna a árvore geradora mínima.
     * @param g O grafo.
     * @return Retorna a árvore geradora mínima.
     */
    public Collection<Aresta> arvoreGeradoraMinima(Grafo g);
    
    /**
     * Calcula o custo de uma árvore geradora.
     * @param arestas As arestas que compoem a árvore geradora.
     * @param g O grafo.
     * @return O custo da árvore geradora.
     * @throws java.lang.Exception Se a árvore apresentada não é geradora do grafo.
     */
    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) throws Exception;
  
}
