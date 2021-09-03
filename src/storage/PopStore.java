package storage;

import com.google.gson.Gson;
import domain.Comment;
import domain.Manifestation;
import domain.Ticket;
import domain.User;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PopStore {
    private static User currentUser;

    private static Collection<User> users;
    private static Collection<Manifestation> manifestations;
    private static Collection<Ticket> tickets;
    private static Collection<Comment> comments;

    public static void readAll(){
        users = new ArrayList<>();
        manifestations = new ArrayList<>();
        tickets = new ArrayList<>();
        comments = new ArrayList<>();

        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/users.json"));
            users = Arrays.asList(gson.fromJson(reader, User[].class));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/manifestations.json"));
            manifestations = Arrays.asList(gson.fromJson(reader, Manifestation[].class));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/tickets.json"));
            tickets = Arrays.asList(gson.fromJson(reader, Ticket[].class));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/comments.json"));
            comments = Arrays.asList(gson.fromJson(reader, Comment[].class));
            reader.close();
        } catch (Exception ignored) {

        }
    }

    public static void writeAll(){
        try {
            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/users.json"));
            gson.toJson(users, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/manifestations.json"));
            gson.toJson(manifestations, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/tickets.json"));
            gson.toJson(tickets, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/comments.json"));
            gson.toJson(comments, writer);
            writer.close();
        } catch (Exception ignored) {

        }
    }

    public static Collection<User> getUsers() {
        return users;
    }

    public static void setUsers(Collection<User> users) {
        PopStore.users = users;
    }

    public static Collection<Manifestation> getManifestations() {
        return manifestations;
    }

    public static void setManifestations(Collection<Manifestation> manifestations) {
        PopStore.manifestations = manifestations;
    }

    public static Collection<Ticket> getTickets() {
        return tickets;
    }

    public static void setTickets(Collection<Ticket> tickets) {
        PopStore.tickets = tickets;
    }

    public static Collection<Comment> getComments() {
        return comments;
    }

    public static void setComments(Collection<Comment> comments) {
        PopStore.comments = comments;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        PopStore.currentUser = currentUser;
    }
}
