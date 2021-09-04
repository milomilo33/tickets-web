import com.google.gson.Gson;
import storage.PopStore;
import utility.PopGenerator;

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

        get("/api/test", (request, response) -> "Hello world");
    }

}
