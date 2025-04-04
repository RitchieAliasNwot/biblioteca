package biblioteca;

import java.sql.SQLException;
import java.util.ArrayList;

import entrada.Teclado;
import excepciones.BDException;
import modelo.Libro;

public class Principal {

	public static void main(String[] args) {
		int opcion = 0;
		
		do {
			// Imprimir opciones del menú
			System.out.println("\n0) Salir del programa");
			System.out.println("1) Opciones libros");
//			System.out.println("2) Opciones socios");
			System.out.println("3) Opciones préstamos");
			System.out.println("4) Consultas avanzadas");
			
			// Escoger opción
			opcion = Teclado.leerEntero("¿Opción? ");
			System.out.println();
			
			if (opcion == 1) {
				menuLibros();
//			} else if (opcion == 2) {
//				menuSocios();
			} else if (opcion == 3) {
				menuPrestamos();
			} else if (opcion == 4) {
				menuConsultas();
			} else if (opcion != 0) {
				System.err.println("La opción debe estar comprendida entre 0 y 4");
			}
		} while (opcion != 0);
	}
	
	private static void menuLibros() {
		int opcion = 0;
		
		do {
			// Imprimir opciones del menú
			System.out.println("\n0) Salir del submenú");
			System.out.println("1) Insertar un libro");
			System.out.println("2) Eliminar un libro por código");
			System.out.println("3) Consulatr todos los libros");
			System.out.println("4) Consultar varios libros, por escritor, ordenados por puntuación descendente");
			System.out.println("5) Consultar los libros no prestados");
			System.out.println("6) Consultar los libros devueltos en una fecha");
			
			// Escoger opción
			opcion = Teclado.leerEntero("¿Opción? ");
			System.out.println();
			
			if (opcion == 1) { // Insertar libro
				String isbn = Teclado.leerCadena("¿ISBN? ");
				String titulo = Teclado.leerCadena("¿Título? ");
				String escritor = Teclado.leerCadena("¿Escritor? ");
				int publicacion = Teclado.leerEntero("¿Fecha de publicación? ");
				double puntuacion = Teclado.leerReal("¿Puntuación? ");
				
				try {
					boolean insertado = AccesoLibro.insertarLibro(isbn, titulo, escritor, publicacion, puntuacion);
					if (insertado) {
						System.out.println("Se ha insertado el libro en la base de datos");
					} else {
						System.out.println("No se ha podido insertar el libro");
					}
				} catch (BDException | SQLException e) {
					System.err.println(e.getMessage());
				}
				
			} else if (opcion == 2) { // Eliminar libro
				int codigo = Teclado.leerEntero("¿Código del libro a eliminar? ");
				
				try {
					boolean eliminado = AccesoLibro.eliminarLibro(codigo);
					
					if (eliminado) {
						System.out.println("Se ha eliminado el libro de la base de datos");
					} else {
						System.out.println("No se ha podido eliminar el libro");
					}
				} catch (BDException | SQLException e) {
					System.err.println(e.getMessage());
				}
				
			} else if (opcion == 3) {
				try {
					System.out.println(AccesoLibro.consultarLibros());
				} catch (BDException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 4) {
				String escritor = Teclado.leerCadena("¿Escritor? ");
				
				try {
					System.out.println(AccesoLibro.consultarLibrosPorEscritor(escritor));
				} catch (BDException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 5) {
				try {
					System.out.println(AccesoLibro.consultarLibrosNoPrestados());
				} catch (BDException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 6) {
				String devolucion = Teclado.leerCadena("¿Fecha de devolución? ");
				try {
					System.out.println(AccesoLibro.consultarLibrosDevueltos(devolucion));
				} catch (BDException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion != 0) {
				System.err.println("La opción debe estar comprendida entre 0 y 6");
			}
		} while (opcion != 0);
	}
	
//	private static void menuSocios() {
//		int opcion = 0;
//		
//		do {
//			// Imprimir opciones del menú
//			System.out.println("\n0) Salir del submenú");
//			System.out.println("1) Insertar un socio");
//			System.out.println("2) Eliminar un socio por código");
//			System.out.println("3) Consultar todos los socios");
//			System.out.println("4) Consultar socios, por localidad, ordenados por nombre ascendente");
//			System.out.println("5) Consultar los socios sin préstamos");
//			System.out.println("6) Consultar los socios con préstamos en una fecha");
//			
//			// Escoger opción
//			opcion = Teclado.leerEntero("¿Opción? ");
//			System.out.println();
//			
//			if (opcion == 1) {
//				
//			} else if (opcion == 2) {
//				
//			} else if (opcion == 3) {
//				
//			} else if (opcion == 4) {
//				
//			} else if (opcion == 5) {
//				
//			} else if (opcion == 6) {
//				
//			} else if (opcion != 0) {
//				System.err.println("La opción debe estar comprendida entre 0 y 6");
//			}
//		} while (opcion != 0);
//	}
	
	private static void menuPrestamos() {
		int opcion = 0;
		
		do {
			// Imprimir opciones del menú
			System.out.println("\n0) Salir del submenú");
			System.out.println("1) Insertar un préstamo");
			System.out.println("2) Actualizar un préstamo por datos identificativos");
			System.out.println("3) Eliminar un préstamo por datos identificativos");
			System.out.println("4) Consultar todos los préstamos");
			System.out.println("5) Consultar los préstamos no devueltos");
			System.out.println("6) Consultar DNI, nombre de socio, ISBN, título y fecha de devolución de los préstamos realizados en una fecha");
			
			// Escoger opción
			opcion = Teclado.leerEntero("¿Opción? ");
			System.out.println();
			
			if (opcion == 1) {
				int codigoLibro = Teclado.leerEntero("¿Código de libro? ");
				int codigoSocio = Teclado.leerEntero("¿Código de socio? ");
				String fechaInicio = Teclado.leerCadena("¿Fecha de inicio? ");
				String fechaFin = Teclado.leerCadena("¿Fecha fin? ");
				
				try {
					if (AccesoPrestamo.libroPrestado(codigoLibro)) {
						System.err.println("Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.");
					} else if (AccesoPrestamo.socioEndeudado(codigoSocio)) {
						System.err.println("Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.");
					} else if (AccesoPrestamo.insertarPrestamo(codigoLibro, codigoSocio, fechaInicio, fechaFin)) {
						System.out.println("Se ha insertado un préstamo en la base de datos.");
					}
				} catch (BDException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 2) {
				
			} else if (opcion == 3) {
				
			} else if (opcion == 4) {
				
			} else if (opcion == 5) {
				
			} else if (opcion == 6) {
				
			} else if (opcion != 0) {
				System.err.println("La opción debe estar comprendida entre 0 y 6");
			}
		} while (opcion != 0);
	}
	
	private static void menuConsultas() {
		int opcion = 0;
		
		do {
			// Imprimir opciones del menú
			System.out.println("\n0) Salir del submenú");
			System.out.println("1) Consultar el libros que ha sido prestados menos veces (mínimo 1)");
			System.out.println("2) Consultar el socio que ha realizado más préstamos");
			System.out.println("3) Consultar los libros que han sido prestados una cantidad de veces inferior a la media");
			System.out.println("4) Consultar los socios que han realizado una cantidad de préstamos superior a la media");
			System.out.println("5) Consultar el ISBN, título y nº de veces prestados de los libros, ordenados por el nº de préstamo descendente");
			System.out.println("6) Consultar el DNI, nombre y nº de préstamos realizados, ordenados por el nº de préstamo descente");
			
			// Escoger opción
			opcion = Teclado.leerEntero("¿Opción? ");
			System.out.println();
			
			if (opcion == 1) {
				int codigoLibro = Teclado.leerEntero("¿Código de libro? ");
				int codigoSocio = Teclado.leerEntero("¿Código de socio? ");
				String fechaInicio = Teclado.leerCadena("¿Fecha de inicio? ");
				String fechaFin = Teclado.leerCadena("¿Fecha fin? ");
				
				try {
					if (AccesoPrestamo.insertarPrestamo(codigoLibro, codigoSocio, fechaInicio, fechaFin)) {
						System.out.println("Se ha insertado el préstamo en la base de datos");
					} else {
						System.out.println("No se ha insertado el préstamo en la base de datos");
					}
				} catch (BDException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 2) {
				System.out.println("Datos del préstamo a modificar:");
				
				int codigoLibro = Teclado.leerEntero("¿Código del libro? ");
				int codigoSocio = Teclado.leerEntero("¿Código del socio?");
				String fechaInicio = Teclado.leerCadena("¿Fecha de inicio? ");
				
				System.out.println("Modificaciones:");
				String fechaDevolucion = Teclado.leerCadena("¿Fecha de devolución? ");
				
				try {
					if (AccesoPrestamo.modificarPrestamo(fechaDevolucion, codigoLibro, codigoSocio, fechaInicio)) {
						System.out.println("Se ha modificado el préstamo con éxito");
					} else {
						System.out.println("No se ha podido modificar el préstamo");
					}
				} catch (BDException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 3) {
				int codigoLibro = Teclado.leerEntero("¿Código del libro? ");
				int codigoSocio = Teclado.leerEntero("¿Código del socio?");
				String fechaInicio = Teclado.leerCadena("¿Fecha de inicio? ");
				
				try {
					if (AccesoPrestamo.eliminarPrestamo(codigoLibro, codigoSocio, fechaInicio)) {
						System.out.println("Se ha eliminado el préstamo correctamente");
					} else {
						System.out.println("No se ha podido eliminar el préstamo");
					}
				} catch (BDException | SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 4) {
				try {
					System.out.println(AccesoPrestamo.consultarPrestamos());
				} catch (BDException | SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 5) {
				try {
					System.out.println(AccesoPrestamo.consultarPrestamosNoDevuletos());
				} catch (BDException | SQLException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion == 6) {
				String fechaInicio = Teclado.leerCadena("¿Fecha de inicio?  ");
				
				try {
					System.out.println(AccesoPrestamo.consultarDatosFecha(fechaInicio));
				} catch (BDException e) {
					System.err.println(e.getMessage());
				}
			} else if (opcion != 0) {
				System.err.println("La opción debe estar comprendida entre 0 y 6");
			}
		} while (opcion != 0);
	}
}
