# 🗺️ Problema do Caixeiro Viajante (PCV)

## 📌 Sobre

Este projeto aborda algoritmos de solução e otimização para o **Problema do Caixeiro Viajante (PCV)**, implementando tanto uma abordagem de solução exata quanto uma heurística evolutiva.

---

## 🧠 Algoritmos Implementados

### Algoritmo Genético (Solução Aproximada)
Segue as premissas clássicas de algoritmos evolutivos: uma população de indivíduos (rotas) é criada e evoluída ao longo de gerações em busca de um caminho de menor custo. O melhor indivíduo de cada geração é sempre preservado na próxima (elitismo), e cada filho gerado pode sofrer mutação. Para entradas grandes (n > 20), o algoritmo é executado 30 vezes e o melhor resultado encontrado é utilizado como base.

### Solução Exata (Lower Bound / Branch and Bound)
Utiliza um algoritmo de tentativa e erro com poda baseada em lower bound. Parte de uma solução inicial obtida pelo Algoritmo Genético e, a cada passo, calcula a AGM (Árvore Geradora Mínima) dos vértices ainda não visitados. Se o custo acumulado (caminho atual + AGM + custo de ida/retorno) ultrapassar o melhor resultado conhecido até o momento, aquele ramo é podado, evitando explorar caminhos inviáveis.

---

## 🖥️ Interface Gráfica

A interface permite ao usuário:

- Carregar o grafo a partir de um arquivo `.txt`
- Escolher qual algoritmo executar (Genético ou Exato)
- Configurar parâmetros do Algoritmo Genético:
  - Tipo de mutação (ou seleção aleatória)
  - Tamanho da população
  - Número de gerações
  - Taxa de mutação

---

## 📄 Formato do Arquivo de Entrada (`.txt`)

```
<número de vértices>
<custo v0→v0> <custo v0→v1> ... <custo v0→vn>
<custo v1→v0> <custo v1→v1> ... <custo v1→vn>
...
```

**Exemplo para 4 vértices:**
```
5
0 1-10; 2-35; 3-25; 4-20;
1 0-10; 2-15; 3-30; 4-45;
2 0-35; 1-15; 3-20; 4-30;
3 0-25; 1-30; 2-20; 4-15;
4 0-20; 1-45; 2-30; 3-15;
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java JDK 8 ou superior
- IDE como IntelliJ IDEA, Eclipse ou NetBeans

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/MatheusKersul/Caixeiro-Viajante.git
   ```

2. Abra o projeto na sua IDE apontando para a pasta `Carteiro_Viajante/`.

3. Compile e execute a classe principal.

4. Na interface, carregue o arquivo de entrada, selecione o algoritmo e configure os parâmetros desejados.

