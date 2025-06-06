package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();
	Scanner input = new Scanner(System.in); // (System.in)

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();

//	   here will comment out test and uncomment launch
//     app.test();
//     app.testActor();
		app.launch();
	}

	private void test() {
		Film film = db.findFilmById(1);
		if (film == null) {
			System.out.print("There's no film with that film id, so the value is ");
		}
		System.out.println(film);
		System.out.println(film.getActors());
		System.out.println("\n");
	}

	private void launch() {
		startUserInterface();

		input.close();
	}

	private void startUserInterface() {
		boolean menuLoop = true;
		String menuChoice;
		System.out.println("\n");
		try {

			while (menuLoop) {
				displayUserMenu();
				menuChoice = input.next();
				input.nextLine();

				// switch for the menu
				switch (menuChoice) {

				case "1": {
					System.out.println("Enter a film ID #:");
					int userFilmId = input.nextInt();

					Film film = db.findFilmById(userFilmId);
					if (film == null) {
						System.out.print("There's no film with that film id, so the value is ");
					}
//					System.out.println(film);
//					System.out.println(film.getActors());
					System.out.println(" Title: " + film.getTitle());
					System.out.println(" Year: " + film.getReleaseYear());
					System.out.println(" Rating: " + film.getRating());
					System.out.println(" Language: " + film.getLanguage());
					System.out.println(" Description: " + film.getDescription());
					System.out.println(" Cast:  ");
					for (Actor actors : film.getActors()) {
						System.out.println("       " + actors.getFirstName() + " " + actors.getLastName());
					}
					
					continue;
				}

//				case "2": {
//					System.out.println("Enter a keyword to search on: ");
//					String keyword = input.next();
//					List<Film> filmByKeyword = db.findFilmByKeyword(keyword);
//					if (filmByKeyword == null) {
//						System.out.println("Keyword not found for: " + keyword);
//					}
//					else {
//						for (Film filmByKey : filmByKeyword) {
//							System.out.println(filmByKey);
//						}
//					}
//					continue;
					
				case "2": {
				    System.out.println("Enter a keyword to search on: ");
				    String keyword = input.nextLine();
				    
				    List<Film> filmByKeyword = db.findFilmByKeyword(keyword);
				    
				    if (filmByKeyword == null || filmByKeyword.isEmpty()) {
				        System.out.println("No films found for keyword: " + keyword);
				    } else {
				        for (Film film : filmByKeyword) {
				            System.out.println(" Title: " + film.getTitle());
				            System.out.println(" Year: " + film.getReleaseYear());
				            System.out.println(" Rating: " + film.getRating());
				            System.out.println(" Language: " + film.getLanguage());
				            System.out.println(" Description: " + film.getDescription());
				            System.out.println(" Cast:");
				            for (Actor actor : film.getActors()) {
				                System.out.println("       " + actor.getFirstName() + " " + actor.getLastName());
				            }
				            System.out.println();
				        }
				    }
				    continue;
				

					
				}

				case "3": {
					quit();
					menuLoop = false;
					break;
				}

				default: {
					System.out.println("Invalid selection, kindly key in a number between 1 and 3.");
					continue;
				}
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Kindly restart app and key in a valid film ID to try again.");
			menuLoop = false;
		}

	}

	public void quit() {
		System.out.println("Film Query App Closed.");

	}

	private void displayUserMenu() {
		System.out.println("\n");
		System.out.println("=========================================");
		System.out.println("==         Film Query (Menu)           ==");
		System.out.println("==Press the # for your selected action ==");
		System.out.println("=========================================");
		System.out.println("== 1. To look up film by its ID #      ==");
		System.out.println("== 2. To look up a film by keyword.    ==");
		System.out.println("== 3. To exit Film Query application.  ==");
		System.out.println("=========================================");
		
	}

}