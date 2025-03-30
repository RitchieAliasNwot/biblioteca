package modelo;

import java.util.Objects;

public class Prestamo {
	private Libro libro;
	private Socio socio;
	private String fechaInicio;
	private String fechaFin;
	private String fecha_devolucion;
	
	public Prestamo(Libro libro, Socio socio, String fechaInicio, String fechaFin, String fecha_devolucion) {
		this.libro = libro;
		this.socio = socio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fecha_devolucion = fecha_devolucion;
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
		return fecha_devolucion;
	}

	public void setFecha_devolucion(String fecha_devolucion) {
		this.fecha_devolucion = fecha_devolucion;
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
	
}