import com.google.gson.Gson;
import controller.CommentController;
import controller.ManifestationController;
import controller.TicketController;
import domain.Ticket;
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
        get("/api/allmanifestations", ManifestationController.GetAllManifestations);
        get("/api/manifestationtypes", ManifestationController.GetAllManifestationTypes);
        get("/api/manifestationsearch", ManifestationController.SearchAllManifestations);
        get("/api/manifestationdetails/:id", ManifestationController.ShowManifestationDetails);
        get("/api/remainingtickets/:id", ManifestationController.GetRemainingTickets);
        get("/api/manifestationcomments/:id", ManifestationController.GetCommentsForManifestation);
        get("/api/cancomment/:id", CommentController.CheckCanComment);
        get("/api/alltickets", TicketController.GetAllTickets);
        get("/api/ticketsearch", TicketController.SearchAllTickets);

        post("/api/buyers/register", UserController.RegisterBuyer);
        post("/api/login", UserController.Login);
        post("/api/logout", UserController.Logout);
        post("/api/users/profile", UserController.ChangeUserProfileInfo);
        post("/api/addnewmanifestation", ManifestationController.CreateNewManifestation);
        post("/api/activatemanifestation/:id", ManifestationController.ActivateManifestation);
        post("/api/deletemanifestation/:id", ManifestationController.DeleteManifestation);
        post("/api/updatemanifestation", ManifestationController.UpdateManifestation);
        post("/api/createcomment", CommentController.AddComment);
        post("/api/approvecomment/:id", CommentController.ApproveComment);
        post("/api/deletecomment/:id", CommentController.DeleteComment);
    }

}
