package com.sobrevidas.crud_pacientes.service;

import com.sobrevidas.crud_pacientes.entity.Paciente;
import com.sobrevidas.crud_pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteImportService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PacienteImportService.class);
    private final PacienteRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("Banco de dados vazio. Iniciando importação do CSV...");
            try {
                importarCsvInterno("/pacientes.csv");
                log.info("Importação do CSV concluída com sucesso.");
            } catch (IOException e) {
                log.error("Falha na importação do CSV", e);
            }
        } else {
            log.info("O banco de dados já contém dados. Nenhuma importação é necessária.");
        }
    }

    public void importarCsvInterno(String caminhoArquivo) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(caminhoArquivo);
        if (inputStream == null) {
            throw new IOException("Arquivo não encontrado no classpath: " + caminhoArquivo);
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            List<Paciente> pacientes = new ArrayList<>();
            for (CSVRecord record : records) {
                Paciente paciente = Paciente.builder()
                        .cpf(record.get("cpf"))
                        .nome(record.get("nome"))
                        .nomeMae(record.get("nome_mae"))
                        .dataNascimento(LocalDate.parse(record.get("data_nascimento").substring(0, 10)))
                        .cep(record.get("cep"))
                        .endereco(record.get("endereco"))
                        .numEndereco(record.get("num_endereco"))
                        .complemento(record.get("complemento"))
                        .bairro(record.get("bairro"))
                        .cidade(record.get("cidade"))
                        .estado(record.get("estado"))
                        .telefoneCelular(record.get("telefone_celular"))
                        .telefoneResponsavel(record.get("telefone_responsavel"))
                        .email(record.get("email"))
                        .sexo(record.get("sexo"))
                        .numCartaoSus(record.get("num_cartao_sus"))
                        .ehTabagista(parseBoolean(record.get("eh_tabagista")))
                        .ehEtilista(parseBoolean(record.get("eh_etilista")))
                        .temLesaoSuspeita(parseBoolean(record.get("tem_lesao_suspeita")))
                        .participaSmartMonitor(parseBoolean(record.get("participa_smart_monitor")))
                        .build();
                pacientes.add(paciente);
            }
            repository.saveAll(pacientes);
        }
    }

    private Boolean parseBoolean(String value) {
        if (value == null) {
            return null;
        }

        if (value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }

        return value.equalsIgnoreCase("true");
    }
}

