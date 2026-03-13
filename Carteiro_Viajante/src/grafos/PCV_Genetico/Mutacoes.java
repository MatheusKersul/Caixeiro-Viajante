package PCV_Genetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Interfaces.*;
import Construtores.*;

public class Mutacoes{
    
    Random random = new Random();
    Grafo grafo = null;

    public enum TipoDeMutacao{

        Aleatorio,
        Insercao, 
        Mistura,
        Troca,
        Inversao;
    }

    public Individuo IniciarMutacao(TipoDeMutacao tipo, Individuo filho){

        switch (tipo) {
            case Insercao:
                return Insercao(filho);
                
            case Troca:
                return Troca(filho);
                
            case Mistura:
                return Mistura(filho);
                
            case Inversao:
                return Inversao(filho);
            
            case Aleatorio: 
                TipoDeMutacao[] tipos = {
                TipoDeMutacao.Insercao,
                TipoDeMutacao.Mistura,
                TipoDeMutacao.Troca,
                TipoDeMutacao.Inversao
                };
                
                int idx = ThreadLocalRandom.current().nextInt(tipos.length);
                return IniciarMutacao(tipos[idx], filho);
                
            default:
                return Troca(filho);
                
        }
    }

    public Mutacoes(Grafo g){
        this.grafo = g;
    }

    /*
        1 - Escolhe aleatoriamente um gene
        2 - Escolhe aleatoriamente uma posição
        3 - Coloca esse gene nessa posição e anda as outras casas para o lado
    */
    public Individuo Insercao(Individuo filho){

        boolean validar = false;
        Individuo backup = new Individuo(filho);
        ArrayList<Vertice> genes = backup.getGenes();

        

            int tamanho = genes.size();
            int pos = 1 + random.nextInt(tamanho - 2);
            int elemento = 1+random.nextInt(tamanho - 2);

            Vertice troca = genes.get(elemento);
            genes.remove(elemento);

            if (elemento < pos)    
                pos--; 

            genes.add(pos, troca);
            validar = backup.ValidarPai();
            if (!validar){
                
                return filho;
            }
        
        
        return backup;
    }

    /*
        1 - Escolhe aleatoriamente um intervalo de posições
        2 - Embaralha os genes desse intervalo
    */ 
    public Individuo Mistura(Individuo filho){

        boolean validar = false;
        Individuo backup = new Individuo(filho);
        ArrayList<Vertice> genes = backup.getGenes();


        int tamanho = genes.size();
        int pos1 = 1 + random.nextInt(tamanho - 2);
        int pos2 = 1 + random.nextInt(tamanho - 2);

        while (pos1 == pos2){

            pos1 = 1 + random.nextInt(tamanho - 2);
            pos2 = 1 + random.nextInt(tamanho - 2);
        }

        if (pos1 > pos2){

            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }
        Collections.shuffle(genes.subList(pos1, pos2+ 1));
        validar = backup.ValidarPai();
        if (!validar){
            
            return filho;
        }
    
        return backup;
    }

    /*
        1 - Escolhe aleatoriamente dois genes
        2 - Troca ambas posições
        (melhor método)
    */
    public Individuo Troca(Individuo filho){

        boolean validar = false;
        Individuo backup = new Individuo(filho);
        ArrayList<Vertice> genes = backup.getGenes();


            int pos1 = 0;
            int pos2 = 0;

            while (pos1 == pos2){

            pos1 = 1+ random.nextInt(filho.getTamanho() - 2);
            pos2 = 1+ random.nextInt(filho.getTamanho() - 2);
            }

            Collections.swap(genes, pos1, pos2);
            validar = backup.ValidarPai();
            if (!validar){
                
                return filho;
            }
    
        return backup;
    
    }

    /*
        1 - Escolhe aleatoriamente um intervalo de posições
        2 - Inverte esse intervalo
    */
    public Individuo Inversao(Individuo filho){


        boolean validar = false;
        Individuo backup = new Individuo(filho);
        ArrayList<Vertice> genes = backup.getGenes();

            int tamanho = genes.size();
            int pos1 = 1 + random.nextInt(tamanho - 2);
            int pos2 = 1 + random.nextInt(tamanho - 2);

            while (pos1 == pos2){

                pos1 = 1 + random.nextInt(tamanho - 2);
                pos2 = 1 + random.nextInt(tamanho - 2);
            }

            if (pos1 > pos2){

                int temp = pos1;
                pos1 = pos2;
                pos2 = temp;
            }
            Collections.reverse(genes.subList(pos1, pos2 + 1));
            validar = backup.ValidarPai();
            if (!validar){
                
                return filho;
            }
        
        return backup;
    }
}
