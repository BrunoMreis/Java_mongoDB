package br.com.curso.alura.cursoalura.model;

public class Curso {

	private String nome;



	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Curso [nome=" + nome + "]";
	}

	
	

}
