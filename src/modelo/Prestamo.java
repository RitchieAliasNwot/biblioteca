package modelo;

import java.util.Objects;

public class Prestamo {
	private Libro libro;
	private Socio socio;
	private String fechaInicio;
	private String fechaFin;
	private String fechaDevolucion;
	
	public Prestamo(Libro libro, Socio socio, String fechaInicio, String fechaFin, String fechaDevolucion) {
		this.libro = libro;
		this.socio = socio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaDevolucion = fechaDevolucion;
	}
  
  public Prestamo(Libro libro, Socio socio, String fechaInicio, String fechaFin) {
    this.libro = libro;
    this.socio = socio;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
  }

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getFecha_devolucion() {
		return fechaDevolucion;
	}

	public void setFecha_devolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fechaInicio, libro, socio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prestamo other = (Prestamo) obj;
		return Objects.equals(fechaInicio, other.fechaInicio) && Objects.equals(libro, other.libro)
				&& Objects.equals(socio, other.socio);
	}

	@Override
	public String toString() {
		return "Prestamo [Código de libro: " + libro.getCodigo() + ", Código de socio: " + socio.getCodigo() + ", Fecha inicio: " + fechaInicio + ", Fecha fin: "
				+ fechaFin + ", Fecha devolucion=" + fechaDevolucion + "]\n";
	}
	
}
