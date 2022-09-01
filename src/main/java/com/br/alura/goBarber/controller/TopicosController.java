package com.br.alura.goBarber.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.goBarber.controller.dto.TopicoDto;
import com.br.alura.goBarber.controller.form.TopicoForm;
import com.br.alura.goBarber.modelo.Topico;
import com.br.alura.goBarber.repository.CursoRepository;
import com.br.alura.goBarber.repository.TopicoRepository;

@RestController //Por default retorna valor no body, lá no navegador
@RequestMapping("/topicos")
public class TopicosController {
	
	//Vamos injetar aqui o JpaRepository para fazer consultas no BD
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
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
	
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);//convertendo para topico, para persistir dentro da entidade "topico"
		topicoRepository.save(topico);//inserindo +1 topico no BD
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();//construindo a URI
		return ResponseEntity.created(uri).body(new TopicoDto(topico));//mandando a URI, e convertendo os Topico para TopicoDto
	}
}
