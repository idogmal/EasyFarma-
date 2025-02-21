package util;

import model.Receita;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportadorReceitas {

    /**
     * Exporta a lista de receitas para um arquivo CSV utilizando ponto e vírgula como seprador.
     * Inclui o BOM para UTF-8 para que o Excel interprete os acentos corretamente.
     *
     * @param receitas     Lista de receitas a serem exportadas.
     * @param caminhoArquivo Caminho (e nome) do arquivo CSV a ser gerado.
     */
    public static void exportarReceitasCSV(List<Receita> receitas, String caminhoArquivo) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            // Escreve o BOM para UTF-8
            writer.write("\uFEFF");
            // Cabeçalho usando ponto e vírgula como delimitador
            writer.write("ID;Paciente;CPF;DataPrescricao;Status;Medicamentos\n");

            // Para cada receita, escreve os dados separados por ponto e vírgula
            for (Receita r : receitas) {
                writer.write(String.valueOf(r.getId()));
                writer.write(";");
                writer.write(r.getPaciente());
                writer.write(";");
                writer.write(r.getCpf() != null ? r.getCpf() : "");
                writer.write(";");
                writer.write(r.getDataPrescricao());
                writer.write(";");
                writer.write(r.getStatus());
                writer.write(";");
                writer.write(r.getMedicamentosAsString());
                writer.write("\n");
            }
            writer.flush();
            System.out.println("Exportação concluída: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar receitas: " + e.getMessage());
        }
    }
}
