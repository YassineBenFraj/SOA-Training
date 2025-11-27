package webservices;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "SOA Training API",
                version = "1.0",
                description = "Documentation Swagger pour l’API REST des Modules et UE"
        )
)
@ApplicationPath("/api")
public class SwaggerConfig extends Application {
}
