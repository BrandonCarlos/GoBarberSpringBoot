package com.br.alura.goBarber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.alura.goBarber.controller.dto.TopicoDto;
import com.br.alura.goBarber.modelo.Topico;
import com.br.alura.goBarber.repository.TopicoRepository;

@RestController //Por default retorna valor no body, lá no navegador
@RequestMapping("/topicos")
public class TopicosController {
	
	//Vamos injetar aqui o JpaRepository para fazer consultas no BD
	@Autowired
	private TopicoRepository topicoRepository;
	
	@GetMapping
	public List<TopicoDto> lista(String cursoNome) {//Boa prática: retornar DTO e não a entidade
		if(cursoNome == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.convert(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(cursoNome);
			return TopicoDto.convert(topicos);
		}
	}
	
	
}
