package main;
import Construtores.*;
import Interfaces.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import PCV.*;
import PCV_Genetico.*;
import PCV_Genetico.Mutacoes.TipoDeMutacao;

public class MainGUI extends JFrame {

    private final JTextField txtCaminhoArquivo;
    private final JComboBox<TipoDeMutacao> comboRepresentacao;
    private final JComboBox<AlgoritmoParaTestar> comboAlgoritmo; 
    private final JTextField txtPopulacao;
    private final JTextField txtGeracao;
    private final JTextField txtTaxaMutacao; // <--- NOVO CAMPO
    private final JButton btnExecutar;
    private final JTextArea areaLog; 
    private final JFileChooser fileChooser;

    private enum AlgoritmoParaTestar {
        BASICOS("Informações Básicas"),
        PCV_EXATO("Tentativa e erro"),
        PCV_GENETICO("Algoritmo genético");
        
        private final String label;
        AlgoritmoParaTestar(String label) { this.label = label; }
        @Override public String toString() { return this.label; }
    }

    public MainGUI() {
        setTitle("Problema do Caixeiro Viajante");
        setSize(1000, 600); // Aumentado um pouco para caber o novo campo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        this.fileChooser = new JFileChooser("."); 

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new GridBagLayout());
        panelControles.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridy = 0; gbc.gridx = 0;
        panelControles.add(new JLabel("Arquivo do Grafo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; 
        txtCaminhoArquivo = new JTextField(40);
        txtCaminhoArquivo.setEditable(false);
        panelControles.add(txtCaminhoArquivo, gbc);
        gbc.gridx = 2; gbc.weightx = 0.0;
        JButton btnProcurar = new JButton("Procurar...");
        panelControles.add(btnProcurar, gbc);


        gbc.gridy = 1; gbc.gridx = 0;
        panelControles.add(new JLabel("Tipo de mutação:"), gbc);
        gbc.gridx = 1;
        comboRepresentacao = new JComboBox<>(TipoDeMutacao.values());
        panelControles.add(comboRepresentacao, gbc);


        gbc.gridy = 2; gbc.gridx = 0;
        panelControles.add(new JLabel("Algoritmo:"), gbc);
        gbc.gridx = 1;
        comboAlgoritmo = new JComboBox<>(AlgoritmoParaTestar.values());
        panelControles.add(comboAlgoritmo, gbc);


        gbc.gridy = 3; gbc.gridx = 0;
        panelControles.add(new JLabel("Pop. / Gerações / Taxa Mut. (%):"), gbc);
        
        JPanel panelParametros = new JPanel(new GridLayout(1, 3, 5, 0));
        txtPopulacao = new JTextField("200");
        txtGeracao = new JTextField("1000");
        txtTaxaMutacao = new JTextField("38"); // <--- INICIALIZADO COM 38
        panelParametros.add(txtPopulacao);
        panelParametros.add(txtGeracao);
        panelParametros.add(txtTaxaMutacao);
        gbc.gridx = 1;
        panelControles.add(panelParametros, gbc);

        // Linha 4: Botão Executar
        gbc.gridy = 4; gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        btnExecutar = new JButton("EXECUTAR ALGORITMO"); 
        btnExecutar.setFont(new Font("Arial", Font.BOLD, 14));
        panelControles.add(btnExecutar, gbc);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(areaLog); 

        add(panelControles, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);

        btnProcurar.addActionListener(e -> onProcurarArquivo());
        btnExecutar.addActionListener(e -> onExecutarTesteSelecionado()); 
        comboAlgoritmo.addActionListener(e -> onAlgoritmoSelecionado());

        onAlgoritmoSelecionado();
        setVisible(true);
    }

    private void onProcurarArquivo() {
        int retorno = fileChooser.showOpenDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            txtCaminhoArquivo.setText(arquivo.getAbsolutePath());
            log("Arquivo selecionado: " + arquivo.getName());
        }
    }

    private void onAlgoritmoSelecionado() {
        AlgoritmoParaTestar selecionado = (AlgoritmoParaTestar) comboAlgoritmo.getSelectedItem();
        boolean habilitar = (selecionado == AlgoritmoParaTestar.PCV_EXATO || selecionado == AlgoritmoParaTestar.PCV_GENETICO);
        txtPopulacao.setEnabled(habilitar);
        txtGeracao.setEnabled(habilitar);
        txtTaxaMutacao.setEnabled(habilitar); // <--- HABILITA/DESABILITA
    }

    private void onExecutarTesteSelecionado() {
        areaLog.setText("");
        String path = txtCaminhoArquivo.getText();
        TipoDeMutacao tipo = (TipoDeMutacao) comboRepresentacao.getSelectedItem();
        AlgoritmoParaTestar algoritmo = (AlgoritmoParaTestar) comboAlgoritmo.getSelectedItem();

        if (path.isEmpty()) {
            log("!!! ERRO: Selecione um arquivo de grafo.");
            return;
        }

        try {

            log(">>> Carregando grafo... <<<");
            Algoritmos leitorDeArquivos = new Algoritmos(); 
            Grafo grafoLista = leitorDeArquivos.carregarGrafo(path, TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            leitorDeArquivos = new Algoritmos(); 
            Grafo grafoMatriz = leitorDeArquivos.carregarGrafo(path, TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA);
            log ("Grafo carregado com sucesso");
            log("por favor, reinicie a execução caso queira usar outro arquivo de texto!!!");
            int TamanhoPopulacao = Integer.parseInt(txtPopulacao.getText());
            int NumeroDeGeracoes = Integer.parseInt(txtGeracao.getText());
            int TaxaMutacao = Integer.parseInt(txtTaxaMutacao.getText()); 

            log("\n[INICIANDO ALGORITMO: " + algoritmo + "]");

            switch (algoritmo) {
                case PCV_EXATO:
                    
                    Genetico geneticoExato = new Genetico(TamanhoPopulacao, NumeroDeGeracoes, TaxaMutacao, tipo);
                    Double custoGenetico = Double.MAX_VALUE;
                    ArrayList<Integer> caminhoExato = new ArrayList<>();
                    
                    if(grafoLista.numeroDeVertices() > 20){

                        for(int i = 0; i < 30; i++){

                            geneticoExato.CalcularGenetico(grafoLista);
                            if (geneticoExato.GetMelhorCaminho() < custoGenetico)
                                custoGenetico = geneticoExato.GetMelhorCaminho();
                        }
                    } else{

                        geneticoExato.CalcularGenetico(grafoLista);
                        custoGenetico = geneticoExato.GetMelhorCaminho();
                    }
                    log ("Começando a busca exata com um teto de: " + custoGenetico);
                    log ("");
                    
                    Exato exato = new Exato();
                    long inicioExec = System.currentTimeMillis();
                    
                    exato.CalcularExato(grafoMatriz, custoGenetico);
                    caminhoExato = exato.getCaminho(); 
                    if (caminhoExato.isEmpty()){

                        caminhoExato = geneticoExato.getCaminho();
                    }
                    String linhaExato = caminhoExato.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" - "));

                    log("Custo do caminho exato: " + exato.getMelhorCusto());
                    log("Caminho encontrado: " + linhaExato);
                    log("Tempo de execução(ms): " + (System.currentTimeMillis() - inicioExec));
                break;

                case PCV_GENETICO:
                    Genetico genetico = new Genetico(TamanhoPopulacao, NumeroDeGeracoes, TaxaMutacao, tipo);
                    genetico.CalcularGenetico(grafoLista);
                    ArrayList<Integer> caminho = new ArrayList<>();

                     Double melhorCaminhoGenetico = genetico.GetMelhorCaminho();

                    caminho = genetico.getCaminho();

                    long inicioExecucao2 = System.currentTimeMillis();

                    String linha = caminho.stream()

                    .map(String::valueOf)

                    .collect(Collectors.joining(" - "));


                    log("Custo do caminho genético: " + melhorCaminhoGenetico);

                    log("Caminho encontrado " + linha);

                    log("Tempo gasto (ms): " + (System.currentTimeMillis() - inicioExecucao2)); 
                    log("");

                break;
                
                case BASICOS:
                    log("Vértices: " + grafoLista.numeroDeVertices());
                break;
            }

        } catch (Exception e) {
            log("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void log(String mensagem) {
        areaLog.append(mensagem + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}