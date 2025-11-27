package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
@Tag(name = "Module", description = "Gestion des modules d'enseignement")
public class ModuleResource {

    private ModuleBusiness business = new ModuleBusiness();
    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Ajouter un module",
            description = "Ajoute un nouveau module",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Module créé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Échec de l'ajout du module")
            }
    )
    public Response addModule(Module module) {
        boolean ok = business.addModule(module);
        if (ok)
            return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Lister tous les modules",
            description = "Retourne la liste de tous les modules",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des modules récupérée",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Module.class)))
            }
    )
    public Response getModules() {
        return Response.ok(business.getAllModules()).build();
    }

    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Récupérer un module par matricule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Module trouvé",
                            content = @Content(schema = @Schema(implementation = Module.class))),
                    @ApiResponse(responseCode = "404", description = "Module non trouvé")
            }
    )
    public Response getModule(@PathParam("matricule") String mat) {
        Module m = business.getModuleByMatricule(mat);
        if (m != null)
            return Response.ok(m).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{matricule}")
    @Operation(
            summary = "Supprimer un module",
            description = "Supprime un module par son matricule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Module supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Module non trouvé")
            }
    )
    public Response deleteModule(@PathParam("matricule") String mat) {
        boolean ok = business.deleteModule(mat);
        if (ok)
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Modifier un module",
            description = "Modifie un module existant par matricule",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Module modifié avec succès",
                            content = @Content(schema = @Schema(implementation = Module.class))),
                    @ApiResponse(responseCode = "404", description = "Module non trouvé")
            }
    )
    public Response updateModule(@PathParam("matricule") String mat, Module updated) {
        boolean ok = business.updateModule(mat, updated);
        if (ok)
            return Response.ok(updated).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Lister les modules d'une UE",
            description = "Retourne la liste des modules associés à une UE donnée",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Modules récupérés avec succès",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Module.class))),
                    @ApiResponse(responseCode = "404", description = "UE non trouvée")
            }
    )
    public Response getModulesByUE(@QueryParam("codeUE") int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(business.getModulesByUE(ue)).build();
    }
}
