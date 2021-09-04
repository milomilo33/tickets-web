import com.google.gson.Gson;
import storage.PopStore;
import utility.PopGenerator;
import controller.UserController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;


public class Main {

    public static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        port(8080);
        PopGenerator.fillPopStore();
        PopStore.writeAll();

        try {
            staticFiles.externalLocation(new File("./static").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        before((req, res) -> {
            PopStore.setCurrentUser(req.session().attribute("currentUser"));
        });

        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "*");
        });

        get("/api/test", (req, res) -> "Hello world");
        get("/api/currentUsername", UserController.GetCurrentUsername);
        get("/api/user", UserController.GetCurrentUser);

        post("/api/buyers/register", UserController.RegisterBuyer);
        post("/api/login", UserController.Login);
        post("/api/users/profile", UserController.ChangeUserProfileInfo);
    }

}
