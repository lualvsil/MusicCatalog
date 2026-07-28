package com.lualvsil.music.controller;

import com.lualvsil.music.model.Music;
import com.lualvsil.music.repository.MusicRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class MusicController {
	public record ErrorMessage(String error, String details) {}

	private final MusicRepository musicRepository;

	public MusicController(MusicRepository musicRepository) {
		this.musicRepository = musicRepository;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/musics")
	public Music createMusic(@RequestBody Music music) {
		return musicRepository.insert(music);
	}
	
	@GetMapping("/api/musics")
	public List<Music> listMusic() {
		return musicRepository.list();
	}
	
	@GetMapping("/api/musics/{id}")
	public Music getMusic(@PathVariable UUID id) {
		return musicRepository.findMusicById(id)
			.orElseThrow(() -> new NoSuchElementException(
				"Music with id " + id + " not found!"
			));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/api/musics/{id}")
	public void deleteMusic(@PathVariable UUID id) {
		int changes = musicRepository.delete(id);
		if (changes == 0) {
			throw new NoSuchElementException("Music with id " + id + " not found!");
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/api/musics/{id}")
	public void updateMusic(@PathVariable UUID id, @RequestBody Music music) {
		int changes = musicRepository.update(id, music);
		if (changes == 0) {
			throw new NoSuchElementException("Music with id " + id + " not found!");
		}
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleIndexError(NoSuchElementException exception) {
		return new ErrorMessage("Music Not Found", exception.getMessage());
	}
}