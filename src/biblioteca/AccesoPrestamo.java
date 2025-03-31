package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConfigSQLite;
import excepciones.BDException;

public class AccesoPrestamo {
	
	public static boolean insertarPrestamo(int codigo, String isbn, String titulo, String escritor, int publicacion, double puntuacion) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int inserciones = 0;
		
		conexion = ConfigSQLite.abrirConexion();
		
		String query = "INSERT INTO libro VALUES ('?', '?', '?', '?', '?', '?')";
		
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
}