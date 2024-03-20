package com.eviden.migration.model.drupal;


import lombok.*;

/**
 * En esta clase se definen los atributos
 * establecidos la columnas del CSV
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class DrupalUsuario {
    //atributos del CSV
    private String uid;
    private String rol;
    private String email;
    private String nombre;
    private String apellidos;
    private String direccion1;
    private String direccion2;
    private String codigoPostal;
    private String ciudad;
    private String provincia;
    private String codigoProvincia;
    private String telefono;
}
