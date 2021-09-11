package controller;

import com.google.gson.Gson;
import domain.Comment;
import domain.Manifestation;
import domain.TicketStatus;
import domain.UserRole;
import spark.Route;
import storage.PopStore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommentController {
    private static Gson gson = new Gson();

    public static Route CheckCanComment = ((req, res) ->{
        res.status(200);
        if(PopStore.getCurrentUser() != null)
        {if(PopStore.getCurrentUser().getRole().equals(UserRole.KUPAC)){
            for(var ticket : PopStore.getTickets()){
                Boolean ret = ticket.getManifestation().getId().equals(UUID.fromString(req.params(":id"))) && ticket.getManifestation().getDate().isBefore(LocalDateTime.now()) && ticket.getStatus().equals(TicketStatus.REZERVISANA) && ticket.getBuyer().getId().equals(PopStore.getCurrentUser().getId());
                System.out.println(req.params(":id"));
                System.out.println(ticket.getManifestation().getId().equals(UUID.fromString(req.params(":id"))));
                System.out.println(ticket.getManifestation().getDate().isBefore(LocalDateTime.now()));
                System.out.println(ticket.getBuyer().getId().equals(PopStore.getCurrentUser().getId()));
                res.body(gson.toJson(ret.toString()));
                if(ret) return res;
            }
        }
        else
            res.body(gson.toJson("false"));}
        else
            res.body(gson.toJson("false"));
        return res;
    });

    public static Route AddComment = ((req, res) -> {
        var json = gson.fromJson((req.body()), HashMap.class);
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setManifestation(PopStore.getManifestations().stream().filter(manifestation -> manifestation.getId().equals(UUID.fromString((String) json.get("manifestationId")))).collect(Collectors.toList()).get(0));
        comment.setRating((int) Math.round((Double) json.get("rating")));
        comment.setText((String) json.get("text"));
        comment.setUser(PopStore.getCurrentUser());
        comment.setDeleted(false);
        comment.setApproved(false);
        comment.setRejected(false);
        PopStore.getComments().add(comment);
        res.status(200);
        return res;
    });

    public static void CalcManifestationRating(Manifestation m){
        var div1 = PopStore.getComments().stream().filter(comment -> comment.getManifestation().getId().equals(m.getId())).filter(comment -> !comment.getDeleted()).filter(Comment::getApproved).filter(comment -> !comment.getRejected()).mapToDouble(Comment::getRating).sum();
        var div2 = PopStore.getComments().stream().filter(comment -> comment.getManifestation().getId().equals(m.getId())).filter(comment -> !comment.getDeleted()).filter(Comment::getApproved).filter(comment -> !comment.getRejected()).count();
        m.setRating(Math.round((div2 == 0 ? 0.0 : div1/div2) * 100.0) / 100.0);
    }

    public static Route ApproveComment = ((req, res) -> {
        var comments = PopStore.getComments().stream().filter(comment -> {
            if(comment.getId().equals(UUID.fromString(req.params(":id")))) {
                comment.setApproved(true);
                CalcManifestationRating(comment.getManifestation());
            }
            return false;
        }).collect(Collectors.toList());
        res.status(200);
        return res;
    });

    public static Route DeleteComment = ((req, res) -> {
        var comments = PopStore.getComments().stream().filter(comment -> {
            if(comment.getId().equals(UUID.fromString(req.params(":id")))) {
                comment.setDeleted(true);
                CalcManifestationRating(comment.getManifestation());
            }
            return false;
        }).collect(Collectors.toList());
        res.status(200);
        return res;
    });

    public static Route RejectComment = ((req, res) -> {
        var comments = PopStore.getComments().stream().filter(comment -> {
            if(comment.getId().equals(UUID.fromString(req.params(":id")))) {
                comment.setRejected(true);
                CalcManifestationRating(comment.getManifestation());
            }
            return false;
        }).collect(Collectors.toList());
        res.status(200);
        return res;
    });
}
