package dk.mikkel.streambox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StreamBoxService {

    private List<Content> catalog = new ArrayList<>();
    private int nextId = 1;

    public Content addContent(String title, Genre genre, int lengthMinutes, int ageRating) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie must contain a title.");
        }
        if (genre == null || genre.toString().isEmpty()) {
            throw new IllegalArgumentException("Movie must contain genre.");
        }
        if (lengthMinutes <= 0 || lengthMinutes > 200) {
            throw new IllegalArgumentException("Length of movie must be greater than 0.");
        }
        if (ageRating != 0 && ageRating != 7 && ageRating != 11 && ageRating != 15 && ageRating != 18) {
            throw new IllegalArgumentException("Age rating must either be 0, 7, 11, 15 or 18.");
        }


        int id = nextId++;
        Content newContent = new Content(id, title, genre, lengthMinutes, ageRating);
        catalog.add(newContent);

        return newContent;
    }

    public List<Content> getCatalog() {
        return catalog;
    }

    public Optional<Content> findById(int id) {
        for (Content i : catalog) {
            if (i.getId() == id) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public boolean play(int contentId, int userAge) {
        Optional<Content> playing = findById(contentId);
        if (playing.isEmpty()){
            return false;
        }

        Content content = playing.get();

        if (userAge < content.getAgeRating()){
            return false;
        }
        content.incrementViews();
        return true;
    }

    public List<Content> findByGenre(Genre genre) {
        // bevidst: returnerer altid tom
        return new ArrayList<>();
    }

    public int totalRuntimeByGenre(Genre genre) {
        // bevidst: 0
        return 0;
    }

    public List<Content> topTrending(int n) {
        // bevidst: tom liste (og ingen validering)
        return new ArrayList<>();
    }

    public Optional<Content> mostViewedInGenre(Genre genre) {
        // bevidst: ingen resultater
        return Optional.empty();
    }

    public boolean removeById(int id) {
        // bevidst: fjerner aldrig noget
        return false;
    }
}
