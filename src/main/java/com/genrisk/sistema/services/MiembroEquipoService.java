package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.repository.*;
import com.genrisk.sistema.model.dto.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MiembroEquipoService extends CrudServices<MiembroEquipo, Integer> {

    private MiembroEquipoRepository miembroEquipoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RecuperacionClaveRepository recuperacionClave;

    @Autowired 
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public MiembroEquipoService(MiembroEquipoRepository repo, PasswordEncoder passwordEncoder) {
        super(repo);
        this.miembroEquipoRepository = repo;
        this.passwordEncoder = passwordEncoder;
    }
    

    public Optional<MiembroEquipo> login(LoginRequest loginRequest) {
        Optional<MiembroEquipo> miembroOpt = miembroEquipoRepository.findByCorreoMiembro(loginRequest.getCorreo());

        if (miembroOpt.isEmpty()) {
            return Optional.empty();
        }

        MiembroEquipo miembro = miembroOpt.get();

        if (passwordEncoder.matches(loginRequest.getClave(), miembro.getClave())) {
            return Optional.of(miembro);
        } else {
            return Optional.empty();
        }
    }

    public MiembroEquipo crearNuevoMiembro(MiembroEquipo nuevoMiembroEquipo){
        if(nuevoMiembroEquipo.getClave() != null){
            if(!nuevoMiembroEquipo.getClave().startsWith("$2a$") && !nuevoMiembroEquipo.getClave().startsWith("$2y$")){
                nuevoMiembroEquipo.setClave(nuevoMiembroEquipo.getClave());
            }
        }
        return miembroEquipoRepository.save(nuevoMiembroEquipo);
    }

    public void cambiarClave(MiembroEquipo miembro, String nuevaClave) {
        miembro.setClave(nuevaClave); // hasheo de forma automática
        miembroEquipoRepository.save(miembro);
    }

    public MiembroEquipo dtoToEntity(MiembroEquipoDTO dto) {    
        MiembroEquipo miembro = new MiembroEquipo();
        
        if (dto.nombreMiembro() != null && !dto.nombreMiembro().isBlank()) {
            miembro.setNombreMiembro(dto.nombreMiembro());
        }
        if (dto.correoMiembro() != null && !dto.correoMiembro().isBlank()) {
            miembro.setCorreoMiembro(dto.correoMiembro());
        }
        if (dto.rolMiembro() != null && !dto.rolMiembro().isBlank()) {
            miembro.setRolMiembro(dto.rolMiembro());
        }

        if (dto.clave() != null && !dto.clave().isBlank()) {
            miembro.setClave(dto.clave()); 
        }
        return miembro; 
    }

    // MÉTODOS RELACIONADOS CON LA RECUPERACIÓN DE CLAVE

    @Transactional
    public void solicitarNuevaClave(String correoUsuario){

        //Validar de que el domimio del correo sea gmail u outlook
        if(!correoUsuario.matches("^[\\w.-]+@(gmail\\.com|outlook\\.com)$")){
            throw new IllegalArgumentException("Lo sentimos, este sistema solo permite correos gmail u outlook");
        }

        Optional <MiembroEquipo> miembroBusqueda = miembroEquipoRepository.findByCorreoMiembro(correoUsuario); 
        if(miembroBusqueda.isEmpty()){ // Si no se encuentra el correo, se retorna null
            return;
        }

        MiembroEquipo miembro = miembroBusqueda.get();

        String token = UUID.randomUUID().toString();
        RecuperacionClave recuperacion = new RecuperacionClave(null, token, LocalDateTime.now().plusHours(1), miembro);
        SimpleMailMessage mensajeMostrar = new SimpleMailMessage();

        recuperacionClave.save(recuperacion);
        mensajeMostrar.setFrom("genrisksistema@gmail.com");
        mensajeMostrar.setTo(correoUsuario);
        mensajeMostrar.setSubject("Recuperación de Contraseña - GenRisk System");
        mensajeMostrar.setText("""
            Hola %s,
            
            Se ha solicitado la recuperación de su contraseña en la plataforma GenRisk.

            Haga clic en el siguiente enlace para crear una nueva contraseña:
            %s/contrasenia?token=%s

            El enlace tiene validez de 1 hora.

            Si no solicitó este cambio, ignore este mensaje.

            Equipo GenRisk
            """.formatted(miembro.getNombreMiembro(), frontendUrl, token));

        mailSender.send(mensajeMostrar);
    }

    @Transactional
    public void restablecerClave(String token, String nuevaClave) {
        RecuperacionClave recuperacion = recuperacionClave
            .findByClaveMomentanea(token)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        if (recuperacion.claveExpirada()) {
            recuperacionClave.delete(recuperacion);
            throw new IllegalArgumentException("El enlace ha expirado");
        }

        MiembroEquipo miembro = recuperacion.getMiembro();
        miembro.setClave(nuevaClave);
        miembroEquipoRepository.save(miembro);

        recuperacionClave.delete(recuperacion); // token de un solo uso
    }
}

