package com.lualvsil.music.model;

public record Music(
	Long id,
	String title,
	Integer duration,
	Long album_id
) {}