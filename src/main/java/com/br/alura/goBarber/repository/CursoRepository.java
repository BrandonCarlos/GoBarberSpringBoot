package com.br.alura.goBarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.alura.goBarber.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nomeCurso);

}
