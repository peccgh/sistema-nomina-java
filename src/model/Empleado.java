package model;

public class Empleado {
    private String nombre;
    private String cedula;
    private String cargo;
    private double salarioBase;
    private double horasExtras;
    private double bonos;
    private boolean seguro;
    private Departamento departamento; // Nuevo atributo

    public Empleado() {
        // Departamento por defecto
        this.departamento = new Departamento(1, "Administración");
    }

    public Empleado(String nombre, String cedula, String cargo, double salarioBase, double horasExtras, double bonos, boolean seguro, Departamento departamento) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
        this.horasExtras = horasExtras;
        this.bonos = bonos;
        this.seguro = seguro;
        this.departamento = departamento != null ? departamento : new Departamento(1, "Administración");
    }

    // Constructor de compatibilidad (asigna departamento por defecto ID 1)
    public Empleado(String nombre, String cedula, String cargo, double salarioBase, double horasExtras, double bonos, boolean seguro) {
        this(nombre, cedula, cargo, salarioBase, horasExtras, bonos, seguro, new Departamento(1, "Administración"));
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    public double getHorasExtras() { return horasExtras; }
    public void setHorasExtras(double horasExtras) { this.horasExtras = horasExtras; }

    public double getBonos() { return bonos; }
    public void setBonos(double bonos) { this.bonos = bonos; }

    public boolean isSeguro() { return seguro; }
    public void setSeguro(boolean seguro) { this.seguro = seguro; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    public String aTextoCSV() {
        return String.join(";", nombre, cedula, cargo, String.valueOf(salarioBase), String.valueOf(horasExtras), String.valueOf(bonos), String.valueOf(seguro), departamento.getNombre());
    }
}
