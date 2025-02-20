package model;

import java.util.HashMap;
import java.util.Map;

public class Receita {
    private int id;
    private String paciente;
    private String cpf;
    private Map<String, Integer> medicamentos;
    private String dataPrescricao;
    private boolean validada;

    // Construtor atualizado
    public Receita(int id, String paciente, String cpf, Map<String, Integer> medicamentos, String dataPrescricao) {
        if (paciente == null || paciente.isEmpty() ||
                cpf == null || cpf.isEmpty() ||
                medicamentos == null || medicamentos.isEmpty() ||
                dataPrescricao == null || dataPrescricao.isEmpty()) {
            throw new IllegalArgumentException("Paciente, CPF, medicamentos e data de prescrição são obrigatórios.");
        }
        this.id = id;
        this.paciente = paciente;
        this.cpf = cpf;
        this.medicamentos = new HashMap<>(medicamentos);
        this.dataPrescricao = dataPrescricao;
        this.validada = false;
    }

    // Construtor sem argumentos para desserialização do JSON
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
        if (medicamentos == null) {
            medicamentos = new HashMap<>();
        }
        return medicamentos;
    }

    public void setMedicamentos(Map<String, Integer> medicamentos) {
        this.medicamentos = (medicamentos != null) ? new HashMap<>(medicamentos) : new HashMap<>();
    }

    public String getDataPrescricao() {
        return dataPrescricao;
    }

    public boolean isValidada() {
        return validada;
    }

    // Novo getter que retorna o status da receita como String
    public String getStatus() {
        return validada ? "Validada" : "Pendente";
    }

    // Novo setter para atualizar o status da receita
    public void setStatus(String status) {
        this.validada = "Validada".equalsIgnoreCase(status);
    }

    // Método para converter o mapa de medicamentos em uma String legível
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
