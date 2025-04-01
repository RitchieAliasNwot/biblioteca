package modelo;

import java.util.Objects;

public class Libro {
	private int codigo;
	private String isbn;
	private String titulo;
	private String escritor;
	private int anyoPublicacion;
	private double puntuacion;
	
	public Libro(int codigo, String isbn, String titulo, String escritor, int anyoPublicacion, double puntuacion) {
		this.codigo = codigo;
		this.isbn = isbn;
		this.titulo = titulo;
		this.escritor = escritor;
		this.anyoPublicacion = anyoPublicacion;
		this.puntuacion = puntuacion;
	}
  
  public Libro (int codigo) {
    this.codigo = codigo;
  }

	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getEscritor() {
		return escritor;
	}
	
	public void setEscritor(String escritor) {
		this.escritor = escritor;
	}
	
	public int getAnyoPublicacion() {
		return anyoPublicacion;
	}
	
	public void setAnyoPublicacion(int anyoPublicacion) {
		this.anyoPublicacion = anyoPublicacion;
	}
	
	public double getPuntuacion() {
		return puntuacion;
	}
	
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
		
	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return codigo == other.codigo;
	}
	
	@Override
	public String toString() {
		return "Libro [Código: " + codigo + ", ISBN:" + isbn + ", Título: " + titulo + ", Escritor: " + escritor
				+ ", Año de publicación: " + anyoPublicacion + ", Puntuación: " + puntuacion + "]\n";
	}
}