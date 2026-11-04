package service;

import model.Empleado;
import model.Nomina;
import utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;

public class NominaService {

    private static final int ID_DEPARTAMENTO = 1;

    private final List<Empleado> empleados = new ArrayList<>();

    public NominaService() {
        cargarDesdeBaseDatos();
    }

    private void cargarDesdeBaseDatos() {
        String sql = "SELECT Nombre, Cedula, Cargo, Salario_Base, "
                   + "Horas_Extras, Bonos, Seguro FROM Empleados";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            empleados.clear();

            while (rs.next()) {
                Empleado emp = new Empleado(
                    rs.getString("Nombre"),
                    rs.getString("Cedula"),
                    rs.getString("Cargo"),
                    rs.getDouble("Salario_Base"),
                    rs.getDouble("Horas_Extras"),
                    rs.getDouble("Bonos"),
                    rs.getDouble("Seguro") > 0
                );

                empleados.add(emp);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudieron cargar los empleados desde Azure SQL.\n"
                + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void agregarEmpleado(Empleado emp) {
        String sql = "INSERT INTO Empleados "
                   + "(Id_Departamento, Nombre, Cedula, Cargo, "
                   + "Salario_Base, Horas_Extras, Bonos, Seguro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//modificado
        try (Connection con = ConexionBD.conectar();
     PreparedStatement ps = con.prepareStatement(
         sql, java.sql.Statement.RETURN_GENERATED_KEYS)) { 
                        con.setAutoCommit(false);
//hasta aqui
            ps.setInt(1, ID_DEPARTAMENTO);
            ps.setString(2, emp.getNombre());
            ps.setString(3, emp.getCedula());
            ps.setString(4, emp.getCargo());
            ps.setBigDecimal(5,
                java.math.BigDecimal.valueOf(emp.getSalarioBase()));
            ps.setBigDecimal(6,
                java.math.BigDecimal.valueOf(emp.getHorasExtras()));
            ps.setBigDecimal(7,
                java.math.BigDecimal.valueOf(emp.getBonos()));
//modificado
            ps.setBigDecimal(8,
    emp.isSeguro()
        ? java.math.BigDecimal.ONE
        : java.math.BigDecimal.ZERO);
//hasta aqui
//modificado
            ps.executeUpdate();

int idEmpleado;

try (ResultSet rs = ps.getGeneratedKeys()) {
    if (rs.next()) {
        idEmpleado = rs.getInt(1);
    } else {
        throw new SQLException("No se pudo obtener el ID del empleado.");
    }
}
//hasta aqui
//modificado
Nomina nomina = new Nomina(emp);

String sqlNomina = "INSERT INTO Detalle_Nomina "
        + "(Id_Empleado, Nombre_Empleado, Pago_hora_extras, "
        + "Salario_Bruto, Deduciones, Salario_neto) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

try (PreparedStatement psNomina = con.prepareStatement(sqlNomina)) {
    psNomina.setInt(1, idEmpleado);
    psNomina.setString(2, emp.getNombre());
    psNomina.setBigDecimal(3, java.math.BigDecimal.valueOf(nomina.calcularPagoHorasExtras()));
    psNomina.setBigDecimal(4, java.math.BigDecimal.valueOf(nomina.calcularSalarioBruto()));
    psNomina.setBigDecimal(5, java.math.BigDecimal.valueOf(nomina.calcularDeducciones()));
    psNomina.setBigDecimal(6, java.math.BigDecimal.valueOf(nomina.calcularSalarioNeto()));

    psNomina.executeUpdate();
    con.commit();
    empleados.add(emp);
} 
//hasta aqui
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudo registrar el empleado en Azure SQL.\n"
                + e.getMessage(),
                "Error al registrar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void eliminarEmpleadoPorCedula(String cedula) {
        String sql = "DELETE FROM Empleados WHERE Cedula = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedula);
            int filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                empleados.removeIf(
                    e -> Objects.equals(e.getCedula(), cedula)
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudo eliminar el empleado de Azure SQL.\n"
                + e.getMessage(),
                "Error al eliminar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public double calcularTotalNominaGeneral() {
        double total = 0;

        for (Empleado emp : empleados) {
            Nomina nomina = new Nomina(emp);
            total += nomina.calcularSalarioNeto();
        }

        return total;
    }
}
