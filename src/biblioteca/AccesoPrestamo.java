package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConfigSQLite;
import excepciones.BDException;

//TODO añadir excepciones con mensajes de error personalizados para cada caso

public class AccesoPrestamo {

	/**
	 * 
	 * @param codigo
	 * @param isbn
	 * @param titulo
	 * @param escritor
	 * @param publicacion
	 * @param puntuacion
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean insertarPrestamo(int codigo, String isbn, String titulo, String escritor, int publicacion,
			double puntuacion) throws BDException, SQLException {
    
		String query = "INSERT INTO prestamo VALUES ('?', '?', '?', '?', '?', '?')";

		ps = conexion.prepareStatement(query);
		ps.setInt(1, codigo);
		ps.setString(2, isbn);
		ps.setString(3, titulo);
		ps.setString(4, escritor);
		ps.setInt(5, publicacion);
		ps.setDouble(6, puntuacion);

		inserciones = ps.executeUpdate();
		ConfigSQLite.cerrarConexion(conexion);

		return inserciones > 0;
	}

	/**
	 * 
	 * @param fechaDevolucion
	 * @param codigoLibro
	 * @param codigoSocio
	 * @param fechaInicio
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean modificarPrestamo(String fechaDevolucion, int codigoLibro, int codigoSocio,
			String fechaInicio) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int modificaciones = 0;

		conexion = ConfigSQLite.abrirConexion();

		String query = "UPDATE prestamo SET fecha_devolucion = '?' "
				+ "WHERE codigo_libro = '?' AND codigo_socio = '?' " + "AND fecha_inicio = '?' ;";

		ps = conexion.prepareStatement(query);
		ps.setString(1, fechaDevolucion);
		ps.setInt(2, codigoLibro);
		ps.setInt(3, codigoSocio);
		ps.setString(4, fechaInicio);

		modificaciones = ps.executeUpdate();
		ConfigSQLite.cerrarConexion(conexion);

		return modificaciones > 0;
	}

	/**
	 * 
	 * @param codigoLibro
	 * @param codigoSocio
	 * @param fechaInicio
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean eliminarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio)
			throws BDException, SQLException {
		int modificaciones = 0;

		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "DELETE FROM prestamo WHERE codigo_libro = '?' "
				+ "AND codigo_socio = '?' AND fecha_inicio = '?';";

		PreparedStatement ps = conexion.prepareStatement(query);
		ps.setInt(1, codigoLibro);
		ps.setInt(2, codigoSocio);
		ps.setString(3, fechaInicio);

		modificaciones = ps.executeUpdate();
		ConfigSQLite.cerrarConexion(conexion);

		return modificaciones > 0;
	}

  public static ArrayList<Prestamo> consultarPrestamos() throws BDException, SQLException {
    Connection conexion = ConfigSQLite.abrirConexion();

    String query = "SELECT * FROM prestamo";
    
    Statement sentencia = conexion.createStatement();
    ResultSet resultados = sentencia.executeQuery(query);
    
    ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();
    

    //TODO probar si al obtener nulo en una consulta, se puede insertar NULL como String
    while (resultados.next()) {
      Libro libro = new Libro(resultados.getInt("codigo_libro"));
      Socio socio = new Socio(resultados.getInt("codigo_socio"))

      String fechaInicio = resultados.getString("fecha_inicio");
      String fechaFin = resultados.getString("fecha_fin");
      String fechaDevolucion = resultados.getString("fecha_devolucion");
      
      Prestamo prestamo = new Prestamo(libro, socio, fechaInicio, fechaFin, fechaDevolucion);

      prestamos.add(prestamo);
    }

    return prestamos;
  }

  public static ArrayList<> consultarPrestamosNoDevuletos() throws BDException, SQLException {
    Connection conexion = ConfigSQLite.abrirConexion();
    
    String query = "";

    Statement sentencia = conexion.createStatement();
    ResultSet resultados = sentencia.executeQuery(query);

    ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();

    while (resultados.next()) {
      Libro libro = new Libro(resultados.getInt("codigo_libro"));
      Socio socio = new Socio(resultados.getInt("codigo_socio"));
      Prestamo prestamo = new Prestamo();
    }
  } 
}
