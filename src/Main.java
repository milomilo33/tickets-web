import com.google.gson.Gson;
import controller.UserController;
import storage.PopStore;

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

    public static void main(String[] args) {
        port(8080);

        try {
            staticFiles.externalLocation(new File("./static").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        before((req, res) -> {
            PopStore.setCurrentUser(req.session().attribute("currentUser"));
        });

        get("/api/test", (req, res) -> "Hello world");

        post("/api/buyers/register", UserController.RegisterBuyer);
        post("/api/login", UserController.Login);
    }

}
