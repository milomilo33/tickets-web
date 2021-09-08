package controller;

import com.google.gson.Gson;
import domain.Location;
import domain.Manifestation;
import domain.UserRole;
import spark.Route;
import storage.PopStore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ManifestationController {
    public static Gson gson = new Gson();

    private static List<Manifestation> FilterManifestations(){
        List<Manifestation> manifestations = new ArrayList<>();
        if(PopStore.getCurrentUser() == null){
            manifestations = PopStore.getManifestations()
                    .stream()
                    .filter(manifestation -> !manifestation.getDeleted())
                    .filter(Manifestation::getActive)
                    .collect(Collectors.toList());
        }
        else
        switch (PopStore.getCurrentUser().getRole()) {
            case PRODAVAC:
                    manifestations = PopStore.getManifestations()
                        .stream()
                        .filter(manifestation -> !manifestation.getDeleted())
                        .filter(manifestation -> {
                            if(!manifestation.getActive()){
                                return PopStore.getCurrentUser().getManifestations().stream().anyMatch(manifestation1 -> manifestation1.getId().equals(manifestation.getId()));
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
                break;
            case ADMINISTRATOR:
                manifestations = PopStore.getManifestations()
                        .stream()
                        .filter(manifestation -> !manifestation.getDeleted())
                        .collect(Collectors.toList());
                break;
            default:
                manifestations = PopStore.getManifestations()
                        .stream()
                        .filter(manifestation -> !manifestation.getDeleted())
                        .filter(Manifestation::getActive)
                        .collect(Collectors.toList());
                break;
        }
        return manifestations;
    }

    public static Route GetAllManifestations = ((req, res) -> {
        if (PopStore.getManifestations() == null) {
            res.status(200);
        }
        else {
            res.status(200);
            Map<String, String> searchParams = new HashMap<>();
            req.queryParams().forEach(q -> searchParams.put(q, req.queryParams(q)));
            List<Manifestation> manifestations = FilterManifestations();
            if(Boolean.parseBoolean(searchParams.get("isSeller")))
                manifestations = manifestations.stream().filter(manifestation -> PopStore.getCurrentUser().getManifestations().stream().anyMatch(manifestation1 -> manifestation1.getId().equals(manifestation.getId()))).collect(Collectors.toList());
            res.body(gson.toJson(manifestations));
        }

        return res;
    });

    public static Route GetAllManifestationTypes = ((req, res) -> {
        if (PopStore.getManifestations() == null) {
            res.status(200);
        }
        else {
            res.status(200);
            res.body(gson.toJson(PopStore.getManifestations()
                    .stream().filter(manifestation -> !manifestation.getDeleted()).map(Manifestation::getType).distinct().collect(Collectors.toList())));
        }

        return res;
    });
    public static Route SearchAllManifestations = ((req, res) -> {
        if (PopStore.getManifestations() == null) {
            res.status(200);
        }
        else {
            res.status(200);
            Map<String, String> searchParams = new HashMap<>();
            req.queryParams().forEach(q -> searchParams.put(q, req.queryParams(q)));
            List<Manifestation> manifestations = FilterManifestations();

            if(searchParams.getOrDefault("dateFrom", "1900-01-01").equals("")) searchParams.put("dateFrom", "1900-01-01");
            if(searchParams.getOrDefault("dateTo", "1900-01-01").equals("")) searchParams.put("dateTo", "3021-01-01");
            if(searchParams.get("typeSelected").equals("Select a type:"))searchParams.put("typeSelected", "");
            var fpManifs = manifestations.stream()
                    .filter(manifestation -> manifestation.getName().toLowerCase().contains(searchParams.getOrDefault("name", manifestation.getName()).toLowerCase()))
                    .filter(manifestation -> manifestation.getLocation().getAddress().toLowerCase().contains(searchParams.getOrDefault("address", manifestation.getLocation().getAddress()).toLowerCase()))
                    .filter(manifestation -> manifestation.getDate().plusDays(1).toLocalDate().isAfter(LocalDate.parse(searchParams.getOrDefault("dateFrom", "1900-01-01"))))
                    .filter(manifestation -> manifestation.getDate().minusDays(1).toLocalDate().isBefore(LocalDate.parse(searchParams.getOrDefault("dateTo", "3021-01-01"))))
                    .filter(manifestation -> manifestation.getTicketPrice() >= Double.parseDouble(searchParams.getOrDefault("priceFrom", "0")))
                    .filter(manifestation -> manifestation.getTicketPrice() <= Double.parseDouble(searchParams.getOrDefault("priceTo", "99999999")))
                    .filter(manifestation -> manifestation.getType().toLowerCase().contains(searchParams.get("typeSelected").toLowerCase()))
                    .filter(manifestation -> {
                        var numns = PopStore.getTickets().stream()
                                .filter(ticket -> manifestation.getId().equals(ticket.getManifestation().getId()))
                                .count();
                        if(Boolean.parseBoolean(searchParams.get("soldOut")))
                            return numns < Integer.parseInt(manifestation.getCapacity());
                        return true;
                    })
                    .collect(Collectors.toList());
            if(Integer.parseInt(searchParams.get("sortSelected")) != 0) {

                    switch(Integer.parseInt(searchParams.get("sortSelected"))){
                        case 2://asc
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getName))
                                    .collect(Collectors.toList());
                            break;
                        case 4:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getDate))
                                    .collect(Collectors.toList());
                            break;
                        case 6:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getTicketPrice))
                                    .collect(Collectors.toList());
                            break;
                        case 8:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getLocationAddr))
                                    .collect(Collectors.toList());
                            break;
                        case 1://asc
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getName).reversed())
                                    .collect(Collectors.toList());
                            break;
                        case 3:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getDate).reversed())
                                    .collect(Collectors.toList());
                            break;
                        case 5:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getTicketPrice).reversed())
                                    .collect(Collectors.toList());
                            break;
                        case 7:
                            fpManifs = fpManifs.stream()
                                    .sorted(Comparator.comparing(Manifestation::getLocationAddr).reversed())
                                    .collect(Collectors.toList());
                            break;
                        default:
                            break;
                    }
            }
            if(Boolean.parseBoolean(searchParams.get("isSeller")))
                fpManifs = fpManifs.stream().filter(manifestation -> PopStore.getCurrentUser().getManifestations().stream().anyMatch(manifestation1 -> manifestation1.getId().equals(manifestation.getId()))).collect(Collectors.toList());
            res.body(gson.toJson(fpManifs));
        }

        return res;

    });

    public static Route ShowManifestationDetails = ((req, res) -> {
        res.status(200);
        res.body(gson.toJson(PopStore.getManifestations().stream().filter(manifestation -> manifestation.getId().equals(UUID.fromString(req.params(":id")))).collect(Collectors.toList()).get(0)));
        return res;
    });

    public static Route GetRemainingTickets = ((req, res) -> {
        res.status(200);
        Manifestation m = PopStore.getManifestations().stream().filter(manifestation -> manifestation.getId().equals(UUID.fromString(req.params(":id")))).collect(Collectors.toList()).get(0);
        Long rem = Integer.parseInt(m.getCapacity()) - PopStore.getTickets().stream().filter(ticket -> ticket.getManifestation().getId().equals(m.getId())).count();
        res.body(gson.toJson(rem));
        return res;
    });

    public static Route GetCommentsForManifestation = ((req, res) -> {
        res.status(200);
        res.body(gson.toJson(PopStore.getComments().stream().filter(comment -> comment.getManifestation().getId().equals(UUID.fromString(req.params(":id")))).collect(Collectors.toList())));
        return res;
    });

    private static String GenerateManifestation(HashMap<?,?> json, String txt1, String txt2, Manifestation m){
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse((CharSequence) json.get("date")), LocalTime.parse((CharSequence) json.get("time")));
        if(PopStore.getManifestations().stream().anyMatch(manifestation -> manifestation.getDate().isEqual(dateTime) && manifestation.getLocationAddr().equals((String) json.get("address")))){
            return "Opening date matches a manifestation that already exists.\n" + txt1 + " failed.";
        }
        Manifestation manifestation;
        manifestation = Objects.requireNonNullElseGet(m, Manifestation::new);
        manifestation.setId(UUID.randomUUID());
        manifestation.setActive(false);
        manifestation.setCapacity((String) json.get("capacity"));
        manifestation.setDate(dateTime);
        manifestation.setDeleted(false);
        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setAddress((String) json.get("address"));
        location.setLatitude((Double) json.get("latitude"));
        location.setLongitude((Double) json.get("longitude"));
        manifestation.setLocation(location);
        manifestation.setName((String) json.get("name"));
        String strToFilter = "data:image/png;base64,";
        String filteredStr = (String) json.get("picture");
        manifestation.setPicture(filteredStr.substring(strToFilter.length()));
        manifestation.setRating(0.0);
        manifestation.setTicketPrice(Double.valueOf((String) json.get("ticketPrice")));
        manifestation.setType((String) json.get("type"));
        if(txt1.equals("Creation")) {
            List<Manifestation> manifestations = new ArrayList<>(PopStore.getManifestations());
            manifestations.add(0, manifestation);
            PopStore.setManifestations(manifestations);
            manifestations = new ArrayList<>(PopStore.getCurrentUser().getManifestations());
            manifestations.add(0, manifestation);
            PopStore.getCurrentUser().setManifestations(manifestations);
        }
        return "Manifestation successfully "+txt2+"!";
    }

    public static Route CreateNewManifestation = ((req, res) -> {
        res.status(200);
        var json = gson.fromJson((req.body()), HashMap.class);
        res.body(gson.toJson(GenerateManifestation(json, "Creation", "created", null)));
        return res;
    });

    public static Route ActivateManifestation = ((req, res)->{
        res.status(200);
        for(var manifestation : PopStore.getManifestations()) {
          if(manifestation.getId().equals(UUID.fromString(req.params(":id")))) {
                manifestation.setActive(true);
                break;
          }
        }
        return res;
    });

    public static Route DeleteManifestation = ((req, res)->{
        res.status(200);
        for(var manifestation : PopStore.getManifestations()) {
            if(manifestation.getId().equals(UUID.fromString(req.params(":id")))) {
                manifestation.setDeleted(true);
                break;
            }
        }
        return res;
    });

    public static Route UpdateManifestation = ((req, res)->{
        res.status(200);
        var json = gson.fromJson((req.body()), HashMap.class);
        for(var manifestation : PopStore.getManifestations()) {
            if(manifestation.getId().equals(UUID.fromString((String) json.get("id")))) {
                res.body(gson.toJson(GenerateManifestation(json, "Update", "updated", manifestation)));
                break;
            }
        }
        return res;
    });
}
