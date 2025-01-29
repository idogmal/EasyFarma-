package model;

import java.util.HashMap;
import java.util.Map;

public class Receita {
    private int id;
    private String paciente;
    private String cpf; // CPF do paciente
    private Map<String, Integer> medicamentos; // Nome do medicamento e quantidade
    private String dataPrescricao;
    private boolean validada;

    // Construtor completo
    public Receita(int id, String paciente, String cpf, Map<String, Integer> medicamentos, String dataPrescricao) {
        if (paciente == null || paciente.isEmpty() || cpf == null || cpf.isEmpty() || medicamentos == null || medicamentos.isEmpty() || dataPrescricao == null || dataPrescricao.isEmpty()) {
            throw new IllegalArgumentException("Paciente, CPF, medicamentos e data de prescrição são obrigatórios.");
        }
        this.id = id;
        this.paciente = paciente;
        this.cpf = cpf;
        this.medicamentos = new HashMap<>(medicamentos); // Copia os medicamentos
        this.dataPrescricao = dataPrescricao;
        this.validada = false;
    }

    // Construtor vazio para desserialização do JSON
    public Receita() {
        this.medicamentos = new HashMap<>();
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
        return medicamentos == null ? new HashMap<>() : medicamentos;
    }

    public String getDataPrescricao() {
        return dataPrescricao;
    }

    public boolean isValidada() {
        return validada;
    }

    // Setters (necessários para desserialização do JSON)
    public void setId(int id) {
        this.id = id;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setMedicamentos(Map<String, Integer> medicamentos) {
        this.medicamentos = medicamentos != null ? new HashMap<>(medicamentos) : new HashMap<>();
    }

    public void setDataPrescricao(String dataPrescricao) {
        this.dataPrescricao = dataPrescricao;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }

    // Método para obter os medicamentos como uma String formatada
    public String getMedicamentosAsString() {
        if (medicamentos == null || medicamentos.isEmpty()) {
            return "Nenhum medicamento cadastrado.";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> medicamento : medicamentos.entrySet()) {
            sb.append(medicamento.getKey())
                    .append(" (")
                    .append(medicamento.getValue())
                    .append(" unidades), ");
        }

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
                "\nValidada: " + (validada ? "Sim" : "Não") +
                "\n-------------------------";
    }
}
