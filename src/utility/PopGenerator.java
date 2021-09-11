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

    public static void specialPopFill(){
        PopStore.setUsers(new ArrayList<>());
        PopStore.setComments(new ArrayList<>());
        PopStore.setManifestations(new ArrayList<>());
        PopStore.setTickets(new ArrayList<>());
        PopStore.setUserTypes(new ArrayList<>());
        UserType u = new UserType(UUID.randomUUID(), "BRONZE", 0.95, 1200.0 );
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "SILVER", 0.88, 4000.0);
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "GOLD", 0.80, 12000.0);
        PopStore.getUserTypes().add(u);
    }

    public static void fillPopStore(){
        PopStore.setUsers(new ArrayList<>());
        PopStore.setComments(new ArrayList<>());
        PopStore.setManifestations(new ArrayList<>());
        PopStore.setTickets(new ArrayList<>());
        PopStore.setUserTypes(new ArrayList<>());

        UserType u = new UserType(UUID.randomUUID(), "BRONZE", 0.95, 1200.0 );
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "SILVER", 0.88, 4000.0);
        PopStore.getUserTypes().add(u);
        u = new UserType(UUID.randomUUID(), "GOLD", 0.80, 12000.0);
        PopStore.getUserTypes().add(u);
        UserType t = (UserType) PopStore.getUserTypes().toArray()[0];

        Location location = new Location(UUID.randomUUID(), 45.2716, 19.8478, "Novi Sad");

        Manifestation m = new Manifestation(UUID.randomUUID(), "Fated spin", "Wedding", "10", LocalDateTime.now(), 250.0, true, location, imgToString("src/images/wedding.png"), false, 10.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Red Star Match", "Soccer", "1000", LocalDateTime.now().plusDays(14), 150.0, true, location, imgToString("src/images/soccer.png"), false, 0.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Ftn entrance party", "Party", "50", LocalDateTime.now().plusDays(5), 300.0, true, location, imgToString("src/images/party.png"), false, 0.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Dinos Cinema", "Cinema", "75", LocalDateTime.now().plusDays(8), 550.0, true, location, imgToString("src/images/cinema.png"), false, 0.0);
        PopStore.getManifestations().add(m);
        m = new Manifestation(UUID.randomUUID(), "Petroland", "Aquapark", "300", LocalDateTime.now().plusDays(14), 400.0, true, location, imgToString("src/images/aquapark.png"), false, 0.0);
        PopStore.getManifestations().add(m);

        User user = new User(UUID.randomUUID(), "AdminSuperstar", "admin0", "Dragoljub", "Vladin", "Male", "1981-03-09", UserRole.ADMINISTRATOR, null, null, 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius0", "seller0", "Visnja", "Iljin", "Female", "1951-01-02", UserRole.PRODAVAC, null,
                new LinkedHashSet<>(new ArrayList<>(PopStore.getManifestations()).subList(0,2)), 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius1", "seller1", "Slavica", "Travic", "Female", "1954-01-02", UserRole.PRODAVAC, null,
                new LinkedHashSet<>(new ArrayList<>(PopStore.getManifestations()).subList(2,4)), 0.0, (UserType) PopStore.getUserTypes().toArray()[0], false, false);
        PopStore.getUsers().add(user);
        user = new User(UUID.randomUUID(), "SellerGenius2", "seller2", "Vesna", "Babic", "Female", "1952-02-01", UserRole.PRODAVAC, null,
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
            UserType type = (UserType) PopStore.getUserTypes().toArray()[typenum];
            user = new User(UUID.randomUUID(), "RandomBuyer" + i, "buyer" + i, "Boba" + i, "Bobic" + i, "Male", "1995"+"-0"+i+"-0"+i, UserRole.KUPAC, new ArrayList<>(),
                    null, type.getThreshold() + 5, type, false, false);
            PopStore.getUsers().add(user);
        }

        System.out.println(user.getUsername());
        System.out.println(user.getType());
        System.out.println(user.getPoints());
        Ticket ticket = new Ticket(UUID.randomUUID(), generateShortId(10),m,m.getDate(), 400.0, user, TicketStatus.REZERVISANA, TicketType.REGULAR);
        PopStore.getTickets().add(ticket);
        Manifestation randomManifestation = PopStore.getManifestations().stream()
                                    .skip((int) (PopStore.getManifestations().size() * Math.random()))
                                    .findFirst()
                                    .orElse(m);
        ticket = new Ticket(UUID.randomUUID(), generateShortId(10),randomManifestation,randomManifestation.getDate(), randomManifestation.getTicketPrice() * 2, user, TicketStatus.ODUSTANAK, TicketType.FAN_PIT);
        ticket.setCancellationDate(LocalDateTime.now());
        PopStore.getTickets().add(ticket);
        randomManifestation = PopStore.getManifestations().stream()
                .skip((int) (PopStore.getManifestations().size() * Math.random()))
                .findFirst()
                .orElse(m);
        ticket = new Ticket(UUID.randomUUID(), generateShortId(10),randomManifestation,randomManifestation.getDate(), randomManifestation.getTicketPrice(), user, TicketStatus.REZERVISANA, TicketType.REGULAR);
        PopStore.getTickets().add(ticket);
        randomManifestation = PopStore.getManifestations().stream()
                .skip((int) (PopStore.getManifestations().size() * Math.random()))
                .findFirst()
                .orElse(m);
        ticket = new Ticket(UUID.randomUUID(), generateShortId(10),randomManifestation,randomManifestation.getDate(), randomManifestation.getTicketPrice() * 4, user, TicketStatus.REZERVISANA, TicketType.VIP);
        PopStore.getTickets().add(ticket);

        Comment comment = new Comment(UUID.randomUUID(), user, new ArrayList<>(PopStore.getManifestations()).get(0), "sve je bilo super", 10, true, false);
        PopStore.getComments().add(comment);
    }
}
