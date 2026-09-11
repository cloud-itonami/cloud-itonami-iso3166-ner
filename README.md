# cloud-itonami-iso3166-ner

**NER**: Republic of Niger.

- ARCOP (Autorité de Régulation de la Commande Publique -- formerly ARMP,
  renamed by Loi N°2022-46 du 12 décembre 2022) public procurement;
  Direction Générale du Contrôle des Marchés Publics et des Engagements
  Financiers a priori/a posteriori control; Code des marchés publics
  (Décret n° 2022-743/PRN/PM du 29 septembre 2022, en application de la
  Loi N°2011-37 du 28 Octobre 2011, amended by Décret N°2026-0099/PRN/PM
  du 11 février 2026); four UEMOA procurement directives transposed
  (04/2005, 05/2005, 02/2014, 04/2012 CM/UEMOA)
- OHADA RCCM (Registre du Commerce et du Crédit Mobilier) company/
  business registration via the Tribunal de Commerce de Niamey; NIF
  (Numéro d'Identification Fiscale) tax registration; ANPIPS (Agence
  Nigérienne pour la Promotion des Investissements Privés et des Projets
  Stratégiques) investment guichet unique
- Décret n°2016-642/PRN/PM du 1er décembre 2016 Article 7 redevance-de-
  régulation non-payment sanction regime -- a two-dimension statutory
  bounds gate (1,000,000-100,000,000 FCFA pecuniary sanction AND 1-5
  year exclusion range) on any prior ARCOP sanction for non-payment of
  the Article 5 procurement regulatory levy (flagship check)

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-gin`/`-caf`/`-cog` (the closest architectural
match: Guinea is also a Francophone OHADA member state, and Niger shares
Guinea's ARMP-renamed-and-split-regulator shape while additionally being
a UEMOA member state, a genuinely relevant structural fact this iteration
verified rather than assumed):

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Code des
  marchés publics (Décret n° 2022-743/PRN/PM du 29 septembre 2022,
  implementing Loi N°2011-37 du 28 Octobre 2011, amended by Décret
  N°2026-0099/PRN/PM du 11 février 2026 -- title/number/date confirmed on
  two independent ARCOP document listings, full article text not
  independently fetched this session, an honest gap); ARCOP (Autorité de
  Régulation de la Commande Publique, renamed from ARMP by Loi N°2022-46
  du 12 décembre 2022) and the Direction Générale du Contrôle des Marchés
  Publics et des Engagements Financiers (a priori/a posteriori control,
  per Décret n°2014-070/PRN/ME/F du 12 février 2014); RCCM (OHADA Acte
  Uniforme relatif au Droit Commercial Général, performed by the Tribunal
  de Commerce de Niamey per that court's own site) and NIF tax
  registration. `governor.cljc`'s flagship check independently
  recomputes whether an engagement's own declared prior redevance-non-
  payment sanction (amount AND exclusion duration) falls within Décret
  n°2016-642/PRN/PM's own Article 7 statutory bounds -- a TWO-DIMENSION
  ABSOLUTE-CONSTANT range validation (currency amount + duration years,
  neither derived from any per-engagement base value), a check shape
  genuinely different from every other iso3166 sibling's (see the
  namespace docstrings for the full research trail and honestly-narrowed
  scope, including this iteration's own OCR-reading of a scanned décret
  PDF and an honestly-flagged, unresolved gap over ANPIPS's identity as
  the Code des Investissements' own Article 3 guichet unique).
- `src/statute/facts.cljk` -- general-law catalog: the OHADA Uniform Act
  on Commercial Companies (AUSCGIE, directly applicable, no domestic
  transposition act -- Niger's own OHADA membership independently
  re-confirmed this session), the Code du Travail (Loi n°2012-45 du 25
  septembre 2012, confirmed from a native text-layer PDF hosted on
  Niger's own Ministère de la Fonction Publique, du Travail et de
  l'Emploi site, no OCR required), and the Code des Investissements (Loi
  n°2014-09 du 16 avril 2014, per UNCTAD's Investment Policy Hub
  hosting).

Every citation is curl/WebFetch-verified against an official source
(arcop.ne, its legacy predecessor armp-niger.org via the Wayback Machine,
marchespublics.ne, uemoa.int, ohada.org, tribunalcommerceniamey.ne,
mfpte.gouv.ne, investmentpolicy.unctad.org, anpips.ne); Décret n°2016-
642/PRN/PM's own PDF is a scanned image with no embedded text layer, read
in full via French-language Tesseract OCR at 400dpi across all three
pages, including its own signature page -- see `marketentry.facts`'s
docstring for exactly which facts are HIGH confidence vs. an honestly-
flagged gap.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Niger:

- `src/culture/facts.cljk` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
