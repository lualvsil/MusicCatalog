package com.lualvsil.music;

import com.lualvsil.music.model.Music;
import com.lualvsil.music.repository.MusicRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

	@PostMapping("/musics")
	public Music musics(@RequestBody Music music) {
		musicRepository.insert(music);
		return music;
	}
	
	@GetMapping("/musics")
	public Music musicGet(@RequestParam(defaultValue="0") int id) {
		return musicRepository.findMusicById(id)
			.orElseThrow(() -> new NoSuchElementException(
				"Music with id " + id + " not found!"
			));
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleIndexError(NoSuchElementException exception) {
		return new ErrorMessage("Music Not Found", exception.getMessage());
	}
}