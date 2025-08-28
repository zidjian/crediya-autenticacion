package co.com.crediya.model.usuario;

import lombok.Data;

import java.math.BigDecimal;

public class Usuario {
    private final Long idUsuario;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String documentoIdentidad;
    private final String telefono;
    private final Long idRol;
    private final BigDecimal salarioBase;

    private Usuario(Long idUsuario, String nombre, String apellido, String email, String documentoIdentidad, String telefono, Long idRol, BigDecimal salarioBase) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (apellido == null || apellido.isBlank()) throw new IllegalArgumentException("El apellido es obligatorio");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("El email es obligatorio");
        if (salarioBase == null) throw new IllegalArgumentException("El salarioBase es obligatorio");

        this.idUsuario = idUsuario;
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.email = email.trim();
        this.documentoIdentidad = documentoIdentidad == null ? null : documentoIdentidad.trim();
        this.telefono = telefono == null ? null : telefono.trim();
        this.idRol = idRol;
        this.salarioBase = salarioBase;
    }

    public static Usuario crear(String nombre, String apellido, String email, String documentoIdentidad, String telefono, Long idRol, BigDecimal salarioBase) {
        return new Usuario(null, nombre, apellido, email, documentoIdentidad, telefono, idRol, salarioBase);
    }

    public Usuario conId(Long idUsuario) {
        return new Usuario(idUsuario, nombre, apellido, email, documentoIdentidad, telefono, idRol, salarioBase);
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public Long getIdRol() {
        return idRol;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }
}
