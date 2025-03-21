package fr.gouv.social.sireclamations.user_side;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gouv.social.sireclamations.hexagone.domain.AutreEtablissement;
import fr.gouv.social.sireclamations.hexagone.domain.Domicile;
import fr.gouv.social.sireclamations.hexagone.domain.DossierDeReclamation;
import fr.gouv.social.sireclamations.hexagone.domain.Etablissement;
import fr.gouv.social.sireclamations.user_side.plateforme_telephonique.DossierDeReclamationDeLaPlateformeTelephoniqueApi;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReclamationApiMapperTest {

  private ReclamationApiMapper reclamationApiMapper;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void mapperUnDossierADomicileAvecMaltraitanceUnServiceADomicileEnCauseEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
             "domicile": {
               "adresse": "31 Avenue Pierre Curie",
               "serviceADomicile": "Service de Soins Infirmier à Domicile (SSIAD)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var domicile =
        new Domicile(
            75010,
            "31 Avenue Pierre Curie",
            "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
            "Service de Soins Infirmier à Domicile (SSIAD)");
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            domicile,
            "Service de Soins Infirmier à Domicile (SSIAD)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }

  @Test
  void mapperUnDossierADomicileAvecMaltraitanceEtUnMembreDeLaFamilleMisEnCauseEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
             "domicile": {
               "adresse": "31 Avenue Pierre Curie"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Membre de la famille",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var domicile =
        new Domicile(
            75010,
            "31 Avenue Pierre Curie",
            "Au domicile (domicile de la victime, domicile d'un membre de la famille, domicile d'un aidant...)",
            null);
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            domicile,
            "Membre de la famille",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }

  @Test
  void mapperUnDossierEnEtablissementDeSanteAvecMaltraitanceParUnProfessionnelDeSanteEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)",
             "etablissementSanitaireEtSocial": {
               "et_finess": "1234567",
               "codeCategorieEtablissement": "340",
               "nomEtablissement": "HOPITAL ST-LAZARE",
               "typeDeMisEnCause": "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var etablissement =
        new Etablissement(
            "1234567",
            340,
            75010,
            "HOPITAL ST-LAZARE",
            "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)");
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            etablissement,
            "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }

  @Test
  void mapperUnDossierEnEtablissementDeSanteAvecMaltraitanceParUnProcheEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)",
             "etablissementSanitaireEtSocial": {
               "et_finess": "1234567",
               "codeCategorieEtablissement": "340",
               "nomEtablissement": "HOPITAL ST-LAZARE"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Proche (ami, voisin...)",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var etablissement =
        new Etablissement(
            "1234567",
            340,
            75010,
            "HOPITAL ST-LAZARE",
            "Dans un établissement de santé (hôpital, clinique, laboratoire, pharmacie ...)");
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            etablissement,
            "Proche (ami, voisin...)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }

  @Test
  void mapperUnDossierEnCabinetMedicalAvecMaltraitanceParUnProfessionnelEnCauseEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Dans un cabinet médical (dentiste, orthopédique, pédiatrie, médecin généraliste...)",
             "cabinetMedical": {
               "adresse": "31 Avenue Pierre Curie",
               "informations": "string",
               "typeDeMisEnCause": "Un professionnel du soin (coiffeur, esthéticienne, naturopathe, ...)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var cabinetMedical =
        new AutreEtablissement(
            75010,
            "31 Avenue Pierre Curie",
            "Dans un cabinet médical (dentiste, orthopédique, pédiatrie, médecin généraliste...)");
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            cabinetMedical,
            "Un professionnel du soin (coiffeur, esthéticienne, naturopathe, ...)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }

  @Test
  void mapperUnDossierAvecMaltraitanceSurUnTrajetParUnProfessionnelEnCauseEtUnMotif()
      throws JsonProcessingException {
    // Given
    String json =
        """
        {
           "id": "1234",
           "lieuSurvenue": {
             "codePostal": "75010",
             "commune": "Paris",
             "natureLieu": "Durant le trajet (transport sanitaire, SAMU, Pompier)",
             "trajet": {
               "typeDeTransport": "Ambulance de secours et de soins d'urgence (ASSU)",
               "nomSociete": "string",
               "typeDeMisEnCause": "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)"
             }
           },
           "misEnCause": {
             "typeDeMisEnCause": "Professionnel",
             "rpps": "string",
             "civilite": "M.",
             "nom": "string",
             "prenom": "string",
             "profession": "string"
           },
           "description": {
             "maltraitance": true,
             "typeDeMaltraitance": [
               "Maltraitance physique (châtiments corporels, agressions physiques, intervention médicale sans consentement éclairé, enfermement...)"
             ],
             "typesDeFaits": [
               "Problème comportemental, relationnel ou de communication avec une personne"
             ],
             "dateSurvenue": "2019-08-24",
             "consequenceSurLaVictime": [
               "Sur la santé physique et/ou psychique (blessures, troubles de la santé ou mentaux...)"
             ],
             "situationToujoursActuelle": "Oui",
             "dateDeFin": "2019-08-24",
             "description": "string"
           }
         }

        """;
    DossierDeReclamationDeLaPlateformeTelephoniqueApi dossierApi =
        objectMapper.readValue(json, DossierDeReclamationDeLaPlateformeTelephoniqueApi.class);

    // When
    var dossierDeReclamationObtenu = reclamationApiMapper.toDossierDeReclamation(dossierApi);

    // Then
    var cabinetMedical =
        new AutreEtablissement(
            75010,
            "31 Avenue Pierre Curie",
            "Durant le trajet (transport sanitaire, SAMU, Pompier)");
    var dossierDeReclamationAttendu =
        new DossierDeReclamation(
            1234,
            cabinetMedical,
            "Un professionnel de santé (médecin, infirmier, aide-soignant, kiné, ostéopathe...)",
            true,
            List.of("Problème comportemental, relationnel ou de communication avec une personne"));

    assertThat(dossierDeReclamationObtenu)
        .usingRecursiveComparison()
        .isEqualTo(dossierDeReclamationAttendu);
  }
}
