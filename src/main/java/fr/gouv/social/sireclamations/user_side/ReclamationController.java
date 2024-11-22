package fr.gouv.social.sireclamations.user_side;

import fr.gouv.social.sireclamations.hexagone.DeposerReclamation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reclamations")
public class ReclamationController {
    private final DeposerReclamation deposerReclamation;

    public ReclamationController(DeposerReclamation deposerReclamation) {
        this.deposerReclamation = deposerReclamation;
    }

    @PostMapping
    public ReclamationApiResponse deposerReclamation(@RequestBody DossierRequest dossierRequest){
        var reclamation = deposerReclamation.executer(dossierRequest.getNumeroDossier());
        return ReclamationApiMapper.toReclamationApiResponse(reclamation);
    }
}
