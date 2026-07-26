package com.lualvsil.music.repository;

import com.lualvsil.music.model.Music;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class MusicRepository {
	private final JdbcTemplate jdbcTemplate;

	public MusicRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insert(Music music) {
		jdbcTemplate.update(
			"INSERT INTO music (title, duration, album_id) VALUES (?, ?, ?);",
			music.title(), music.duration(), music.album_id()
		);
	}

	public Optional<Music> findMusicById(int id) {
		try {
			Music music = jdbcTemplate.queryForObject(
				"SELECT * FROM music WHERE id=?;",
				(rs, rowNum) -> new Music(
					rs.getLong("id"),
					rs.getString("title"),
					rs.getInt("duration"),
					rs.getLong("album_id")
				),
				id
			);
			return Optional.ofNullable(music);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}