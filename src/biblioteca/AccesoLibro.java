package biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import config.ConfigSQLite;
import excepciones.BDException;
import modelo.Libro;

public class AccesoLibro {

    // Insertar un libro en la base de datos.
    public static void insertarLibro(String isbn, String titulo, String escritor, int anyoPublicacion, double puntuacion) throws BDException {
        Connection conexion = ConfigSQLite.abrirConexion();
        
        String query = "INSERT INTO libro (isbn, titulo, escritor, anyoPublicacion, puntuacion) "
        + "VALUES ('?', '?', '?', '?', '?')";

        PreparedStatement ps = conexion.prepareStatement(query);
        
        ps.setString(1, isbn);
        ps.setString(2, titulo);
        ps.setString(3, escritor);
        ps.setInt(4, anyoPublicacion);
        ps.setDouble(5, puntuacion);
        
        ps.executeUpdate();
        
        conexion.close();
    }

    // Eliminar un libro, por código, de la base de datos.
    public static void eliminarLibro(int codigo) throws BDException {
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigSQLite.abrirConexion();
            String query = "DELETE FROM libro WHERE codigo = ?";
            ps = conexion.prepareStatement(query);
            ps.setInt(1, codigo);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Se ha eliminado un libro de la base de datos.");
            } else {
                System.out.println("No existe ningún libro con ese código en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el libro: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) conexion.close();  // Cerramos la conexión
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    // Consultar todos los libros de la base de datos.
    public static List<Libro> consultarLibros() throws BDException {
        List<Libro> listaLibros = new ArrayList<Libro>();
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigSQLite.abrirConexion();
            String query = "SELECT * FROM libro";
            ps = conexion.prepareStatement(query);
            ResultSet resultados = ps.executeQuery();

            while (resultados.next()) {
                int codigo = resultados.getInt("codigo");
                String isbn = resultados.getString("isbn");
                String titulo = resultados.getString("titulo");
                String escritor = resultados.getString("escritor");
                int anyoPublicacion = resultados.getInt("anyoPublicacion");
                double puntuacion = resultados.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) conexion.close();  // Cerramos la conexión
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return listaLibros;
    }

    // Consultar los libros por escritor, ordenados por puntuación descendente.
    public static List<Libro> consultarLibrosPorEscritor(String escritor) throws BDException {
        List<Libro> listaLibros = new ArrayList<Libro>();
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigSQLite.abrirConexion();
            String query = "SELECT * FROM libro WHERE escritor = ? ORDER BY puntuacion DESC";
            ps = conexion.prepareStatement(query);
            ps.setString(1, escritor);
            ResultSet resultados = ps.executeQuery();

            while (resultados.next()) {
                int codigo = resultados.getInt("codigo");
                String isbn = resultados.getString("isbn");
                String titulo = resultados.getString("titulo");
                int anyoPublicacion = resultados.getInt("anyoPublicacion");
                double puntuacion = resultados.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros por escritor: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) conexion.close();  // Cerramos la conexión
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return listaLibros;
    }

    // Consultar los libros no prestados de la base de datos.
    public static List<Libro> consultarLibrosNoPrestados() throws BDException {
        List<Libro> listaLibros = new ArrayList<Libro>();
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigSQLite.abrirConexion();
            String query = "SELECT * FROM libro WHERE codigo NOT IN (SELECT codigo_libro FROM prestamo)";
            ps = conexion.prepareStatement(query);
            ResultSet resultados = ps.executeQuery();

            while (resultados.next()) {
                int codigo = resultados.getInt("codigo");
                String isbn = resultados.getString("isbn");
                String titulo = resultados.getString("titulo");
                String escritor = resultados.getString("escritor");
                int anyoPublicacion = resultados.getInt("anyoPublicacion");
                double puntuacion = resultados.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros no prestados: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) conexion.close();  // Cerramos la conexión
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return listaLibros;
    }

    // Consultar los libros devueltos en una fecha dada.
    public static List<Libro> consultarLibrosDevueltos(String fechaDevolucion) throws BDException {
        List<Libro> listaLibros = new ArrayList<Libro>();
        PreparedStatement ps = null;
        Connection conexion = null;

        try {
            conexion = ConfigSQLite.abrirConexion();
            String query = "SELECT * FROM libro WHERE codigo IN (SELECT codigo_libro FROM prestamo WHERE fecha_devolucion = ?)";
            ps = conexion.prepareStatement(query);
            ps.setString(1, fechaDevolucion);
            ResultSet resultados = ps.executeQuery();

            while (resultados.next()) {
                int codigo = resultados.getInt("codigo");
                String isbn = resultados.getString("isbn");
                String titulo = resultados.getString("titulo");
                String escritor = resultados.getString("escritor");
                int anyoPublicacion = resultados.getInt("anyoPublicacion");
                double puntuacion = resultados.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, anyoPublicacion, puntuacion));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros devueltos: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) conexion.close();  // Cerramos la conexión
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return listaLibros;
    }
}


