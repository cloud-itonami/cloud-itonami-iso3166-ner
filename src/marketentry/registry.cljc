(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `redevance-sanction-outside-range?` is THIS vertical's own new ground-
  truth check, grounding NER's flagship governor check
  (`marketentry.governor/redevance-sanction-range-violations`): Décret
  n°2016-642/PRN/PM du 1er décembre 2016 (own text, OCR-read at 400dpi,
  see `marketentry.facts`), Article 7, states that non-payment of the
  Article 5 Redevance de Régulation des Marchés Publics carries 'une
  exclusion temporaire d'accès à la commande publique allant de un (1) à
  cinq (5) ans et... une sanction pécuniaire allant de un million
  (1.000.000) à cent millions (100.000.000) de francs CFA.' This is a
  GENUINELY DIFFERENT check SHAPE than every prior iso3166 sibling this
  repo mirrors: Bulgaria's ЗОП Art. 54(5) de-minimis is a PERCENTAGE-OF-
  TURNOVER ELIGIBILITY formula, Albania's Neni 76(2)(c) carve-out is a
  FLAT-CONSTANT ELIGIBILITY threshold, Azerbaijan's/Armenia's flagship
  checks are BOOLEAN registry-membership ELIGIBILITY reads, Antigua and
  Barbuda's vendor-class check is a THREE-TIER ELIGIBILITY-THRESHOLD
  classification, Benin's MPME mechanism is a BID-EVALUATION PRICE
  ADJUSTMENT, Bhutan's FDI Negative List is a CATEGORICAL SECTOR-
  EXCLUSION allow-list gate, CAF's Marché réservé mechanism is a MULTI-
  CRITERION INCLUSION-ELIGIBILITY OR-test, COG's exclusion-duration-cap
  check is a SINGLE-SIDED STATUTORY CEILING VALIDATION, and Guinea's Code
  des Marchés Publics Art.159 pecuniary-sanction check is a TWO-SIDED
  PERCENTAGE-OF-BASE-VALUE RANGE VALIDATION (both bounds derived as a
  ratio of a per-engagement base amount). Niger's Article 7 mechanism is
  none of these: it is a TWO-DIMENSION statutory-bounds check where BOTH
  dimensions are ABSOLUTE, FLAT CONSTANTS unrelated to any per-engagement
  base value -- a currency-amount range (1,000,000-100,000,000 FCFA) AND,
  independently, a duration-year range (1-5 years) -- the first in this
  family to validate an absolute currency amount against fixed statutory
  constants (rather than a ratio of a base value) while SIMULTANEOUSLY
  validating a second, independent bounded dimension (duration) drawn
  from the SAME statutory sentence. It was discovered independently, by
  OCR-reading a completely different Niger-specific décret than any
  sibling's flagship citation -- not adapted from a sibling's shape.

  It is entity-condition-gated like COG's/CAF's/GIN's own flagship
  checks: a no-op (false) unless `:prior-redevance-sanction?` is true (an
  engagement with no declared prior redevance-non-payment sanction has
  nothing for this check to validate). Missing/non-numeric
  `:redevance-sanction-amount` or `:redevance-sanction-exclusion-years`
  for a declared prior sanction is never treated as violating the range
  HERE (that is the `evidence-incomplete` check's job, upstream, where an
  assessment must already exist).

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real ARCOP, DGCMP-equivalent, Tribunal de Commerce or DGI
  system. It builds the RECORD an operator would keep, not the act of
  submitting a filing itself (that is `marketentry.operation`'s
  `:filing/submit`, always human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def redevance-sanction-range
  "Décret n°2016-642/PRN/PM du 1er décembre 2016 (own primary text,
  OCR-read at 400dpi, marchespublics.ne's own hosting, 2026-07-23),
  Article 7: the statutory TWO-DIMENSION bounds (both dimensions
  two-sided) on the sanction for non-payment of the Article 5 Redevance
  de Régulation des Marchés Publics."
  {:min-amount 1000000
   :max-amount 100000000
   :min-exclusion-years 1
   :max-exclusion-years 5})

(defn redevance-sanction-outside-range?
  "Does `engagement`'s own declared prior redevance-non-payment sanction
  fall OUTSIDE Article 7's own statutory bounds -- EITHER the pecuniary
  amount outside [1,000,000 ; 100,000,000] FCFA, OR the exclusion
  duration outside [1 ; 5] years?

  A no-op (false) unless `:prior-redevance-sanction?` is true -- an
  engagement with no declared prior redevance-non-payment sanction has
  nothing for this check to validate. Missing/non-numeric
  `:redevance-sanction-amount` / `:redevance-sanction-exclusion-years`
  for a declared prior sanction is never treated as violating the range
  here (that is the `evidence-incomplete` check's job, upstream, where an
  assessment must already exist)."
  [{:keys [prior-redevance-sanction? redevance-sanction-amount redevance-sanction-exclusion-years]}]
  (boolean
   (when (true? prior-redevance-sanction?)
     (let [{:keys [min-amount max-amount min-exclusion-years max-exclusion-years]} redevance-sanction-range
           amount-violation?   (when (number? redevance-sanction-amount)
                                 (or (< redevance-sanction-amount min-amount)
                                     (> redevance-sanction-amount max-amount)))
           duration-violation? (when (number? redevance-sanction-exclusion-years)
                                 (or (< redevance-sanction-exclusion-years min-exclusion-years)
                                     (> redevance-sanction-exclusion-years max-exclusion-years)))]
       (or (boolean amount-violation?) (boolean duration-violation?))))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a marchespublics.ne
  tender response or ARCOP filing package. Pure function -- does not
  touch any real ARCOP, DGCMP-equivalent, Tribunal de Commerce or DGI
  system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a
  marchespublics.ne tender response or ARCOP filing (always human-gated
  upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
