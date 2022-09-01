package com.br.alura.goBarber.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.alura.goBarber.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	List<Topico> findByCursoNome(String cursoNome);

	
	
}
