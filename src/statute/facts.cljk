(ns statute.facts
  "General-law compliance catalog for the Republic of Niger (NER) --
  extends this repo's existing `marketentry.facts` (public-procurement
  market-entry only, narrow scope) with a second, orthogonal catalog of
  statutes a company operating in this jurisdiction must generally track
  for compliance. Mirrors cloud-itonami-iso3166-ben/-btn/-caf/-cog/-gin's
  `statute.facts` (ADR-2607141700, cloud-itonami-compliance-fact-
  federation).

  Every entry below cites an OFFICIAL government-hosted (or, for the
  OHADA/UNCTAD entries, official supranational-body-hosted) URL -- never
  fabricated, all curl/WebFetch/pdftotext-verified 2026-07-22/23:

  - **Companies/commercial-entity law**: this iteration specifically
    investigated, rather than assumed by analogy to the GIN/CAF/COG
    siblings, whether Niger is itself an OHADA member state --
    independently re-confirmed directly on OHADA's own 'Les Etats membres
    de l'OHADA' page (`ohada.org/les-etats-membres-de-lohada/`,
    WebFetch-verified this session, not copied from a sibling's citation),
    which lists 'Niger' among the member states. So, like Benin/CAF/COG/
    GIN, company law is governed DIRECTLY by a SUPRANATIONAL instrument,
    the OHADA Acte uniforme relatif au droit des sociétés commerciales et
    du groupement d'intérêt économique (AUSCGIE) -- this iteration
    independently re-fetched OHADA's own page
    (`ohada.org/droit-des-societes-commerciales-et-du-gie/`,
    WebFetch-verified directly this session, NOT copied from a sibling's
    entry) and confirmed, in OHADA's own words: 'Adoption Date: 30 janvier
    2014 à Ouagadougou (Burkina Faso) -- Publication Date: 04 février
    2014 -- Entry into Force: 05 mai 2014'. Separately, RCCM/business-
    entity REGISTRATION -- as opposed to company FORMATION/governance law
    -- is governed by a DIFFERENT OHADA instrument, the Acte Uniforme
    relatif au Droit Commercial Général (AUDCG; own page independently
    re-fetched this session: 'Adoption Date: 15 décembre 2010 à Lomé
    (Togo) -- Publication Date: 15 février 2011 -- Entry into Force: 15
    mai 2011'); this catalog does not conflate the two
    (`marketentry.facts` cites AUDCG separately for RCCM, performed in
    Niger by the Tribunal de Commerce de Niamey per that court's own
    site, which states directly 'Pour vos démarches et formalités RCCM :
    CLIQUEZ ICI').
  - **Code du Travail (Labour Code)**: Loi n°2012-45 du 25 septembre 2012
    portant Code du travail de la République du Niger -- this iteration
    downloaded Niger's own OFFICIAL Ministère de la Fonction Publique, du
    Travail et de l'Emploi (MFPTE) PDF directly
    (`mfpte.gouv.ne/wp-content/uploads/2025/09/Niger-Code-2012-du-
    travail.pdf`), a NATIVE TEXT-LAYER document (no OCR needed --
    `pdftotext` succeeded directly), own cover/Article 1 text (read
    verbatim, HIGH confidence): 'REPUBLIQUE DU NIGER LOI N° 2012-45 ...
    du 25 septembre 2012 portant Code du travail de la République du
    Niger ... Article premier : Le présent Code régit les rapports entre
    employeurs et travailleurs. Il est applicable sur l'ensemble du
    territoire de la République du Niger.'
  - This iteration also independently confirmed, via UNCTAD's Investment
    Policy Hub (`investmentpolicy.unctad.org/investment-laws/laws/206/
    niger-code-des-investissements-`, WebFetch-verified directly), a
    further Niger-specific statute relevant to compliance: LOI N° 2014-09
    du 16 avril 2014 portant Code des investissements en République du
    Niger, whose own Article 3 (per UNCTAD's hosting) establishes 'un
    guichet unique créé à cet effet auprès du Ministre en charge de
    l'Industrie' -- see `marketentry.facts`'s namespace docstring for the
    honestly-flagged, UNRESOLVED gap over whether ANPIPS (Agence
    Nigérienne pour la Promotion des Investissements Privés et des Projets
    Stratégiques, anpips.ne) is in fact today's incarnation of that
    guichet unique (no explicit statement to that effect was found on
    ANPIPS's own site). This iteration did NOT independently fetch the
    Code des Investissements' own primary (Journal-Officiel-hosted) text,
    only UNCTAD's secondary hosting of it -- an honest gap, similar in
    kind to GIN's honest gap over its own Code des Investissements.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"NER"
   [{:statute/id "ner.ohada-auscgie"
     :statute/title "Acte uniforme relatif au droit des sociétés commerciales et du groupement d'intérêt économique (AUSCGIE)"
     :statute/jurisdiction "NER"
     :statute/kind :law
     :statute/law-number "OHADA Uniform Act -- adopted 30 January 2014 (Ouagadougou), published in the OHADA Official Journal 4 February 2014, in force 5 May 2014; directly applicable in the Republic of Niger as an OHADA member state (own text independently confirmed on ohada.org's own member-states page, 2026-07-23), no domestic transposition act required"
     :statute/url "https://www.ohada.org/droit-des-societes-commerciales-et-du-gie/"
     :statute/url-provenance :official-ohada-org
     :statute/enacted-date "2014-01-30"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "ner.code-du-travail-2012"
     :statute/title "Code du travail de la République du Niger"
     :statute/jurisdiction "NER"
     :statute/kind :law
     :statute/law-number "Loi n° 2012-45 du 25 septembre 2012 (own primary text, native text-layer PDF, read directly via pdftotext from Niger's own Ministère de la Fonction Publique, du Travail et de l'Emploi; HIGH confidence, no OCR required)"
     :statute/url "https://www.mfpte.gouv.ne/wp-content/uploads/2025/09/Niger-Code-2012-du-travail.pdf"
     :statute/url-provenance :official-ministry-mfpte-gouv-ne
     :statute/enacted-date "2012-09-25"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :employment}}
    {:statute/id "ner.code-des-investissements-2014"
     :statute/title "Code des investissements en République du Niger"
     :statute/jurisdiction "NER"
     :statute/kind :law
     :statute/law-number "Loi n° 2014-09 du 16 avril 2014 (per UNCTAD's Investment Policy Hub hosting; this iteration did not independently fetch the law's own Journal-Officiel-hosted primary text, an honest gap)"
     :statute/url "https://investmentpolicy.unctad.org/investment-laws/laws/206/niger-code-des-investissements-"
     :statute/url-provenance :secondary-unctad-investment-policy-hub
     :statute/enacted-date "2014-04-16"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:investment}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ner statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "NER")) " NER statutes seeded with an "
                 "official citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :corporate-governance)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
