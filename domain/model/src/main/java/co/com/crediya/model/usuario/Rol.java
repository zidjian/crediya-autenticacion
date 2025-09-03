package co.com.crediya.model.usuario;

public class Rol {
    private final Long idRol;
    private final String nombre;
    private final String descripcion;

    private Rol(Long idRol, String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");

        this.idRol = idRol;
        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
    }

    public static Rol toRol(Long idRol, String nombre, String descripcion) {
        return new Rol(idRol, nombre, descripcion);
    }

    public Long getIdRol() {
        return idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
