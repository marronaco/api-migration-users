package com.eviden.migration.utils;

import com.eviden.migration.model.drupal.DrupalUsuario;
import com.eviden.migration.model.magento.MagentoUsuario;
import com.eviden.migration.model.magento.MagentoUsuario.Addresses;
import com.eviden.migration.model.magento.MagentoUsuario.Customer;
import com.eviden.migration.model.magento.MagentoUsuario.ExtensionAttributes;
import com.eviden.migration.model.magento.MagentoUsuario.Region;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.List.of;

@Slf4j
public class MagentoUsuarioMapper {
    private static List<String> passwords = new ArrayList<>();
    private static final String CARACTERES_ESPECIALES = "!@#$%^&*()-_=+";

    public static MagentoUsuario mapDrupalUsuarioToMagento(DrupalUsuario usuario){
        log.info("Mapper: iniciando mapeo del usuario: {}", usuario.getNombre());
        return  MagentoUsuario.builder()
                .customer(mapCustomerToMagento(usuario))
                .password(generarPassword())
                .build();
    }

    private static Customer mapCustomerToMagento(DrupalUsuario usuario) {
        return Customer.builder()
                .groupId(mapRoleToMagento(usuario.getRol()))
                .email(usuario.getEmail())
                .firstname(usuario.getNombre())
                .lastname(usuario.getApellidos())
                .gender(3) //ID 3: NOT SPECIFIED
                .storeId(1)
                .websiteId(1)
                .addresses(of(mapAddressesToMagento(usuario.getDireccion1(), usuario.getDireccion2(),
                        usuario.getTelefono(), usuario.getCodigoPostal(),
                        usuario.getCiudad(), usuario.getNombre(),
                        usuario.getApellidos(), usuario.getProvincia(),
                        usuario.getCodigoProvincia())))
                .disableAutoGroupChange(0)
                .extensionAttributes(mapExtensionAttributesToMagento())
                .build();
    }

    private static int mapRoleToMagento(String rol) {
        log.info("Mapper: Rol");
        //En magento 1 = General 1 | 4 = UsuarioVIP
        int rolFinal = 1;
        //comprobar el tipo de rol de usario del csv
        if(rol.equals("UsuarioVIP")){
            rolFinal = 4;
        }
        return rolFinal;
    }

    private static Addresses mapAddressesToMagento(String direccion1, String direccion2,
                                                   String telefono, String codigoPostal,
                                                   String ciudad, String nombre,
                                                   String apellidos, String provincia,
                                                   String codProvincia) {
        log.info("Mapper: Adresses");
        return Addresses.builder()
                .id(1)
                .customerId(2)
                .region(mapRegionToMagento(codProvincia, provincia))
                .regionId(0)
                .countryId("ES")
                .street(of(direccion1, direccion2))
                .telephone(telefono)
                .postcode(codigoPostal)
                .city(ciudad)
                .firstname(nombre)
                .lastname(apellidos)
                .defaultShipping(true)
                .defaultBilling(true)
                .build();

    }

    private static Region mapRegionToMagento(String codProvincia, String provincia) {
        log.info("Mapper: Region");
        return Region.builder()
                .regionCode(codProvincia)
                .region(provincia)
                .regionId(0)
                .build();
    }

    private static ExtensionAttributes mapExtensionAttributesToMagento() {
        log.info("Mapper: Extension attributes");
        return ExtensionAttributes.builder()
                .isSubscribed(false)
                .build();
    }


    private static String generarPassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Generar la primera letra mayúscula
        char letraMayuscula = (char) (random.nextInt(26) + 'A');
        sb.append(letraMayuscula);

        // Generar 8 números
        for (int i = 0; i < 8; i++) {
            int digito = random.nextInt(10);
            sb.append(digito);
        }

        // Generar un carácter especial aleatorio
        char caracterEspecial = CARACTERES_ESPECIALES.charAt(random.nextInt(CARACTERES_ESPECIALES.length()));
        sb.append(caracterEspecial);

        return sb.toString();
    }

}
