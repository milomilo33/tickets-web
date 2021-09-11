package storage;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PopStore {
    private static User currentUser;

    private static Collection<User> users;
    private static Collection<Manifestation> manifestations;
    private static Collection<Ticket> tickets;
    private static Collection<Comment> comments;
    private static Collection<UserType> userTypes;

    public static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getDeclaringClass() == Manifestation.class && field.getName().equals("picture");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    private static Gson gson = new GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(strategy).create();

    public static Collection<UserType> getUserTypes() {
        return userTypes;
    }

    public static void setUserTypes(Collection<UserType> userTypes) {
        PopStore.userTypes = userTypes;
    }

    public static void readAll(){
        users = new ArrayList<>();
        manifestations = new ArrayList<>();
        tickets = new ArrayList<>();
        comments = new ArrayList<>();

        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/users.json"));
            users = new ArrayList<>(Arrays.asList(gson.fromJson(reader, User[].class)));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/manifestations.json"));
            manifestations = new ArrayList<>(Arrays.asList(gson1.fromJson(reader, Manifestation[].class)));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/tickets.json"));
            tickets = new ArrayList<>(Arrays.asList(gson.fromJson(reader, Ticket[].class)));
            reader.close();
        } catch (Exception ignored) {

        }
        try {
            Reader reader = Files.newBufferedReader(Paths.get("src/storage/output/comments.json"));
            comments = new ArrayList<>(Arrays.asList(gson.fromJson(reader, Comment[].class)));
            reader.close();
        } catch (Exception ignored) {

        }
    }

    public static void writeAll(){
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/users.json"));
            gson.toJson(users, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/manifestations.json"));
            gson1.toJson(manifestations, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("src/storage/output/tickets.json"));
            gson.toJson(tickets, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        try {
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
