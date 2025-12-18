package com.genrisk.sistema.model.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.Data;

@Entity
@Table(name = "miembro_equipo")
@Data
public class MiembroEquipo implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miembro")
    private Integer idMiembroEquipo;

    @Column(name = "nombre_miembro")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreMiembro;

    @Column(name = "correo_miembro")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoMiembro;

    @Column(name = "clave")
    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    private String clave;

    @Column(name = "rol_miembro")
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(Investigador|Administrador|Reclutador|Informatico|Medico|Digitador)$", 
             message = "Rol inválido. Roles permitidos: Investigador, Administrador, Reclutador, Informatico, Medico")
    private String rolMiembro;

    public void setClave(String clavePlana) {
        this.clave = new BCryptPasswordEncoder().encode(clavePlana);
    }

    // ========= MÉTODOS RELACIONADOS A LA ENCRIPTACIÓN DE CLAVES ================ 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rolMiembro.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return this.clave;
    }

    @Override
    public String getUsername() {
        return this.correoMiembro;
    }

    @Override public boolean isAccountNonExpired() { 
        return true; 
    }
    
    @Override public boolean isAccountNonLocked() { 
        return true; 
    }

    @Override public boolean isCredentialsNonExpired() 
    { 
        return true; 
    }
    @Override public boolean isEnabled() 
    { 
        return true; 
    }
}
