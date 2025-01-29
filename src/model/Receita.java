package model;

import java.util.HashMap;
import java.util.Map;

public class Receita {
    private int id;
    private String paciente;
    private String cpf; // Adicionado CPF do paciente
    private Map<String, Integer> medicamentos; // Nome do medicamento e quantidade
    private String dataPrescricao;
    private boolean validada;

    // Construtor atualizado
    public Receita(int id, String paciente, String cpf, Map<String, Integer> medicamentos, String dataPrescricao) {
        if (paciente == null || paciente.isEmpty() || cpf == null || cpf.isEmpty() || medicamentos == null || medicamentos.isEmpty() || dataPrescricao == null || dataPrescricao.isEmpty()) {
            throw new IllegalArgumentException("Paciente, CPF, medicamentos e data de prescrição são obrigatórios.");
        }
        this.id = id;
        this.paciente = paciente;
        this.cpf = cpf; // Inicializando CPF
        this.medicamentos = new HashMap<>(medicamentos); // Garante uma cópia do mapa para evitar modificações externas
        this.dataPrescricao = dataPrescricao;
        this.validada = false; // Começa como não validada
    }

    // Método para validar a receita
    public void validar() {
        this.validada = true;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getCpf() {
        return cpf;
    }

    public Map<String, Integer> getMedicamentos() {
        return medicamentos;
    }

    public String getDataPrescricao() {
        return dataPrescricao;
    }

    public boolean isValidada() {
        return validada;
    }

    // Método para obter os medicamentos como uma String
    public String getMedicamentosAsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> medicamento : medicamentos.entrySet()) {
            sb.append(medicamento.getKey())
                    .append(" (")
                    .append(medicamento.getValue())
                    .append(" unidades), ");
        }
        // Remove a última vírgula e espaço, se existir
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    // Método toString para exibição detalhada
    @Override
    public String toString() {
        return "Receita ID: " + id +
                "\nPaciente: " + paciente +
                "\nCPF: " + cpf +
                "\nData da Prescrição: " + dataPrescricao +
                "\nMedicamentos: " + getMedicamentosAsString() +
                "\nValidada: " + (validada ? "Sim" : "Não");
    }
}
