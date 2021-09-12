package controller;

import com.google.gson.Gson;
import domain.*;
import javaxt.utils.string;
import org.eclipse.jetty.util.DateCache;
import spark.Route;
import storage.PopStore;
import utility.PopGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TicketController {

    public static Gson gson = new Gson();

    public static Route GetAllTickets = ((req, res) -> {
        List<Ticket> tickets = null;
        switch (PopStore.getCurrentUser().getRole()) {
            case KUPAC:
                tickets = GetAllTicketsForBuyer();
                res.status(200);
                break;
            case PRODAVAC:
                tickets = GetAllTicketsForSeller();
                res.status(200);
                break;
            case ADMINISTRATOR:
                tickets = GetAllTicketsForAdmin();
                res.status(200);
                break;
            default:
                res.status(400);
                break;
        }

        res.body(gson.toJson(tickets));
        return res;
    });

    private static List<Ticket> GetAllTicketsForBuyer() {
        return PopStore.getTickets()
                        .stream()
                        .filter(ticket -> !ticket.getDeleted())
                        .filter(ticket -> ticket.getBuyer().getId().equals(PopStore.getCurrentUser().getId()))
                        .collect(Collectors.toList());
    }

    private static List<Ticket> GetAllTicketsForSeller() {
        return PopStore.getTickets()
                .stream()
                .filter(ticket -> !ticket.getDeleted())
                .filter(ticket -> PopStore.getCurrentUser().getManifestations().stream().anyMatch(manifestation -> manifestation.getId().equals(ticket.getManifestation().getId())))
                .filter(ticket -> ticket.getStatus() == TicketStatus.REZERVISANA)
                .collect(Collectors.toList());
    }

    private static List<Ticket> GetAllTicketsForAdmin() {
        return PopStore.getTickets()
                .stream()
                .filter(ticket -> !ticket.getDeleted())
                .collect(Collectors.toList());
    }

    public static Route SearchAllTickets = ((req, res) -> {
        List<Ticket> tickets = null;
        switch (PopStore.getCurrentUser().getRole()) {
            case KUPAC:
                tickets = GetAllTicketsForBuyer();
                res.status(200);
                break;
            case PRODAVAC:
                tickets = GetAllTicketsForSeller();
                res.status(200);
                break;
            case ADMINISTRATOR:
                tickets = GetAllTicketsForAdmin();
                res.status(200);
                break;
            default:
                res.status(400);
                return res;
        }

        Map<String, String> searchParams = new HashMap<>();
        req.queryParams().forEach(q -> searchParams.put(q, req.queryParams(q)));

        if (searchParams.getOrDefault("dateFrom", "1900-01-01").equals("")) searchParams.put("dateFrom", "1900-01-01");
        if (searchParams.getOrDefault("dateTo", "1900-01-01").equals("")) searchParams.put("dateTo", "3021-01-01");
        if (searchParams.getOrDefault("priceFrom", "0").equals("")) searchParams.put("priceFrom", "0");
        if (searchParams.getOrDefault("priceTo", "99999999").equals("")) searchParams.put("priceTo", "99999999");
        if (searchParams.get("typeSelected").equals("Select a ticket type:")) searchParams.put("typeSelected", "");
        else searchParams.put("typeSelected", ticketTypeFix(searchParams.get("typeSelected")));

        var resultingTickets = tickets.stream()
                .filter(ticket -> ticket.getManifestation().getName().toLowerCase().contains(searchParams.getOrDefault("manifestationName", ticket.getManifestation().getName()).toLowerCase()))
                .filter(ticket -> ticket.getDate().plusDays(1).toLocalDate().isAfter(LocalDate.parse(searchParams.getOrDefault("dateFrom", "1900-01-01"))))
                .filter(ticket -> ticket.getDate().minusDays(1).toLocalDate().isBefore(LocalDate.parse(searchParams.getOrDefault("dateTo", "3021-01-01"))))
                .filter(ticket -> ticket.getPrice() >= Double.parseDouble(searchParams.getOrDefault("priceFrom", "0")))
                .filter(ticket -> ticket.getPrice() <= Double.parseDouble(searchParams.getOrDefault("priceTo", "99999999")))
                .filter(ticket -> ticket.getType().name().toLowerCase().contains(searchParams.get("typeSelected").toLowerCase()))
                .filter(ticket -> {
                    if (PopStore.getCurrentUser().getRole() != UserRole.PRODAVAC) {
                        Boolean onlyReserved = Boolean.parseBoolean(searchParams.get("onlyReserved"));
                        if (onlyReserved.equals(true)) {
                            return ticket.getStatus() == TicketStatus.REZERVISANA;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (Integer.parseInt(searchParams.get("sortSelected")) != 0) {

            switch (Integer.parseInt(searchParams.get("sortSelected"))){
                case 2: //asc
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(ticket -> ticket.getManifestation().getName()))
                            .collect(Collectors.toList());
                    break;
                case 4:
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(Ticket::getPrice))
                            .collect(Collectors.toList());
                    break;
                case 6:
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(Ticket::getDate))
                            .collect(Collectors.toList());
                    break;
                case 1: //desc
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(ticket -> ticket.getManifestation().getName(), Comparator.reverseOrder()))
                            .collect(Collectors.toList());
                    break;
                case 3:
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(Ticket::getPrice).reversed())
                            .collect(Collectors.toList());
                    break;
                case 5:
                    resultingTickets = resultingTickets.stream()
                            .sorted(Comparator.comparing(Ticket::getDate).reversed())
                            .collect(Collectors.toList());
                    break;
                default:
                    break;
            }
        }

        System.out.println(resultingTickets.size());

        res.body(gson.toJson(resultingTickets));
        return res;
    });

    private static String ticketTypeFix(String type) {
        switch (type) {
            case "Regular":
                return "REGULAR";
            case "Fan pit":
                return "FAN_PIT";
            case "VIP":
                return "VIP";
            default:
                return "ERROR";
        }
    }

    public static Route CheckReservationQuantityAndPrice = ((req, res) -> {
        Map<String, String> queryParams = new HashMap<>();
        req.queryParams().forEach(q -> queryParams.put(q, req.queryParams(q)));
        Integer quantity = Integer.parseInt(queryParams.get("quantity"));
        UUID manifestationId = UUID.fromString(queryParams.get("manifestationId"));
        String ticketTypeStr = queryParams.get("type");
        ticketTypeStr = ticketTypeFix(ticketTypeStr);
        TicketType ticketType = TicketType.valueOf(ticketTypeStr);

        Manifestation m = PopStore.getManifestations().stream().filter(manifestation -> manifestation.getId().equals(manifestationId)).collect(Collectors.toList()).get(0);
        Long remainingTickets = Integer.parseInt(m.getCapacity()) - PopStore.getTickets().stream()
                .filter(ticket -> ticket.getManifestation().getId().equals(m.getId()))
                .filter(ticket -> !ticket.getDeleted())
                .filter(ticket -> ticket.getStatus() == TicketStatus.REZERVISANA)
                .count();

        if (quantity <= remainingTickets) {
            double totalPrice = quantity * m.getTicketPrice();
            if (PopStore.getCurrentUser().getType() != null)
                totalPrice *= PopStore.getCurrentUser().getType().getDiscount();
            if (ticketType == TicketType.FAN_PIT)
                totalPrice *= 2;
            if (ticketType == TicketType.VIP)
                totalPrice *= 4;

            res.body(String.valueOf(totalPrice));
            res.status(200);
        }
        else
            res.status(400);

        return res;
    });

    public static Route MakeReservation = ((req, res) -> {
        var payload = gson.fromJson(req.body(), HashMap.class);
        Integer quantity = Integer.parseInt(String.valueOf(payload.get("quantity")));
        UUID manifestationId = UUID.fromString(String.valueOf(payload.get("manifestationId")));
        String ticketTypeStr = (String) payload.get("type");
        ticketTypeStr = ticketTypeFix(ticketTypeStr);
        TicketType ticketType = TicketType.valueOf(ticketTypeStr);

        Manifestation m = PopStore.getManifestations().stream().filter(manifestation -> manifestation.getId().equals(manifestationId)).collect(Collectors.toList()).get(0);
        Long remainingTickets = Integer.parseInt(m.getCapacity()) - PopStore.getTickets().stream()
                .filter(ticket -> ticket.getManifestation().getId().equals(m.getId()))
                .filter(ticket -> !ticket.getDeleted())
                .filter(ticket -> ticket.getStatus() == TicketStatus.REZERVISANA)
                .count();

        if (quantity <= remainingTickets) {
            double totalPrice = quantity * m.getTicketPrice();
            if (PopStore.getCurrentUser().getType() != null)
                totalPrice *= PopStore.getCurrentUser().getType().getDiscount();
            if (ticketType == TicketType.FAN_PIT)
                totalPrice *= 2;
            if (ticketType == TicketType.VIP)
                totalPrice *= 4;

            double newPoints = 0.0;
            for (int i = 0; i < quantity; i++) {
                Ticket newTicket = new Ticket(UUID.randomUUID(), PopGenerator.generateShortId(10), m, m.getDate(),
                                              totalPrice / quantity, PopStore.getCurrentUser(), TicketStatus.REZERVISANA,
                                              ticketType);
                PopStore.getTickets().add(newTicket);
                newPoints += (totalPrice / quantity) / 1000 * 133;
            }

            PopStore.getCurrentUser().setPoints(PopStore.getCurrentUser().getPoints() + newPoints);
            UserType maxThresholdType = PopStore.getCurrentUser().getType();
            for (var userType : PopStore.getUserTypes()) {
                if (PopStore.getCurrentUser().getPoints() >= userType.getThreshold() &&
                        (maxThresholdType == null || userType.getThreshold() > maxThresholdType.getThreshold())) {
                    maxThresholdType = userType;
                }
            }
            PopStore.getCurrentUser().setType(maxThresholdType);

            res.body(String.valueOf(newPoints));
            res.status(200);
        }
        else
            res.status(400);

        return res;
    });

    public static Route CheckCancellable = ((req, res) -> {
        UUID ticketId = UUID.fromString(req.params(":id"));

        Ticket ticket = PopStore.getTickets().stream().filter(t -> t.getId().equals(ticketId)).collect(Collectors.toList()).get(0);
        LocalDateTime eventDate = ticket.getManifestation().getDate();
        long days = LocalDateTime.now().until(eventDate, ChronoUnit.DAYS);

        if (days < 7)
            res.status(400);
        else
            res.status(200);

        return res;
    });

    public static Route CancelTicket = ((req, res) -> {
        UUID ticketId = UUID.fromString(req.params(":id"));

        Ticket ticket = PopStore.getTickets().stream().filter(t -> t.getId().equals(ticketId)).collect(Collectors.toList()).get(0);
        ticket.setStatus(TicketStatus.ODUSTANAK);
        ticket.setCancellationDate(LocalDateTime.now());

        double lostPoints = ticket.getPrice() / 1000 * 133 * 4;
        PopStore.getCurrentUser().setPoints(PopStore.getCurrentUser().getPoints() - lostPoints);
        if (PopStore.getCurrentUser().getType() != null) {
            if (PopStore.getCurrentUser().getType().getThreshold() > PopStore.getCurrentUser().getPoints()) {
                UserType maxThresholdType = null;
                for (var userType : PopStore.getUserTypes()) {
                    if (userType.getThreshold() <= PopStore.getCurrentUser().getPoints()) {
                        if (maxThresholdType == null)
                            maxThresholdType = userType;
                        else if (userType.getThreshold() > maxThresholdType.getThreshold())
                            maxThresholdType = userType;
                    }
                }
                PopStore.getCurrentUser().setType(maxThresholdType);
            }
        }

        res.body(String.valueOf(lostPoints));
        res.status(200);

        return res;
    });

    public static Route DeleteTicket = ((req, res) -> {
        UUID ticketId = UUID.fromString(req.params(":id"));

        Ticket ticketToDelete = PopStore.getTickets()
                .stream()
                .filter(t -> t.getId().equals(ticketId)).collect(Collectors.toList()).get(0);

        if (ticketToDelete == null) {
            res.status(400);
            return res;
        }

        ticketToDelete.setDeleted(true);
        res.status(200);

        return res;
    });

}
