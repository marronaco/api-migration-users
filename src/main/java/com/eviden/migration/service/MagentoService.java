package com.eviden.migration.service;

import com.eviden.migration.exceptions.AuthenticationFailedException;
import com.eviden.migration.exceptions.ResourceNotFoundException;
import com.eviden.migration.model.drupal.DrupalUsuario;
import com.eviden.migration.model.magento.MagentoAuthRequest;
import com.eviden.migration.model.magento.MagentoAuthToken;
import com.eviden.migration.model.magento.MagentoUsuario;
import com.eviden.migration.model.magento.MagentoUsuarioResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.eviden.migration.utils.MagentoUsuarioMapper.mapDrupalUsuarioToMagento;

@Slf4j
@Service
public class MagentoService {
    private final WebClient magentoWebClient;
    private static String  authToken;

    public MagentoService(WebClient magentoWebClient) {
        this.magentoWebClient = magentoWebClient;
    }

    /**
     * Autenticacion de usuario, obtener token
     * @return token
     */
    public Mono<MagentoAuthToken> autenticarUsuario(){
        log.info("Magento: autenticacion del usuario... obteniendo token");
        //definir credenciales del usuario y contraseña
        MagentoAuthRequest authRequest = MagentoAuthRequest.builder()
                .username("DrupalAdmin")
                .password("B1llMurr@y")
                .build();

        //almacenar el token devuelto de la solicitud
        return magentoWebClient.post()
                .uri("/integration/admin/token")
                .body(Mono.just(authRequest), MagentoAuthRequest.class)
                .retrieve()
                .bodyToMono(MagentoAuthToken.class)
                .doOnSuccess(magentoAuthToken -> {
                    log.info("Magento: token recibido almacenado");
                    // Almacenar el token obtenido en la variable
                    authToken = magentoAuthToken.getToken();
                });
    }

    public Mono<MagentoUsuarioResponse> insertarUsuario(DrupalUsuario usuarioCsv){
        //comprobar si el token es nulo
        if(authToken == null){
            throw new AuthenticationFailedException("Magento: el token es nulo, usuario y contraseña no auntenticado", HttpStatus.BAD_REQUEST);
        }
        //mapear usarioCsv a UsuarioMagento
        MagentoUsuario usuario = mapDrupalUsuarioToMagento(usuarioCsv);
        log.info("Magento: usuario '{}' password '{}'", usuario.getCustomer().getEmail(), usuario.getPassword());
        //enviar peticion HTTP a la API Magento
        log.info("Magento: Enviando peticion a la API Magento del usuario '{}'", usuarioCsv.getEmail());
        return magentoWebClient.post()
                .uri("/customers")
                .header("Authorization", "Bearer %s".formatted(authToken))
                .body(Mono.just(usuario), MagentoUsuario.class)
                .retrieve()
                .bodyToMono(MagentoUsuarioResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    //manejo detallado de la excepcion
                    HttpStatus status = ex.getStatusCode();
                    String response = ex.getResponseBodyAsString();
                    return Mono.error(
                            new ResourceNotFoundException("Error: '%s' en usuario '%s'  | Mensaje: '%s'"
                                    .formatted(status, usuario.getCustomer().getEmail(), response)));
                })
                .doOnError(error -> {
                    log.error("Error: '%s'".formatted(error));
                });
    }
}
