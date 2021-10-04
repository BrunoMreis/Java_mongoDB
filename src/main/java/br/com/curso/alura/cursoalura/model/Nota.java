package br.com.curso.alura.cursoalura.model;

public class Nota {

	private Double nota;

	
	public Nota() {
	}
	
	
	public Nota(Double nota) {
		this.nota = nota;
	}


	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}


	@Override
	public String toString() {
		return "Nota [nota=" + nota + "]";
	}

	
	
}
