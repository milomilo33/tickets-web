package utility;


import domain.*;
import javaxt.utils.Base64;
import storage.PopStore;


import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;


public class PopGenerator {
    static final String ALL_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String imgToString(String path){
        return Base64.encodeFromFile(path);
    }

    public static void stringToImg(String encoded, String path){
        Base64.decodeToFile(encoded, path);
    }

    public static String generateShortId(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(ALL_CHARS.charAt(rnd.nextInt(ALL_CHARS.length())));
        return sb.toString();
    }

    public static void fillPopStore(){
        PopStore.setUsers(new ArrayList<>());
        PopStore.setComments(new ArrayList<>());
        PopStore.setManifestations(new ArrayList<>());
        PopStore.setTickets(new ArrayList<>());
        PopStore.setUserTypes(new ArrayList<>());

        UserType u = new UserType(UUID.randomUUID(), "BRONZE", 0.95, 20.0 );
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "SILVER", 0.88, 50.0);
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "GOLD", 0.80, 100.0);
        PopStore.getUserTypes().add(u);
        UserType t = (UserType) PopStore.getUserTypes().toArray()[0];

        Location location = new Location(UUID.randomUUID(), 45.2716, 19.8478, "Novi Sad");

        Manifestation m = new Manifestation(UUID.randomUUID(), "Fated spin", "Wedding", "100", LocalDateTime.now(), 250.0, true, location, imgToString("src/images/wedding.png"), false, 5.4);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Red Star Match", "Soccer", "1000", LocalDateTime.now(), 150.0, true, location, imgToString("src/images/soccer.png"), false, 9.1);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Ftn entrance party", "Party", "50", LocalDateTime.now(), 300.0, true, location, imgToString("src/images/party.png"), false, 3.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Dinos Cinema", "Cinema", "75", LocalDateTime.now(), 550.0, true, location, imgToString("src/images/cinema.png"), false, 0.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Petroland", "Aquapark", "300", LocalDateTime.now(), 400.0, true, location, imgToString("src/images/aquapark.png"), false, 7.7);
        PopStore.getManifestations().add(m);

        User user = new User(UUID.randomUUID(), "AdminSuperstar", "admin0", "Dragoljub", "Vladin", "Male", "09.03.1981.", UserRole.ADMINISTRATOR, null, null, 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius0", "seller0", "Visnja", "Iljin", "Female", "02.01.1951.", UserRole.PRODAVAC, null,
                new LinkedHashSet<>(new ArrayList<>(PopStore.getManifestations()).subList(0,2)), 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius1", "seller1", "Slavica", "Travic", "Female", "02.01.1954.", UserRole.PRODAVAC, null,
                new LinkedHashSet<>(new ArrayList<>(PopStore.getManifestations()).subList(2,4)), 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius2", "seller2", "Vesna", "Babic", "Female", "02.01.1952.", UserRole.PRODAVAC, null,
                new LinkedHashSet<>(new ArrayList<>(PopStore.getManifestations()).subList(4,5)), 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        for(int i=0; i<10; i++){
            double points = i*12 + 0.0;
            int typenum;
            if(points<=20)
                typenum = 0;
            else if(points<=50)
                typenum = 1;
            else
                typenum = 2;
            user = new User(UUID.randomUUID(), "RandomBuyer" + i, "buyer" + i, "Boba" + i, "Bobic" + i, "Male", "0"+i+".0"+i+".1995.", UserRole.KUPAC, new ArrayList<>(),
                    null, points, (UserType) PopStore.getUserTypes().toArray()[typenum], false, false);
            PopStore.getUsers().add(user);
        }

        Ticket ticket = new Ticket(UUID.randomUUID(), generateShortId(10),m,LocalDateTime.now(), 400.0, user, TicketStatus.REZERVISANA, TicketType.REGULAR);
        PopStore.getTickets().add(ticket);

        Comment comment = new Comment(UUID.randomUUID(), user, m, "sve je bilo super", 10);
        PopStore.getComments().add(comment);
    }
}
