package com.skilldistillery.filmquery.app;

import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();
  Scanner input = new Scanner(System.in); //(System.in)

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
    
    // here will comment out test and uncomment launch
      app.test();
//    app.testActor();
//    app.launch();
  }

  private void test() {
    Film film = db.findFilmById(1);
    if (film == null) {
    	System.out.print("There's no film with that film id, so the value is ");
    }
    System.out.println(film);
    System.out.println(film.getActors());
  }

  private void launch() {
//	  int input = 10; 
//    System.out.println(actor);
    startUserInterface();
    
    input.close();
  }

  private void startUserInterface() {
    
  }

}
