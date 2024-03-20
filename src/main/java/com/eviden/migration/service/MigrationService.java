package com.eviden.migration.service;

import com.eviden.migration.model.drupal.DrupalUsuario;
import com.eviden.migration.model.magento.MagentoAuthToken;
import com.eviden.migration.model.magento.MagentoUsuario;
import com.eviden.migration.model.magento.MagentoUsuarioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class MigrationService {
    private final DrupalUsuarioService drupalUsuarioService;
    private  final MagentoService magentoService;

    public MigrationService(DrupalUsuarioService drupalUsuarioService,
                            MagentoService magentoService) {
        this.drupalUsuarioService = drupalUsuarioService;
        this.magentoService = magentoService;
    }

     private Mono<MagentoAuthToken> autenticarUsuarioMagento(){
        return magentoService.autenticarUsuario();
    }

    public void migracionUsuario() {
        //magento: autenticar usuario
        autenticarUsuarioMagento().block();
        //drupal: obtener usuarios
        List<DrupalUsuario> drupalUsuarios = obtenerUsuarioDrupal();
        //drupal: iterar sobre los productos
        Flux.fromIterable(drupalUsuarios)
                .flatMapSequential(drupalUsuario -> {
                    return insertarUsuario(drupalUsuario);
                })
                .subscribe(null,
                        error -> {}
                        ,() -> {
                            log.info("Migracion finalizada");
                        });
    }

    private List<DrupalUsuario> obtenerUsuarioDrupal() {
        return drupalUsuarioService.importarUsuariosDrupalDesdeCsv();
    }

    private Mono<MagentoUsuarioResponse> insertarUsuario(DrupalUsuario usuarioCsv){
        //insertar producto en magento
        return magentoService.insertarUsuario(usuarioCsv)
                .doOnSuccess(magentoProducto -> {
                    log.info("Magento: Usuario Response '{}'", magentoProducto.getEmail());
                });
    }
}
