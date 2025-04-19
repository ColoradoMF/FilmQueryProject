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
		    // Here is our mapping of query columns to our object fields:
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
}