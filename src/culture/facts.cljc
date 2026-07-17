(ns culture.facts
  "Country-level regional-culture catalog for Niger (NER) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"NER"
   [{:culture/id "ner.dish.tuwo"
     :culture/name "Tuwo"
     :culture/country "NER"
     :culture/kind :dish
     :culture/summary "Thick porridge-like swallow food (e.g. tuwon masara) eaten primarily by the Hausa and Fulani; also eaten in Niger, Mali, Cameroon and other West African countries."
     :culture/url "https://en.wikipedia.org/wiki/Tuwo"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.dish.dambou"
     :culture/name "Dambou"
     :culture/country "NER"
     :culture/kind :dish
     :culture/summary "Dish of shredded leafy greens native to the Zarma and Songhai peoples of southwestern Niger, traditionally eaten at festive occasions such as weddings."
     :culture/url "https://en.wikipedia.org/wiki/Dambou"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.dish.kilishi"
     :culture/name "Kilishi"
     :culture/country "NER"
     :culture/kind :dish
     :culture/summary "Spiced, sun-dried meat (a form of jerky) originating in Hausaland, spanning southern Niger and northern Nigeria; some accounts credit its origin to the Hausa people of Niger."
     :culture/url "https://en.wikipedia.org/wiki/Kilishi"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.beverage.fura-da-nono"
     :culture/name "Fura da nono"
     :culture/country "NER"
     :culture/kind :beverage
     :culture/summary "Drink of fermented milk (nono) mixed with balls of ground millet dough (fura), popular among the Zarma-Songhai, Fulani and Hausa peoples of the Sahel and also eaten in Niger."
     :culture/url "https://en.wikipedia.org/wiki/Fura_(food)"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.product.violet-de-galmi"
     :culture/name "Violet de Galmi"
     :culture/name-local "Oignon violet de Galmi"
     :culture/country "NER"
     :culture/kind :product
     :culture/summary "Purple-skinned onion variety cultivated in the Galmi region of Niger, selected as a local ecotype by the IRAT research institute in 1975 and prized across West Africa for its pungent flavor."
     :culture/url "https://fr.wikipedia.org/wiki/Oignon_violet_de_Galmi"
     :culture/url-provenance :wikipedia-fr
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.craft.agadez-cross"
     :culture/name "Agadez Cross"
     :culture/name-local "Croix d'Agadez"
     :culture/country "NER"
     :culture/kind :craft
     :culture/summary "Silver pendant made by the lost-wax casting process, produced especially by the Tuareg people of Niger, in particular the Kel Aïr and Kel Geress tribes."
     :culture/url "https://en.wikipedia.org/wiki/Agadez_Cross"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.festival.gerewol"
     :culture/name "Gerewol"
     :culture/name-local "Guérewol"
     :culture/country "NER"
     :culture/kind :festival
     :culture/summary "Annual courtship ritual competition among the Wodaabe Fula people of Niger, held at the end of the rainy season around the Cure Salée gathering at In-Gall."
     :culture/url "https://en.wikipedia.org/wiki/Gerewol"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.festival.cure-salee"
     :culture/name "Cure Salée"
     :culture/country "NER"
     :culture/kind :festival
     :culture/summary "Yearly late-September gathering (\"Salt Cure\") of Tuareg and Wodaabe peoples in the northern Niger town of In-Gall, marking the end of the rainy season with herds brought to graze at the salt flats."
     :culture/url "https://en.wikipedia.org/wiki/Cure_Sal%C3%A9e"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.heritage.air-tenere-reserve"
     :culture/name "Aïr and Ténéré National Nature Reserve"
     :culture/country "NER"
     :culture/kind :heritage
     :culture/summary "Niger's largest protected area, covering the Aïr Mountains and the Ténéré desert; designated a UNESCO World Heritage Site in 1991 and home to threatened species such as the addax and dorcas gazelle."
     :culture/url "https://en.wikipedia.org/wiki/A%C3%AFr_and_T%C3%A9n%C3%A9r%C3%A9_National_Nature_Reserve"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ner.heritage.agadez"
     :culture/name "Historic Centre of Agadez"
     :culture/country "NER"
     :culture/kind :heritage
     :culture/summary "Historic centre of the Saharan trading city of Agadez, capital of the former Sultanate of Agadez, including the Agadez Grand Mosque (originally 1515, rebuilt 1844); inscribed as a UNESCO World Heritage Site in 2013."
     :culture/url "https://en.wikipedia.org/wiki/Agadez"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ner culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "NER"))
                 " NER entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
