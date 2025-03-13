package fr.gouv.social.sireclamations.hexagone.domain.ports;

public interface ReferentielDesAutoritesCompetentesParMisEnCauseEnEtablissement {
  String recupererAutoriteCompetente(String libelleDuMisEnCause);
}
