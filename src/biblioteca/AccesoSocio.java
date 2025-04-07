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
	public static boolean insertarSocio(String dni, String nombre, String domicilio, String telefono, String correo) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int inserciones = 0;
		
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

			inserciones = ps.executeUpdate();
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
		return inserciones > 0;
	}
	
	/**
	 * Eliminar un socio por código
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
	 * Consultar todos los socios
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
	 * Consultar los socios sin préstamos
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
	 * Consultar los socios con préstamo en una fecha
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
	
	// MENSAJES DE ERROR
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean referenciado(int codigo) throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		boolean referenciado = false;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT codigo_socio FROM prestamo "
					+ "WHERE fecha_devolucion IS NULL "
					+ "AND codigo_socio = ?;";
			
			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigo);
			
			ResultSet resultados = ps.executeQuery();
			
			if (resultados.next()) {
				referenciado = true;
			}
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return referenciado;
	}
	
	// CONSULTAS AVANZADAS
	
	/**
	 * Consultar socio(s) con más préstamos
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static ArrayList<Socio> masPrestamos() throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		ArrayList<Socio> socios = new ArrayList<Socio>();
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT s.*, COUNT(p.codigo_socio) AS total_prestamos "
					+ "FROM socio s JOIN prestamo p ON s.codigo = p.codigo_socio "
					+ "GROUP BY s.codigo HAVING COUNT(p.codigo_socio) = ("
					+ 	"SELECT MAX(conteo) "
					+ 	"FROM ("
					+		"SELECT COUNT(p2.codigo_socio) AS conteo "
					+ 		"FROM prestamo p2 "
					+ 		"GROUP BY p2.codigo_libro"
					+	")"
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
	 * Consultar los socios que han realizado una
	 * cantidad de préstamos superior a la media
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static ArrayList<Socio> prestamosSuperiorMedia() throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		ArrayList<Socio> socios = new ArrayList<Socio>();
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT s.*, COUNT(p.codigo_socio) AS total_prestamos "
					+ "FROM socio s "
					+ "JOIN prestamo p ON s.codigo = p.codigo_socio "
					+ "GROUP BY s.codigo "
					+ "HAVING COUNT(p.codigo_socio) > ("
					+ 	"SELECT AVG(conteo) "
					+ 	"FROM ("
					+ 		"SELECT COUNT(p2.codigo_socio) AS conteo "
					+ 		"FROM prestamo p2 "
					+ 		"GROUP BY p2.codigo_socio"
					+ 	")"
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
	 * Consultar DNI, nombre y nº de préstamos realizados
	 * ordenador por el nº de préstamos descendente
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static String datosSociosMasPrestamos() throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		String devolver = "";
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT dni, nombre, COUNT(codigo_socio) AS n_prestamos"
					+ "FROM socio JOIN prestamo ON(codigo = codigo_socio) "
					+ "GROUP BY codigo ORDER BY n_prestamos DESC;";
					
			ps = conexion.prepareStatement(query);
			
			ResultSet resultados = ps.executeQuery();
			
			for (int i = 0; resultados.next(); i++) {
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				int numeroPrestamos = resultados.getInt("n_prestamos");
				
				devolver += "Socio " + i + " :\n\t"
						+ "[DNI: " + dni + " Nombre: " + nombre
						+ "\n\t Número de préstamos: " + numeroPrestamos
						+ "]\n";
			}
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				throw new BDException(BDException.ERROR_CERRAR_CONEXION);
			}
		}
		return devolver;
	}
}