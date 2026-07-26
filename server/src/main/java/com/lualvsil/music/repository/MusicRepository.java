package com.lualvsil.music.repository;

import com.lualvsil.music.model.Music;

import java.util.Optional;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class MusicRepository {
	private final JdbcTemplate jdbcTemplate;

	private RowMapper<Music> musicMapper = (rs, rowNum) ->
		new Music(
			rs.getLong("id"),
			rs.getString("title"),
			rs.getInt("duration"),
			rs.getLong("album_id")
		);

	public MusicRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Music insert(Music music) {
		String sql = "INSERT INTO music (title, duration, album_id) VALUES (?, ?, ?);";
		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
			ps.setString(1, music.title());
			ps.setInt(2, music.duration());
			ps.setLong(3, music.album_id());
			return ps;
		}, holder);

		Long id = holder.getKey().longValue();

		return new Music(
			id, music.title(), music.duration(), music.album_id()
		);
	}

	public List<Music> list() {
		return jdbcTemplate.query(
			"SELECT * FROM music;",
			musicMapper
		);
	}

	public int delete(int id) {
		int rows = jdbcTemplate.update(
			"DELETE FROM music WHERE id=?;",
			id
		);
		return rows;
	}
	
	public int put(int id, Music music) {
		int rows = jdbcTemplate.update(
			"UPDATE music SET title=?, duration=?, album_id=? WHERE id=?;",
			music.title(), music.duration(), music.album_id(), id
		);
		return rows;
	}

	public Optional<Music> findMusicById(int id) {
		try {
			Music music = jdbcTemplate.queryForObject(
				"SELECT * FROM music WHERE id=?;",
				musicMapper,
				id
			);
			return Optional.ofNullable(music);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}