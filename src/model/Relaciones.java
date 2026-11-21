package model;

public class Relaciones {

    // ================= ATRIBUTOS =================
    private int id;
    private String nombre;

    // ================= CONSTRUCTORES =================
    public Relaciones() {}

    public Relaciones(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // ================= CLASIFICACIÓN POR CARGO =================
    public static Relaciones asignarPorCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            return new Relaciones(1, "Administración y Finanzas");
        }

        switch (cargo.trim().toLowerCase()) {
            case "gerente":
            case "contador":
            case "secretaria":
                return new Relaciones(1, "Administración y Finanzas");
            case "vendedor":
            case "cajero":
                return new Relaciones(2, "Ventas y Atención");
            case "técnico":
            case "tecnico":
            case "supervisor":
                return new Relaciones(3, "Operaciones y Tecnología");
            case "chofer":
            case "conserje":
            case "seguridad":
                return new Relaciones(4, "Servicios Generales");
            default:
                return new Relaciones(5, "General");
        }
    }

    // ================= GETTERS Y SETTERS =================
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // ================= REPRESENTACIÓN EN TEXTO =================
    @Override
    public String toString() {
        return nombre;
    }
}
