(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Republic of Niger procurement law, whether a claimed
  engagement fee actually equals base + months x rate, whether an
  engagement's own declared prior Redevance de Régulation des Marchés
  Publics non-payment sanction actually stays within Décret n°2016-642/
  PRN/PM's own Article 7 statutory bounds, whether a NIF (Numéro
  d'Identification Fiscale) record has been verified for a filing that
  requires it, or when a draft stops being a draft and becomes a real-
  world marchespublics.ne tender response / ARCOP filing, so this MUST be
  a separate system able to *reject* a proposal and fall back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual marchespublics.ne tender response or ARCOP filing
  submission requires Market-Entry Compliance Governor clearance and
  always escalates to human sign-off'; 'a false or fabricated
  regulatory-requirement claim is a HARD hold') names exactly the checks
  below.

  Six checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. Redevance sanction outside
       statutory bounds             -- for `:filing/submit`, when the
                                       engagement declares
                                       `:prior-redevance-sanction? true`,
                                       INDEPENDENTLY recompute whether
                                       its own declared
                                       `:redevance-sanction-amount` and
                                       `:redevance-sanction-exclusion-
                                       years` fall within Décret n°2016-
                                       642/PRN/PM's own Article 7
                                       statutory bounds (1,000,000-
                                       100,000,000 FCFA and 1-5 years
                                       respectively), and HARD-hold if
                                       EITHER falls outside its own
                                       bound. FLAGSHIP genuinely new
                                       check for the iso3166 family
                                       (grep-verified absent as a
                                       governor check function name
                                       fleet-wide at build time) -- a
                                       TWO-DIMENSION statutory-bounds
                                       validation where BOTH dimensions
                                       are ABSOLUTE FLAT CONSTANTS
                                       unrelated to any per-engagement
                                       base value, a check SHAPE
                                       genuinely different from every
                                       prior sibling's (see
                                       `marketentry.registry`'s
                                       docstring for the full
                                       comparison).
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. NIF record unverified        -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-nif? true`,
                                       INDEPENDENTLY check
                                       `:nif-verified?`. CONDITIONAL
                                       on the engagement's own ground
                                       truth. Grounded in ARCOP's own
                                       live 'Demande d'Attestation'
                                       service, which requires a NIF
                                       for an Attestation de situation
                                       fiscale régulière (see
                                       `marketentry.facts`).
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real marchespublics.ne tender-response/ARCOP-filing
  package and submitting it are the two real-world actuation events
  this actor performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(RCCM登録/NIF税務記録/ARCOP非排除確認・非未納確認/ANPIPS投資登録・代理人確認等)が充足していない状態での提案"}]))))

(defn- redevance-sanction-range-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own declared prior redevance-non-payment sanction stays
  within Décret n°2016-642/PRN/PM's own Article 7 statutory bounds --
  the flagship check this vertical adds. HARD-hold when the engagement
  declares `:prior-redevance-sanction? true` but the declared amount OR
  exclusion duration falls outside its own bound."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/redevance-sanction-outside-range? e)
        [{:rule :redevance-sanction-range-violated
          :detail (str subject " の申告Redevance非納制裁(金額=" (:redevance-sanction-amount e)
                      " FCFA, 排除期間=" (:redevance-sanction-exclusion-years e)
                      "年)がDécret n°2016-642/PRN/PM Art.7の法定範囲"
                      "(100万-1億FCFA, 1-5年)の外にある -- 提出提案は進められない")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- nif-record-unverified-violations
  "For `:filing/submit`, when the engagement declares `:requires-nif?
  true`, INDEPENDENTLY check `:nif-verified?` -- CONDITIONAL on the
  engagement's own ground truth. Grounded in ARCOP's own live 'Demande
  d'Attestation' service, which requires a NIF (Numéro d'Identification
  Fiscale) for an Attestation de situation fiscale régulière."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-nif? e))
                 (not (true? (:nif-verified? e))))
        [{:rule :nif-record-unverified
          :detail (str subject " はNIF(Numéro d'Identification Fiscale)確認を"
                      "要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (redevance-sanction-range-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (nif-record-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
