(ns social-explorer.components.petition
  (:require [failjure.core :as f]
    [social-explorer.swapi :as swapi]
    [clojure.data.json :as json]
    [social-explorer.webpage :refer [render-error]]
    [taoensso.timbre :as log]
    ))

(defn petition [swapi-params txid]
  (f/attempt-all [response (swapi/get-transaction swapi-params (cond-> {}
                                                                       txid (assoc :txid txid)))
                  petition (:data response)
                  data (if (get-in petition [:payload :data])
                  (json/read-str (get-in petition [:payload :data])
                                 :key-fn keyword)
                   "")
                  keys (if (get-in petition [:payload :keys])
                  (json/read-str (get-in petition [:payload :keys])
                                 :key-fn keyword)
                   "")
                  ]
                 [:div
                  [:div.hero_petition
                   [:div.petition_info
                    [:h3 "Petition"]
                    [:h5.text-ellipsis txid]
                    ]
                   ]
                  [:div.detail_petition
                   [:h5 "Details"]
                     (if (get-in petition [:payload :data])
                      [:div.details_payload
                       [:div.payload_title "🍱 Payload"]
                       [:div.payload_section
                        [:div.payload_section_title "Data"]
                        [:div.payload_item
                         [:div.detail_title "Beta issuer identifier verify"]
                         [:div.detail_value (get-in data [:issuer_identifier :verify :beta])]
                        ]
                        [:div.payload_item
                         [:div.detail_title "Alpha issuer identifier verify"]
                         [:div.detail_value (get-in data [:issuer_identifier :verify :alpha])]
                        ]
                       ]
                       [:div.payload_section
                         [:div.payload_section_title "Keys identifier"]
                         [:div.payload_item
                          [:div.detail_title "Schema"]
                          [:div.detail_value (get-in keys [:identifier :schema])]
                         ]
                         [:div.payload_item
                          [:div.detail_title "zenroom"]
                          [:div.detail_value (get-in keys [:identifier :zenroom])]
                         ]
                         [:div.payload_item
                          [:div.detail_title "encoding"]
                          [:div.detail_value (get-in keys [:identifier :encoding])]
                         ]
                         [:div.payload_item
                          [:div.detail_title "public"]
                          [:div.detail_value (get-in keys [:identifier :public])]
                         ]
                       ]

                        [:div.payload_item
                         [:div.detail_title "Context Id"]
                         [:div.detail_value (get-in petition [:payload :context-id])]
                        ]
                        [:div.payload_item
                         [:div.detail_title "zencode"]
                         [:div.detail_value (get-in petition [:payload :zencode])]
                        ]
                       ]
                       [:div.payload_item
                        [:div.detail_title "Payload"]
                        [:div.detail_value (:payload petition)]])


                   [:div.details_header
                    [:h6 "🔒 Header"]
                    [:div.detail_item
                     [:div.detail_title "Family version"]
                     [:div.detail_value (get-in petition [:header :family_version] )]
                     ]
                     [:div.detail_item
                      [:div.detail_title "Family name"]
                      [:div.detail_value (get-in petition [:header :family_name] )]
                      ]
                      [:div.detail_item
                       [:div.detail_title "Signer publik key"]
                       [:div.detail_value (get-in petition [:header :signer_public_key] )]
                       ]
                       [:div.detail_item
                        [:div.detail_title "Batcher publik key"]
                        [:div.detail_value (get-in petition [:header :batcher_public_key] )]
                        ]
                    ]
                   ]
                  ]
                  (f/when-failed [response]
                    (render-error response)))
  )
