package model;

public class Receita {
    private int id;
    private String paciente;
    private String medicamento;
    private String dataPrescricao;
    private boolean validada;

    // Construtor com validação
    public Receita(int id, String paciente, String medicamento, String dataPrescricao) {
        if (paciente == null || paciente.isEmpty() || medicamento == null || medicamento.isEmpty() || dataPrescricao == null || dataPrescricao.isEmpty()) {
            throw new IllegalArgumentException("Paciente, medicamento e data de prescrição são obrigatórios.");
        }
        this.id = id;
        this.paciente = paciente;
        this.medicamento = medicamento;
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

    public String getMedicamento() {
        return medicamento;
    }

    public String getDataPrescricao() {
        return dataPrescricao;
    }

    public boolean isValidada() {
        return validada;
    }

    // Método toString para exibição
    @Override
    public String toString() {
        return "Receita ID: " + id +
                ", Paciente: " + paciente +
                ", Medicamento: " + medicamento +
                ", Data: " + dataPrescricao +
                ", Validada: " + (validada ? "Sim" : "Não");
    }
}
