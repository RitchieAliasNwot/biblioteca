package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConfigSQLite;
import excepciones.BDException;

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
	public static boolean insertarPrestamo(int codigo, String isbn, String titulo, String escritor, int publicacion, double puntuacion) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int inserciones = 0;
		
		conexion = ConfigSQLite.abrirConexion();
		
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
	public static boolean modificarPrestamo(String fechaDevolucion, int codigoLibro, int codigoSocio, String fechaInicio) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int modificaciones = 0;
		
		conexion = ConfigSQLite.abrirConexion();
		
		String query = "UPDATE prestamo SET fecha_devolucion = '?' "
				+ "WHERE codigo_libro = '?' AND codigo_socio = '?' "
				+ "AND fecha_inicio = '?' ;";
		
		ps = conexion.prepareStatement(query);
		ps.setString(1, fechaDevolucion);
		ps.setInt(2, codigoLibro);
		ps.setInt(3, codigoSocio);
		ps.setString(4, fechaInicio);
		
		modificaciones = ps.executeUpdate();
		ConfigSQLite.cerrarConexion(conexion);
		
		return modificaciones > 0;
	}
}
