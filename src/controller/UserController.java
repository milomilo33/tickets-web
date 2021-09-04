package controller;

import com.google.gson.Gson;
import domain.Ticket;
import domain.User;
import domain.UserRole;
import spark.Route;
import storage.PopStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

                    return res;
                }
            }
        }

        res.status(400);
        return res;
    });

}
