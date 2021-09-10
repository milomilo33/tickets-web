package controller;

import com.google.gson.Gson;
import domain.*;
import spark.Route;
import storage.PopStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class UserController {

    public static Gson gson = new Gson();

    public static Route RegisterBuyer = ((req, res) -> {
        var newUserDetails = gson.fromJson(req.body(), HashMap.class);

        String username = (String) newUserDetails.get("username");
        var users = PopStore.getUsers();
        for (var user : users) {
            if (username.equals(user.getUsername()) && !user.getDeleted()) {
                res.status(400);
                res.body("Username already exists!");
                return res;
            }
        }

        String password = (String) newUserDetails.get("password");
        String name = (String) newUserDetails.get("name");
        String surname = (String) newUserDetails.get("surname");
        String gender = (String) newUserDetails.get("gender");
        String birth = (String) newUserDetails.get("birth");

        // change UserType
        var newUser = new User(UUID.randomUUID(), username, password, name, surname, gender, birth, UserRole.KUPAC,
                               new ArrayList<Ticket>(), null, 0.0, null, false, false);
        users.add(newUser);

        res.status(200);
        return res;
    });

    public static Route Login = ((req, res) -> {
        var credentials = gson.fromJson(req.body(), HashMap.class);

        String username = (String) credentials.get("username");
        String password = (String) credentials.get("password");

        var users = PopStore.getUsers();
        for (var user : users) {
            if (username.equals(user.getUsername()) && !user.getDeleted()) {
                if (password.equals(user.getPassword())) {
                    if (user.getBlocked()) {
                        res.body("Your account has been blocked by the administrator!");
                        res.status(400);
                    }
                    else {
                        res.status(200);

                        switch (user.getRole()) {
                            case KUPAC:
                                res.body("buyer");
                                break;
                            case PRODAVAC:
                                res.body("seller");
                                break;
                            case ADMINISTRATOR:
                                res.body("administrator");
                                break;
                            default:
                                res.body("error");
                                break;
                        }

                        PopStore.setCurrentUser(user);
                        req.session().attribute("currentUser", user);

                        res.status(200);
                    }
                    return res;
                }
            }
        }

        res.body("Invalid username and/or password!");
        res.status(400);
        return res;
    });

    public static Route Logout = ((req, res) -> {
        PopStore.setCurrentUser(null);
        req.session().attribute("currentUser", null);
        res.status(200);
        return res;
    });

    public static Route GetCurrentUsername = ((req, res) -> {
        if (PopStore.getCurrentUser() == null) {
            res.status(400);
        }
        else {
            String username = PopStore.getCurrentUser().getUsername();
            res.status(200);
            res.body(username);
        }

        return res;
    });

    public static Route GetCurrentUser = ((req, res) -> {
        if (PopStore.getCurrentUser() == null) {
            res.status(400);
        }
        else {
            res.status(200);
            res.body(gson.toJson(PopStore.getCurrentUser()));
        }

        return res;
    });

    public static Route ChangeUserProfileInfo = ((req, res) -> {
        var newUserDetails = gson.fromJson(req.body(), HashMap.class);

        String name = (String) newUserDetails.get("name");
        String surname = (String) newUserDetails.get("surname");
        String gender = (String) newUserDetails.get("gender");
        String birth = (String) newUserDetails.get("birth");

        User user = PopStore.getCurrentUser();
        user.setName(name);
        user.setSurname(surname);
        user.setGender(gender);
        user.setBirth(birth);

        res.status(200);
        return res;
    });

    public static Route GetUserTypes = ((req, res) -> {
        if (PopStore.getUserTypes() == null) {
            res.status(200);
        }
        else {
            res.status(200);
            List<String> userTypes = new ArrayList<>();
            userTypes.add("/");
            for (var type : PopStore.getUserTypes())
                userTypes.add(type.getName());
            res.body(gson.toJson(userTypes));
        }

        return res;
    });

    public static Route GetAllUsers = ((req, res) -> {
        List<User> users = null;
        switch (PopStore.getCurrentUser().getRole()) {
            case PRODAVAC:
                users = GetAllUsersForSeller();
                res.status(200);
                break;
            case ADMINISTRATOR:
                users = GetAllUsersForAdmin();
                res.status(200);
                break;
            default:
                res.status(400);
                break;
        }

        res.body(gson.toJson(users));
        return res;
    });

    private static List<User> GetAllUsersForAdmin() {
        return PopStore.getUsers()
                .stream()
                .filter(user -> !user.getDeleted())
                .collect(Collectors.toList());
    }

    private static List<User> GetAllUsersForSeller() {
        return PopStore.getUsers()
                .stream()
                .filter(user -> !user.getDeleted())
                .filter(user -> {
                    for (var t : PopStore.getTickets())
                        if (PopStore.getCurrentUser().getManifestations().contains(t.getManifestation()) &&
                            t.getBuyer().getId().equals(user.getId()) && !t.getDeleted())
                            return true;
                    return false;
                })
                .collect(Collectors.toList());
    }

    public static Route SearchAllUsers = ((req, res) -> {
        List<User> users = null;
        switch (PopStore.getCurrentUser().getRole()) {
            case PRODAVAC:
                users = GetAllUsersForSeller();
                res.status(200);
                break;
            case ADMINISTRATOR:
                users = GetAllUsersForAdmin();
                res.status(200);
                break;
            default:
                res.status(400);
                return res;
        }

        Map<String, String> searchParams = new HashMap<>();
        req.queryParams().forEach(q -> searchParams.put(q, req.queryParams(q)));

        if (searchParams.get("userTypeSelected").equals("Select a user type:")) searchParams.put("userTypeSelected", "");
        if (searchParams.get("userRoleSelected").equals("Select a user role:")) searchParams.put("userRoleSelected", "");
        else searchParams.put("userRoleSelected", userRoleFix(searchParams.get("userRoleSelected")));

        var resultingUsers = users.stream()
                .filter(user -> user.getName().toLowerCase().contains(searchParams.getOrDefault("name", user.getName()).toLowerCase()))
                .filter(user -> user.getSurname().toLowerCase().contains(searchParams.getOrDefault("lastName", user.getSurname()).toLowerCase()))
                .filter(user -> user.getUsername().toLowerCase().contains(searchParams.getOrDefault("username", user.getUsername()).toLowerCase()))
                .filter(user -> user.getType().getName().toLowerCase().contains(searchParams.get("userTypeSelected").toLowerCase()))
                .filter(user -> user.getRole().name().toLowerCase().contains(searchParams.get("userRoleSelected").toLowerCase()))
                .filter(user -> {
                    if (PopStore.getCurrentUser().getRole() == UserRole.ADMINISTRATOR) {
                        Boolean onlySuspicious = Boolean.parseBoolean(searchParams.get("onlySuspicious"));
                        if (onlySuspicious.equals(true)) {
                            return isUserSuspicious(user);
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (Integer.parseInt(searchParams.get("sortSelected")) != 0) {

            switch (Integer.parseInt(searchParams.get("sortSelected"))){
                case 2: //asc
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getName))
                            .collect(Collectors.toList());
                    break;
                case 4:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getSurname))
                            .collect(Collectors.toList());
                    break;
                case 6:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getUsername))
                            .collect(Collectors.toList());
                    break;
                case 8:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getPoints))
                            .collect(Collectors.toList());
                    break;
                case 1: //desc
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getName).reversed())
                            .collect(Collectors.toList());
                    break;
                case 3:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getSurname).reversed())
                            .collect(Collectors.toList());
                    break;
                case 5:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getUsername).reversed())
                            .collect(Collectors.toList());
                    break;
                case 7:
                    resultingUsers = resultingUsers.stream()
                            .sorted(Comparator.comparing(User::getPoints).reversed())
                            .collect(Collectors.toList());
                    break;
                default:
                    break;
            }
        }

        res.body(gson.toJson(resultingUsers));
        return res;
    });

    private static String userRoleFix(String role) {
        switch (role) {
            case "Buyer":
                return "KUPAC";
            case "Seller":
                return "PRODAVAC";
            case "Administrator":
                return "ADMINISTRATOR";
            default:
                return "ERROR";
        }
    }

    private static boolean isUserSuspicious(User user) {
        int numOfCancellations = 0;
        for (var ticket : PopStore.getTickets()) {
            if (!ticket.getDeleted() && ticket.getBuyer().getId().equals(user.getId())) {
                if (ticket.getStatus() == TicketStatus.ODUSTANAK) {
                    long days = ticket.getCancellationDate().until(LocalDateTime.now(), ChronoUnit.DAYS);
                    if (days < 31) {
                        numOfCancellations++;
                    }
                }
            }
        }

        return numOfCancellations > 5;
    }

    public static Route BlockUser = ((req, res) -> {
        UUID userId = UUID.fromString(req.params(":id"));

        User userToBlock = PopStore.getUsers()
                .stream()
                .filter(u -> u.getId().equals(userId)).collect(Collectors.toList()).get(0);

        if (userToBlock == null || userToBlock.getDeleted()) {
            res.status(400);
            return res;
        }

        if (isUserSuspicious(userToBlock)) {
            userToBlock.setBlocked(true);
            res.status(200);
            res.body(userToBlock.getUsername());
        }
        else
            res.status(400);

        return res;
    });

    public static Route DeleteUser = ((req, res) -> {
        UUID userId = UUID.fromString(req.params(":id"));

        User userToDelete = PopStore.getUsers()
                .stream()
                .filter(u -> u.getId().equals(userId)).collect(Collectors.toList()).get(0);

        if (userToDelete == null || userToDelete.getRole() == UserRole.ADMINISTRATOR) {
            res.status(400);
            return res;
        }

        userToDelete.setDeleted(true);
        res.status(200);
        res.body(userToDelete.getUsername());

        return res;
    });

    public static Route RegisterSeller = ((req, res) -> {
        var newUserDetails = gson.fromJson(req.body(), HashMap.class);

        String username = (String) newUserDetails.get("username");
        var users = PopStore.getUsers();
        for (var user : users) {
            if (username.equals(user.getUsername()) && !user.getDeleted()) {
                res.status(400);
                res.body("Username already exists!");
                return res;
            }
        }

        String password = (String) newUserDetails.get("password");
        String name = (String) newUserDetails.get("name");
        String surname = (String) newUserDetails.get("surname");
        String gender = (String) newUserDetails.get("gender");
        String birth = (String) newUserDetails.get("birth");

        var newUser = new User(UUID.randomUUID(), username, password, name, surname, gender, birth, UserRole.PRODAVAC,
                null, new ArrayList<>(), 0.0, null, false, false);
        users.add(newUser);

        res.status(200);
        return res;
    });

}
