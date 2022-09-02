package com.br.alura.goBarber.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.goBarber.controller.dto.DetalhesDoTopicoDto;
import com.br.alura.goBarber.controller.dto.TopicoDto;
import com.br.alura.goBarber.controller.form.AtualizacaoTopicoForm;
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
	@Transactional
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
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);//convertendo para topico, para persistir dentro da entidade "topico"
		topicoRepository.save(topico);//inserindo +1 topico no BD
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();//construindo a URI
		return ResponseEntity.created(uri).body(new TopicoDto(topico));//mandando a URI, e convertendo os Topico para TopicoDto
	}
	
	@GetMapping("/{id}")
	//@PathVariable pra dizer que será com / e não com ? na URL
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {//DetalhesDoTopicoDto para retornar + que 4 atributos
		Optional<Topico> topico = topicoRepository.findById(id);//se o findById não encontrar o ID não lança EXCEPTION
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));//get() <- para pegar "Topico" mesmo e não "Optional<Topico>"			
		}
		
		return ResponseEntity.notFound().build();//build() para mostrar o objeto ResponseEntity
	}
	
	@PutMapping("/{id}")
	@Transactional //para avisar ao Spring para comitar a transação no final do método
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);//se o findById não encontrar o ID não lança EXCEPTION
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));//o corpo que será devolvido como resposta pro servidor
		}
		return ResponseEntity.notFound().build();//aqui ta devolvendo 404
	 	//Aqui nem precisamos fazer ex: topicoRepository.update() nem nada pois o próprio Spring se encarrega de atualizar no BD
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	//Esse ResponseEntity<TopicoDto> <- isso é o que é devolvido no corpo da requisição para nós que estamos no back-end
	public ResponseEntity<?> remover(@PathVariable Long id) {//Obriga a colocar Generic, então vamos colocar "?" <- não sei qual é o tipo
		Optional<Topico> optional = topicoRepository.findById(id);//se o findById não encontrar o ID não lança EXCEPTION
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();//retorna 200, sem corpo
		}
		return ResponseEntity.notFound().build();//aqui ta devolvendo 404
	}
	
}
