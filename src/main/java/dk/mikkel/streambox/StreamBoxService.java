package dk.mikkel.streambox;

import java.util.*;

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
        if (userAge < 0){
            throw new IllegalArgumentException("Age cannot be negative.");
        }

        if (userAge < content.getAgeRating()){
            return false;
        }
        content.incrementViews();
        return true;
    }

    public List<Content> findByGenre(Genre genre) {
        List<Content> specificGenre = new ArrayList<>();

        for (Content content : catalog) {
            if (content.getGenre() == genre) {
                specificGenre.add(content);
            }
        }

        specificGenre.sort(Comparator.comparing(Content::getTitle));
        return specificGenre;
    }

    public int totalRuntimeByGenre(Genre genre) {
        List<Content> specificGenre = findByGenre(genre);
        int totalRuntime = 0;
        for (Content content : specificGenre) {
            totalRuntime += content.getLengthMinutes();
        }
        return totalRuntime;
    }

    public List<Content> topTrending(int n) {
        List<Content> top = getCatalog();
        if (n <= 0){
            throw new IllegalArgumentException("Number of top movies must be greater than 0.");
        }

        top.sort(Comparator.comparing(Content::getViews).reversed().thenComparing(Content::getTitle));

        return top.subList(0, n);
    }

    public Optional<Content> mostViewedInGenre(Genre genre) {
        // bevidst: ingen resultater
        Content best = null;
        for (Content c : catalog){
            if (c.getGenre() == genre){
                if (best == null || c.getViews() > best.getViews() || c.getViews() == best.getViews() && c.getTitle().compareTo(best.getTitle()) < 0) {
                    best = c;
                }
            }
        }
        return Optional.ofNullable(best);
    }

    public boolean removeById(int id) {
        Content removing;
        for  (Content c : catalog) {
            if (c.getId() == id) {
                removing = c;
                catalog.remove(removing);
                return true;
            }
        }
        return false;
    }
}
