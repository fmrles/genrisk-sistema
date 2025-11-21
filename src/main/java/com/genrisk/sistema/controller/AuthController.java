package com.genrisk.sistema.controller;

import com.genrisk.sistema.model.dto.LoginRequest;
import com.genrisk.sistema.model.entity.MiembroEquipo;
import com.genrisk.sistema.repository.MiembroEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Permite que tu front-end (HTML) llame a esta API
public class AuthController {

    @Autowired
    private MiembroEquipoRepository miembroEquipoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // --- INICIO DE DEPURACIÓN ---
        // Limpiamos los datos que llegan del front-end para evitar errores de espacios
        String correoFront = loginRequest.getCorreo().trim();
        String claveFront = loginRequest.getClave().trim();

        System.out.println("--- INTENTO DE LOGIN RECIBIDO ---");
        System.out.println("Correo del Front: [" + correoFront + "]");
        System.out.println("Clave del Front:  [" + claveFront + "]");
        // --- FIN DE DEPURACIÓN ---

        // PASO 1: Buscar al usuario en la BD por su correo
        Optional<MiembroEquipo> miembroOpt = miembroEquipoRepository.findByCorreoMiembro(correoFront);

        // Verificación PASO 1
        if (miembroOpt.isEmpty()) {
            System.out.println("RESULTADO: Usuario NO ENCONTRADO en la BD.");
            System.out.println("-------------------------------------");
            // Devolvemos 401 (No autorizado)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo no encontrado.");
        }

        // Si llegamos aquí, el usuario SÍ fue encontrado.
        MiembroEquipo miembroEncontrado = miembroOpt.get();
        String claveBD = miembroEncontrado.getClave().trim(); // Limpiamos la clave de la BD

        System.out.println("Usuario Encontrado: " + miembroEncontrado.getNombreMiembro());
        System.out.println("Clave de la BD:     [" + claveBD + "]");


        // PASO 2: Comparar las contraseñas (la del front vs. la de la BD)
        if (claveFront.equals(claveBD)) {
            // ¡ÉXITO! Las claves coinciden.
            System.out.println("RESULTADO: ¡LOGIN EXITOSO!");
            System.out.println("-------------------------------------");

            miembroEncontrado.setClave(null); // Borramos la clave antes de enviarla
            return ResponseEntity.ok(miembroEncontrado); // Devolvemos 200 OK

        } else {
            // FALLO. Las claves NO coinciden.
            System.out.println("RESULTADO: Contraseña INCORRECTA.");
            System.out.println("-------------------------------------");

            // Devolvemos 401 (No autorizado)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta.");
        }
    }
}