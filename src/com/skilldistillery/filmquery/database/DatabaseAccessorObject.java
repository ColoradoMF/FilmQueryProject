package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
	private static final String USER = "student";
	private static final String PASS = "student";

	static {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film foundFilm = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);

			String sql = "SELECT * FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);

			ResultSet filmData = stmt.executeQuery();

			if (filmData.next()) {
				foundFilm = new Film();

				foundFilm.setId(filmData.getInt("id"));
				foundFilm.setTitle(filmData.getString("title"));
				foundFilm.setDescription(filmData.getString("description"));
				foundFilm.setReleaseYear(filmData.getInt("release_year"));
				foundFilm.setLanguageId(filmData.getInt("language_id"));
				int langId = filmData.getInt("language_id");
				foundFilm.setLanguageId(langId);
				foundFilm.setLanguage(findLanguageById(langId));

				foundFilm.setRentalDuration(filmData.getInt("rental_duration"));
				foundFilm.setRentalRate(filmData.getDouble("rental_rate"));
				foundFilm.setLength(filmData.getInt("length"));
				foundFilm.setReplacementCost(filmData.getDouble("replacement_cost"));
				foundFilm.setRating(filmData.getString("rating"));
				foundFilm.setSpecialFeatures(filmData.getString("special_features"));
				foundFilm.setActors(findActorsByFilmId(filmId));
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return foundFilm;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor foundActor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);

			String sql = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);

			ResultSet actorData = stmt.executeQuery();

			if (actorData.next()) {
				foundActor = new Actor();

				foundActor.setId(actorData.getInt("id"));
				foundActor.setFirstName(actorData.getString("first_name"));
				foundActor.setLastName(actorData.getString("last_name"));

			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return foundActor;

	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			
			String sql = "SELECT id, first_name, last_name "
					+ "FROM actor "
					+ "JOIN film_actor ON actor.id = film_actor.actor_id "
					+ "WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1,filmId);
		  
			ResultSet actorResult = stmt.executeQuery();
			while (actorResult.next()) {
			    Actor actor = new Actor(); // Create the object
			    // map query columns to object fields:
			    actor.setId(actorResult.getInt(1));
			    actor.setFirstName(actorResult.getString(2));
			    actor.setLastName(actorResult.getString(3));
			    actors.add(actor);
		  }
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	
	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) {
		List<Film> films = new ArrayList<>();
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			
			String sql = "SELECT * "
					+ "FROM film "
					+ "WHERE description LIKE ? OR title LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,"%" + keyword + "%");
			stmt.setString(2,"%" + keyword + "%");
		
			ResultSet filmsResult = stmt.executeQuery();
				while (filmsResult.next()) {
			    Film film = new Film(); // Create the object
			    // map query columns to object fields:
			    film.setId(filmsResult.getInt(1));
			    film.setTitle(filmsResult.getString(2));
			    film.setDescription(filmsResult.getString(3));
			    film.setReleaseYear(filmsResult.getInt(4));
			    film.setLanguage(filmsResult.getString(5));
			    film.setLanguageId(filmsResult.getInt(6));
			    film.setRentalDuration(filmsResult.getInt(7));
			    film.setRentalRate(filmsResult.getDouble(8));
			    film.setLength(filmsResult.getInt(9));
			    film.setReplacementCost(filmsResult.getDouble(10));
			    film.setRating(filmsResult.getString(11));
			    film.setSpecialFeatures(filmsResult.getString(12));
			    films.add(film);
//			    Field            | Type                                                                | Null | Key | Default | Extra          |
//			    id               | int                                                                 | NO   | PRI | NULL    | auto_increment |
//			    | title            | varchar(255)                                                        | NO   | MUL | NULL    |                |
//			    | description      | text                                                                | YES  |     | NULL    |                |
//			    | release_year     | year                                                                | YES  |     | NULL    |                |
//			    | language_id      | tinyint unsigned                                                    | NO   | MUL | NULL    |                |
//			    | rental_duration  | tinyint unsigned                                                    | NO   |     | 3       |                |
//			    | rental_rate      | decimal(4,2)                                                        | NO   |     | 4.99    |                |
//			    | length           | smallint unsigned                                                   | YES  |     | NULL    |                |
//			    | replacement_cost | decimal(5,2)                                                        | NO   |     | 19.99   |                |
//			    | rating           | enum('G','PG','PG13','R','NC17')                                    | YES  |     | G       |                |
//			    | special_features | set('Trailers','Commentaries','Deleted Scenes','Behind the Scenes') | YES  |     | NULL    |                |

		  }
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}
	
	@Override
	public String findLanguageById(int languageId) {
		String language = null;
		String sql = "SELECT name FROM language WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, languageId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				language = rs.getString("name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return language;
	}
	

}