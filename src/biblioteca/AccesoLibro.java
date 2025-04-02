package biblioteca;

import java.sql.*;
import java.util.ArrayList;
import config.ConfigSQLite;
import excepciones.BDException;
import modelo.Libro;

//TODO Añadir mensajes de error personalizados e imprimirlos EN EL MAIN
public class AccesoLibro {
	// Insertar un libro en la base de datos.
	public static boolean insertarLibro(String isbn, String titulo, String escritor, int anyoPublicacion,
			double puntuacion) throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "INSERT INTO libro (isbn, titulo, escritor, anyo_publicacion, puntuacion) "
				+ "VALUES ('?', '?', '?', '?', '?')";

		PreparedStatement ps = conexion.prepareStatement(query);

		ps.setString(1, isbn);
		ps.setString(2, titulo);
		ps.setString(3, escritor);
		ps.setInt(4, anyoPublicacion);
		ps.setDouble(5, puntuacion);

		int modificaciones = ps.executeUpdate();

		conexion.close();

		return modificaciones > 0;
	}

	// Eliminar un libro, por código, de la base de datos.
	public static boolean eliminarLibro(int codigo) throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "DELETE FROM libro WHERE codigo = ?";

		PreparedStatement ps = conexion.prepareStatement(query);
		ps.setInt(1, codigo);

		int filasAfectadas = ps.executeUpdate();

		conexion.close();

		return filasAfectadas > 0;
	}

	// Consultar todos los libros de la base de datos.
	public static ArrayList<Libro> consultarLibros() throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "SELECT * FROM libro";

		PreparedStatement ps = conexion.prepareStatement(query);

		ResultSet resultados = ps.executeQuery();
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		while (resultados.next()) {
			int codigo = resultados.getInt("codigo");
			String isbn = resultados.getString("isbn");
			String titulo = resultados.getString("titulo");
			String escritor = resultados.getString("escritor");
			int anyoPublicacion = resultados.getInt("anyoPublicacion");
			double puntuacion = resultados.getDouble("puntuacion");
			listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
		}
		conexion.close();

		return listaLibros;
	}

	// Consultar los libros por escritor, ordenados por puntuación descendente.
	public static ArrayList<Libro> consultarLibrosPorEscritor(String escritor) throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "SELECT * FROM libro WHERE escritor = ? ORDER BY puntuacion DESC";

		PreparedStatement ps = conexion.prepareStatement(query);
		ps.setString(1, escritor);

		ResultSet resultados = ps.executeQuery();
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		while (resultados.next()) {
			int codigo = resultados.getInt("codigo");
			String isbn = resultados.getString("isbn");
			String titulo = resultados.getString("titulo");
			int anyoPublicacion = resultados.getInt("anyoPublicacion");
			double puntuacion = resultados.getDouble("puntuacion");
			listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
		}
		conexion.close();

		return listaLibros;
	}

	// Consultar los libros no prestados de la base de datos.
	public static ArrayList<Libro> consultarLibrosNoPrestados() throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();

		String query = "SELECT * FROM libro WHERE codigo NOT IN (SELECT codigo_libro FROM prestamo)";

		PreparedStatement ps = conexion.prepareStatement(query);
		ResultSet resultados = ps.executeQuery();

		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		while (resultados.next()) {
			int codigo = resultados.getInt("codigo");
			String isbn = resultados.getString("isbn");
			String titulo = resultados.getString("titulo");
			String escritor = resultados.getString("escritor");
			int anyoPublicacion = resultados.getInt("anyoPublicacion");
			double puntuacion = resultados.getDouble("puntuacion");
			listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
		}

		conexion.close();

		return listaLibros;
	}

	// Consultar los libros devueltos en una fecha dada.
	public static ArrayList<Libro> consultarLibrosDevueltos(String fechaDevolucion) throws BDException, SQLException {
		Connection conexion = ConfigSQLite.abrirConexion();
		
		String query = "SELECT * FROM libro WHERE codigo IN (SELECT codigo_libro FROM prestamo WHERE fecha_devolucion = ?)";
		
		PreparedStatement ps = conexion.prepareStatement(query);
		ps.setString(1, fechaDevolucion);
		
		ResultSet resultados = ps.executeQuery();
		
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();
		while (resultados.next()) {
			int codigo = resultados.getInt("codigo");
			String isbn = resultados.getString("isbn");
			String titulo = resultados.getString("titulo");
			String escritor = resultados.getString("escritor");
			int anyoPublicacion = resultados.getInt("anyoPublicacion");
			double puntuacion = resultados.getDouble("puntuacion");
			listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
		}

		conexion.close();

		return listaLibros;
	}
}
