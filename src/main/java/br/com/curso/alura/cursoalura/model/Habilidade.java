package br.com.curso.alura.cursoalura.model;

public class Habilidade {

	private String nome;
	private String nivel;

	public Habilidade(String nome, String nivel) {
		this.nome = nome;
		this.nivel = nivel;
	}
	
	public Habilidade() {}
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "Habilidade [nome=" + nome + ", nivel=" + nivel + "]";
	}

}
