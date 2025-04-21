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

	        String sql = "SELECT film.id, film.title, film.description, film.release_year, " +
	                     "film.language_id, language.name, film.rental_duration, " +
	                     "film.rental_rate, film.length, film.replacement_cost, " +
	                     "film.rating, film.special_features " +
	                     "FROM film JOIN language ON film.language_id = language.id " +
	                     "WHERE film.title LIKE ? OR film.description LIKE ?";

	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1, "%" + keyword + "%");
	        stmt.setString(2, "%" + keyword + "%");

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Film film = new Film();

	            film.setId(rs.getInt("film.id"));
	            film.setTitle(rs.getString("film.title"));
	            film.setDescription(rs.getString("film.description"));
	            film.setReleaseYear(rs.getInt("film.release_year"));
	            film.setLanguageId(rs.getInt("film.language_id"));
	            film.setLanguage(rs.getString("language.name"));
	            film.setRentalDuration(rs.getInt("film.rental_duration"));
	            film.setRentalRate(rs.getDouble("film.rental_rate"));
	            film.setLength(rs.getInt("film.length"));
	            film.setReplacementCost(rs.getDouble("film.replacement_cost"));
	            film.setRating(rs.getString("film.rating"));
	            film.setSpecialFeatures(rs.getString("film.special_features"));

	            film.setActors(findActorsByFilmId(film.getId()));

	            films.add(film);
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