package com.eviden.migration.service;

import com.eviden.migration.model.drupal.DrupalUsuario;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DrupalUsuarioService {
    public List<DrupalUsuario> importarUsuariosDrupalDesdeCsv() {
        List<DrupalUsuario> usuarios = new ArrayList<>();

        try {
            //Lectura del fichero csv
            String rutaEscritorio = System.getenv("CSV_DIRECTORY");
            String rutaPath = Paths.get(rutaEscritorio,"users.csv").toString();
            log.info("Drupal: Lectura del CSV '{}'", rutaPath);
            //Lectura del fichero csv
            CSVReader csvReader = new CSVReader(new FileReader(rutaPath));
            String[] linea;
            //salto la primera linea
            csvReader.readNext();
            while ((linea = csvReader.readNext()) != null) {
                DrupalUsuario usuario = mapToUsuarioDrupalCsv(linea);
                usuarios.add(usuario);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        log.info("Total de usuarios a insertar '{}'", usuarios.size());
        return usuarios;
    }

    private DrupalUsuario mapToUsuarioDrupalCsv(String[] linea) {
        log.info("Drupal: usuario email '{}'", linea[2]);
        return DrupalUsuario.builder()
                .uid(linea[0])
                .rol(linea[1])
                .email(linea[2])
                .nombre(linea[3])
                .apellidos(linea[4])
                .direccion1(linea[5])
                .direccion2(linea[6])
                .codigoPostal(linea[7])
                .ciudad(linea[8])
                .provincia(linea[9])
                .codigoProvincia(linea[10])
                .telefono(linea[11])
                .build();
    }
}
