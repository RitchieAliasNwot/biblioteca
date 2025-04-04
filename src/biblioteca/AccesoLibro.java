package biblioteca;

import java.sql.*;
import java.util.ArrayList;
import config.ConfigSQLite;
import entrada.Teclado;
import excepciones.BDException;
import modelo.Libro;

public class AccesoLibro {		
	/**
	 * 
	 * @param isbn
	 * @param titulo
	 * @param escritor
	 * @param anyoPublicacion
	 * @param puntuacion
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean insertarLibro(String isbn, String titulo, String escritor, int anyoPublicacion, double puntuacion) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int inserciones = 0;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "INSERT INTO libro (isbn, titulo, escritor, anyo_publicacion, puntuacion) VALUES (?, ?, ?, ?, ?);";

			ps = conexion.prepareStatement(query);

			ps.setString(1, isbn);
			ps.setString(2, titulo);
			ps.setString(3, escritor);
			ps.setInt(4, anyoPublicacion);
			ps.setDouble(5, puntuacion);

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

	// TODO Mensaje de error para libros referenciados en préstamos
	/**
	 * Eliminar un libro, por código, de la base de datos.
	 * @param codigo
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static boolean eliminarLibro(int codigo) throws BDException, SQLException {
		PreparedStatement ps = null;
		Connection conexion = null;
		
		int modificaciones = 0;
		
		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "DELETE FROM libro WHERE codigo = ?;";
			
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
	 * Consultar todos los libros de la base de datos.
	 * @return
	 * @throws BDException
	 * @throws SQLException
	 */
	public static ArrayList<Libro> consultarLibros() throws BDException, SQLException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM libro;";
			
			ps = conexion.prepareStatement(query);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor = resultados.getString("escritor");
				int anyoPublicacion = resultados.getInt("anyo_publicacion");
				double puntuacion = resultados.getDouble("puntuacion");
				
				listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
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
		return listaLibros;
	}

	/**
	 * Consultar los libros por escritor, ordenados por puntuación descendente.
	 * @param escritor
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Libro> consultarLibrosPorEscritor(String escritor) throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM libro WHERE LOWER(escritor) = LOWER(?) ORDER BY puntuacion DESC;";
			
			ps = conexion.prepareStatement(query);
			ps.setString(1, escritor);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				int anyoPublicacion = resultados.getInt("anyo_publicacion");
				double puntuacion = resultados.getDouble("puntuacion");
				
				listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
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
		return listaLibros;
	}

	// TODO Revisar consulta
	// Consultar los libros no prestados de la base de datos.
	public static ArrayList<Libro> consultarLibrosNoPrestados() throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM libro WHERE codigo NOT IN (SELECT codigo_libro FROM prestamo);";
			ps = conexion.prepareStatement(query);
			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor = resultados.getString("escritor");
				int anyoPublicacion = resultados.getInt("anyo_publicacion");
				double puntuacion = resultados.getDouble("puntuacion");
				
				listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
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
		return listaLibros;
	}

	/**
	 * Consultar los libros devueltos en una fecha dada.
	 * @param fechaDevolucion
	 * @return
	 * @throws BDException
	 */
	public static ArrayList<Libro> consultarLibrosDevueltos(String fechaDevolucion) throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			// No se ha podido abrir la conexión
			if (conexion == null) {
				throw new BDException(BDException.ERROR_ABRIR_CONEXION);
			}
			
			String query = "SELECT * FROM libro WHERE codigo IN (SELECT codigo_libro FROM prestamo WHERE fecha_devolucion = ?);";
			
			ps = conexion.prepareStatement(query);
			ps.setString(1, fechaDevolucion);
			
			ResultSet resultados = ps.executeQuery();
			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor = resultados.getString("escritor");
				int anyoPublicacion = resultados.getInt("anyo_publicacion");
				double puntuacion = resultados.getDouble("puntuacion");
				
				listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
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
		return listaLibros;
	}
}