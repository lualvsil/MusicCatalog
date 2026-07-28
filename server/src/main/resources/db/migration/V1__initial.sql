CREATE TABLE album (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	title VARCHAR(256) NOT NULL,
	release_year INT
);

CREATE TABLE music (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	title VARCHAR(256) NOT NULL,
	duration INT NOT NULL,
	album_id UUID NOT NULL,
	FOREIGN KEY (album_id) REFERENCES album(id)
);
