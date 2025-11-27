package webservices;

import entities.UniteEnseignement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UE")
@Tag(name = "Unité d'Enseignement", description = "Gestion des UEs")
public class UniteEnseignementResource {

    private UniteEnseignementBusiness business = new UniteEnseignementBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Operation(
            summary = "Ajouter une UE",
            description = "Ajoute une nouvelle unité d'enseignement",
            responses = {
                    @ApiResponse(responseCode = "201", description = "UE créée avec succès"),
                    @ApiResponse(responseCode = "404", description = "Échec de l'ajout de l'UE")
            }
    )
    public Response addUE(UniteEnseignement ue) {
        boolean ok = business.addUniteEnseignement(ue);
        if (ok)
            return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Lister les UEs",
            description = "Retourne toutes les unités d'enseignement ou filtre par code/semestre",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des UEs récupérée avec succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UniteEnseignement.class))),
                    @ApiResponse(responseCode = "404", description = "UE non trouvée")
            }
    )
    public Response getUE(
            @QueryParam("semestre") Integer semestre,
            @QueryParam("code") Integer code
    ) {
        if (code != null) {
            UniteEnseignement ue = business.getUEByCode(code);
            if (ue != null)
                return Response.ok(ue).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (semestre != null) {
            List<UniteEnseignement> list = business.getUEBySemestre(semestre);
            return Response.ok(list).build();
        }

        return Response.ok(business.getListeUE()).build();
    }

    @DELETE
    @Path("/{code}")
    @Operation(
            summary = "Supprimer une UE",
            description = "Supprime une UE par son code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "UE supprimée avec succès"),
                    @ApiResponse(responseCode = "404", description = "UE non trouvée")
            }
    )
    public Response deleteUE(@PathParam("code") int code) {
        boolean ok = business.deleteUniteEnseignement(code);
        if (ok)
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 5) Modifier une UE
    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Modifier une UE",
            description = "Modifie une UE existante selon son code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "UE modifiée avec succès",
                            content = @Content(schema = @Schema(implementation = UniteEnseignement.class))),
                    @ApiResponse(responseCode = "404", description = "UE non trouvée")
            }
    )
    public Response updateUE(@PathParam("code") int code, UniteEnseignement ue) {
        boolean ok = business.updateUniteEnseignement(code, ue);
        if (ok)
            return Response.ok(ue).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
