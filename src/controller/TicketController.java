package controller;

import com.google.gson.Gson;
import domain.Manifestation;
import domain.Ticket;
import domain.TicketStatus;
import javaxt.utils.string;
import spark.Route;
import storage.PopStore;

import java.time.LocalDate;
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

    public static Route SearchAllTickets = ((req, res) -> {
        List<Ticket> tickets = null;
        switch (PopStore.getCurrentUser().getRole()) {
            case KUPAC:
                tickets = GetAllTicketsForBuyer();
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
                    Boolean onlyReserved = Boolean.parseBoolean(searchParams.get("onlyReserved"));
                    if (onlyReserved.equals(true)) {
                        return ticket.getStatus() == TicketStatus.REZERVISANA;
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

}
