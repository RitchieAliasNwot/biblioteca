package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConfigSQLite;
import excepciones.BDException;
import modelo.Socio;

public class AccesoSocio {

	/**
	 * Inserta un socio a la base de datos.
	 * Si el DNI está repetido, muestra una excepción
	 * @param dni
	 * @param nombre
	 * @param domicilio
	 * @param telefono
	 * @param correo
	 * @throws BDException
	 */
	public static void insertarSocio(String dni, String nombre, String domicilio, String telefono, String correo) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) VALUES (?, ?, ?, ?, ?);";

			ps = conexion.prepareStatement(query);

			ps.setString(1, dni);
			ps.setString(2, nombre);
			ps.setString(3, domicilio);
			ps.setString(4, telefono);
			ps.setString(5, correo);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws BDException
	 */
	public static boolean eliminarSocio(int codigo) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		
		int modificaciones = 0;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "DELETE FROM socio WHERE codigo = ?;";
			
			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigo);
			
			modificaciones = ps.executeUpdate();
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return modificaciones > 0;
	}
	
	/**
	 * 
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Socio> consultarSocios() throws BDException {
		ArrayList<Socio> socios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM socio;";
			
			ps = conexion.prepareStatement(query);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");
				
				socios.add(new Socio(codigo, dni, nombre, domicilio, telefono, correo));
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return socios;
	}
	
	/**
	 * Consultar socios, por localidad
	 * ordenados por nombre ascendente
	 * @param domicilio
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Socio> consultarLocalidad(String domicilio) throws BDException {
		ArrayList<Socio> socios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;
		
		domicilio = domicilio.toLowerCase();	
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM socio "
					+ "WHERE LOWER(domicilio) LIKE '%?%' "
					+ "ORDER BY nombre ASC;";
			
			ps = conexion.prepareStatement(query);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");
				
				socios.add(new Socio(codigo, dni, nombre, domicilio, telefono, correo));
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return socios;
	}
	
	/**
	 * 
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Socio> consultarSinPrestamos() throws BDException {
		ArrayList<Socio> socios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			// No han sacado préstamos nunca
//			String query = "SELCT * FROM socio "
//					+ "WHERE codigo NOT IN(SELECT codigo_socio FROM prestamo);";
			
			String query = "SELCT * FROM socio "
					+ "WHERE codigo NOT IN("
						+ "SELECT codigo_socio FROM prestamo "
						+ "AND fecha_devolucion IS NULL"
					+ ");";
			
			ps = conexion.prepareStatement(query);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");
				
				socios.add(new Socio(codigo, dni, nombre, domicilio, telefono, correo));
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return socios;
	}
	
	/**
	 * 
	 * @param fechaInicio
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Socio> consultarPorFecha(String fechaInicio) throws BDException {
		ArrayList<Socio> socios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM socio "
					+ "WHERE codigo = ("
						+ "SELECT codigo_socio FROM prestamo "
						+ "WHERE fecha_inicio = ?"
					+ ")";
			
			ps = conexion.prepareStatement(query);
			ps.setString(1, fechaInicio);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");
				
				socios.add(new Socio(codigo, dni, nombre, domicilio, telefono, correo));
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return socios;
	}
}