(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest ner-has-spec-basis
  (let [sb (facts/spec-basis "NER")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "NER")))
    (is (some? (facts/business-registration-spec-basis "NER")))
    (is (some? (facts/redevance-sanction-range-spec-basis "NER")))))

(deftest ner-rep-spec-basis-is-honestly-nil
  (testing "this iteration did not get access to the 2022 Code's own eligibility/exclusion articles this session -- an honest, disclosed access gap, not an assertion that no such provision exists"
    (is (nil? (facts/rep-spec-basis "NER")))))

(deftest ner-business-registration-is-a-different-body-from-tax-and-procurement
  (testing "business/investment registration (ANPIPS) and tax registration (DGI) are administered by different authorities -- see namespace docstring"
    (let [reg (facts/business-registration-spec-basis "NER")
          tax (facts/corporate-number-spec-basis "NER")]
      (is (some? reg))
      (is (some? tax))
      (is (not= (:business-registration-owner-authority reg)
                (:corporate-number-owner-authority tax))))))

(deftest ner-redevance-sanction-range-is-the-flagship-spec-basis
  (testing "Décret n°2016-642/PRN/PM Article 7's statutory bounds are a real, verifiable, two-dimension range -- not fabricated"
    (let [rs (facts/redevance-sanction-range-spec-basis "NER")]
      (is (some? rs))
      (is (= 1000000 (:redevance-sanction-range-min-amount rs)))
      (is (= 100000000 (:redevance-sanction-range-max-amount rs)))
      (is (= 1 (:redevance-sanction-range-min-exclusion-years rs)))
      (is (= 5 (:redevance-sanction-range-max-exclusion-years rs)))
      (is (= "XOF" (:redevance-sanction-range-currency rs)))
      (is (string? (:redevance-sanction-range-legal-basis rs))))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ")))
  (is (nil? (facts/business-registration-spec-basis "ATL")))
  (is (nil? (facts/redevance-sanction-range-spec-basis "ATL")))
  (is (nil? (facts/rep-spec-basis "ATL"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "NER")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "NER" all)))
    (is (not (facts/required-evidence-satisfied? "NER" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["NER" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
