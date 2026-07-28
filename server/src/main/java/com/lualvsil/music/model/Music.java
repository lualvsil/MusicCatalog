package com.lualvsil.music.model;

import java.util.UUID;

public record Music(
	UUID id,
	String title,
	Integer duration,
	UUID album_id
) {}