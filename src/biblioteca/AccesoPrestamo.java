package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import config.ConfigSQLite;
import excepciones.BDException;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Socio;

public class AccesoPrestamo {

	/**
	 * Insertar un préstamo en la base de datos
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
	public static boolean insertarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin) throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		int inserciones = 0;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "INSERT INTO prestamo "
					+ "(codigo_libro, codigo_socio, fecha_inicio, fecha_fin) "
					+ "VALUES (?, ?, ?, ?);";

			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigoLibro);
			ps.setInt(2, codigoSocio);
			ps.setString(3, fechaInicio);
			ps.setString(4, fechaFin);

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
	 * Modificar la fecha de devolución de un préstamo identificado por código de
	 * libro y código de socio
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

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "UPDATE prestamo SET fecha_devolucion = ? WHERE codigo_libro = ? AND codigo_socio = ? "
					+ "AND fecha_inicio = ? ;";

			ps = conexion.prepareStatement(query);
			ps.setString(1, fechaDevolucion);
			ps.setInt(2, codigoLibro);
			ps.setInt(3, codigoSocio);
			ps.setString(4, fechaInicio);

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
	 * Eliminar un préstamo de la base de datos
	 * 
	 * @param codigoLibro
	 * @param codigoSocio
	 * @param fechaInicio
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean eliminarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int modificaciones = 0;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "DELETE FROM prestamo WHERE codigo_libro = ? "
					+ "AND codigo_socio = ? AND fecha_inicio = ?;";

			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigoLibro);
			ps.setInt(2, codigoSocio);
			ps.setString(3, fechaInicio);

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
	 * Consultar todos los préstamos de la base de datos
	 * 
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static ArrayList<Prestamo> consultarPrestamos() throws BDException, SQLException {
		ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT * FROM prestamo;";

			Statement sentencia = conexion.createStatement();
			ResultSet resultados = sentencia.executeQuery(query);

			// TODO probar si al obtener nulo en una consulta, se puede insertar NULL como
			// String
			while (resultados.next()) {
				Libro libro = new Libro(resultados.getInt("codigo_libro"));
				Socio socio = new Socio(resultados.getInt("codigo_socio"));

				String fechaInicio = resultados.getString("fecha_inicio");
				String fechaFin = resultados.getString("fecha_fin");
				String fechaDevolucion = resultados.getString("fecha_devolucion");

				Prestamo prestamo = null;
				if (fechaDevolucion.equalsIgnoreCase("NULL")) {
					prestamo = new Prestamo(libro, socio, fechaInicio, fechaFin, fechaDevolucion);
				} else {
					prestamo = new Prestamo(libro, socio, fechaInicio, fechaFin);
				}

				prestamos.add(prestamo);
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
		return prestamos;
	}

	/**
	 * Consultar los préstamos no devueltos de la base de datos
	 * 
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static ArrayList<Prestamo> consultarPrestamosNoDevuletos() throws BDException, SQLException {
		ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT * FROM prestamo WHERE fecha_devolucion IS NULL;";

			Statement sentencia = conexion.createStatement();
			ResultSet resultados = sentencia.executeQuery(query);

			while (resultados.next()) {
				Libro libro = new Libro(resultados.getInt("codigo_libro"));
				Socio socio = new Socio(resultados.getInt("codigo_socio"));

				String fechaInicio = resultados.getString("fecha_inicio");
				String fechaFin = resultados.getString("fecha_fin");

				Prestamo prestamo = new Prestamo(libro, socio, fechaInicio, fechaFin);
				prestamos.add(prestamo);
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
		return prestamos;
	}

	/**
	 * Consultar DNI, nombre de socio, ISBN, título de libro
	 * y fecha de devolución de los préstamos realizados en 
	 * una fecha dada
	 * @param fechaInicio
	 * @return
	 * @throws BDException
	 */
	public static String consultarDatosFecha(String fechaInicio) throws BDException {
		Connection conexion = null;
		PreparedStatement ps = null;
		
		String devolver = "";

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT socio.dni as dni, socio.nombre as nombre, "
					+ "libro.isbn as isbn, libro.titulo as titulo, fecha_devolucion "
					+ "FROM prestamo JOIN socio ON(codigo_socio = socio.codigo) "
					+ "JOIN libro ON(codigo_libro = libro.codigo) "
					+ "WHERE fecha_inicio = ?;";

			ps = conexion.prepareStatement(query);
			ps.setString(1, fechaInicio);

			ResultSet resultados = ps.executeQuery();
			
			// TODO quitar fecha inicio del resultado de la consulta
			int contador = 0;
			while (resultados.next()) {
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String fechaDevolucion = resultados.getString("fecha_devolucion");
				
				devolver += "Préstamo [DNI: " + dni + ", Nombre de socio: " + nombre + ", "
						+ "ISBN: " + isbn + ", Título: " + titulo +
						", Fecha de devolución: " + fechaDevolucion + "]\n";
				
				contador++;
			}
			devolver += "Se han consultado " + contador + " registros";
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
		if (devolver.equals("")) {
			return null;
		}
		return devolver;
	}
	
	// MENSAJES DE ERROR
	/**
	 * Comprueba si un libro se ha tomado prestado y no se ha devuleto
	 * Si sigue estando prestado (no se ha devuleto), devuelve TRUE
	 * @param codigoLibro
	 * @param codigoSocio
	 * @return
	 * @throws BDException
	 */
	public static boolean prestado(int codigoLibro) throws BDException {
		Connection conexion = null;
		PreparedStatement ps = null;
		boolean prestado = false;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT codigo_libro "
					+ "FROM prestamo WHERE codigo_libro = ? "
					+ "AND fecha_deolucion IS NULL;";
			
			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigoLibro);

			ResultSet resultados = ps.executeQuery();
			
			if (resultados.next()) {
				prestado = true;
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
		return prestado;
	}
	
	/**
	 * Comprueba si el socio tiene un libro prestado sin devolver
	 * @param codigoSocio
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean socioEndeudado(int codigoSocio) throws BDException, SQLException {
		Connection conexion = null;
		PreparedStatement ps = null;
		boolean endeudado = false;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}

			String query = "SELECT codigo_socio FROM prestamo "
					+ "WHERE codigo_socio = ? "
					+ "AND fecha_devolucion IS NULL;";
			
			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigoSocio);

			ResultSet resultados = ps.executeQuery();
			
			if (resultados.next()) {
				endeudado = true;
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
		return endeudado;
	}
}