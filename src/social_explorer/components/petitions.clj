(ns social-explorer.components.petitions
  (:require [failjure.core :as f]
    [social-explorer.swapi :as swapi]
    [social-explorer.webpage :refer [render-error]]
    [taoensso.timbre :as log]
    ))


(defn petitions [swapi-params query-params]
  (f/attempt-all [response (swapi/list-transactions swapi-params (cond-> {}
                                                                         query-params (merge query-params)))
                  petitions (:data response)
                  ]

                 [:div
                  [:div.hero_petitions
                   [:div.hero_content
                    [:h3.hero_title "Search a petition"]
                    [:input.form-input {:placeholder "Type the petitionId..."}]

                    ]
                   ]

                  [:div.list
                   [:h4.list_title "Petitions list"]
                (for [p petitions]
                     [:div.list_item
                      [:div.item_info
                       [:h5.item_title.text-ellipsis (str (:header_signature p))]
                       [:div.item_date "26 jan 2019"]
                       ]
                      [:div.item_actions
                       [:a.btn.btn-primary "Details"]
                       ]
                      ]
                     )
                   ]
                  ]
                  (f/when-failed [response]
                                 (render-error response)))
)
