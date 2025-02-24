package fr.gouv.social.sireclamations.hexagone.domain.ports;

public interface ReferentielDesAutoritesCompetentesParMisEnCausePourMaltraitance {
  String recupererAutoriteCompetente(String libelleDuMisEnCausePourMaltraitance);
}
