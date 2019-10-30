(ns social-explorer.components.petition
  (:require [failjure.core :as f]
    [social-explorer.swapi :as swapi]
    [cheshire.core :refer :all]
    [social-explorer.webpage :refer [render-error]]
    [taoensso.timbre :as log]
    ))

(defn petition [swapi-params txid history]
  (f/attempt-all [response (swapi/get-transaction swapi-params (cond-> {}
                                                                 txid (assoc :txid txid)))
                  petition (:data response)
                  payload (if (get-in petition [:payload])
                            (get-in petition [:payload])
                            "")
                  keys (if (get-in petition [:payload :keys])
                         (get-in petition [:payload :keys])
                         "")
                  ]
                 [:div
                  [:div.hero_petition
                   [:div.container.grid-lg
                    [:a.btn {:href (str history "#" txid)} [:i.icon.icon-arrow-left] "Back"]
                    (if (:petition_id payload)
                      [:div.petition_info_name
                       [:h3 "Petition id"]
                       [:h5 (:petition_id payload)]]
                      [:div])
                    
                    [:h3 "Transaction"]
                    [:h5.transactionId txid]]]
                  [:div.detail_petition.container.grid-lg
                   (if (get-in keys [:zenroom :curve])
                     [:div.zenroom
                      [:h5 "zenroom info"]
                      [:div.columns
                       [:div.payload_item.column.col-3
                        [:div.detail_title "curve"]
                        [:div.detail_value (get-in keys [:zenroom :curve])]]
                       [:div.payload_item.column.col-3
                        [:div.detail_title "scenario"]
                        [:div.detail_value (get-in keys [:zenroom :scenario])]]
                       [:div.payload_item.column.col-3
                        [:div.detail_title "encoding"]
                        [:div.detail_value (get-in keys [:zenroom :encoding])]]
                       [:div.payload_item.column.col-3
                        [:div.detail_title "version"]
                        [:div.detail_value (get-in keys [:zenroom :version])]]]]
                     [:div])

                   [:div.payload.block
                    [:h5.block_title "Payload"]
                    [:button.btn.tooltip.btn-action {
                                                     :data-tooltip "Copy the payload"
                                                     :data-clipboard-target "#text"
                                                     :id "copy"} [:i.icon.icon-copy]]
                    [:pre [:code.json {:id "text"} (generate-string payload {:pretty true})]]]

                   [:div.petition_keys
                    [:div.columns
                     [:div.column.col-12
                      [:div.verifier.block
                       [:h3.block_title "Header"]
                       [:div.payload_item
                        [:div.detail_title "signer public key"]
                        [:div.hash_value (get-in petition [:header :signer_public_key])]]
                       [:div.payload_item
                        [:div.detail_title "dependencies"]
                        [:div.hash_value (for [p (get-in petition [:header :dependencies])]
                                           [:div p])]]
                       [:div.payload_item
                        [:div.detail_title "batcher public key"]
                        [:div.hash_value (get-in petition [:header :batcher_public_key])]]
                       [:div.payload_item
                        [:div.detail_title "outputs"]
                        [:div.hash_value (for [p (get-in petition [:header :outputs])]
                                           [:div p]
                                           )]]
                       [:div.payload_item
                        [:div.detail_title "family version"]
                        [:div.hash_value (get-in petition [:header :family_version])]]
                       [:div.payload_item
                        [:div.detail_title "inputs"]
                        [:div.hash_value (for [p (get-in petition [:header :inputs])]
                                           [:div p])]]
                       [:div.payload_item
                        [:div.detail_title "nonce"]
                        [:div.hash_value (get-in petition [:header :nonce])]]
                       [:div.payload_item
                        [:div.detail_title "family name"]
                        [:div.hash_value (get-in petition [:header :family_name])]]
                       [:div.payload_item
                        [:div.detail_title "payload sha512"]
                        [:div.hash_value (get-in petition [:header :payload_sha512])]]
                       [:div.payload_item
                        [:div.detail_title "header_signature"]
                        [:div.hash_value (get-in petition [:header_signature])]]
                       ]]
                     
                     ]]]]
                  ;  (if (get-in petition [:payload :data])
                  ;    [:div.details_payload
                  ;     [:div.payload_title "🍱 Payload"]
                  ;     [:div.payload_section
                  ;      [:div.payload_section_title "Data"]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "Beta issuer identifier verify"]
                  ;       [:div.detail_value (get-in data [:issuer_identifier :verify :beta])]
                  ;       ]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "Alpha issuer identifier verify"]
                  ;       [:div.detail_value (get-in data [:issuer_identifier :verify :alpha])]
                  ;       ]
                  ;      ]
                  ;     [:div.payload_section
                  ;      [:div.payload_section_title "Keys identifier"]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "Schema"]
                  ;       [:div.detail_value (get-in keys [:identifier :schema])]
                  ;       ]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "zenroom"]
                  ;       [:div.detail_value (get-in keys [:identifier :zenroom])]
                  ;       ]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "encoding"]
                  ;       [:div.detail_value (get-in keys [:identifier :encoding])]
                  ;       ]
                  ;      [:div.payload_item
                  ;       [:div.detail_title "public"]
                  ;       [:div.detail_value (get-in keys [:identifier :public])]
                  ;       ]
                  ;      ]
                  
                  ;     [:div.payload_item
                  ;      [:div.detail_title "Context Id"]
                  ;      [:div.detail_value (get-in petition [:payload :context-id])]
                  ;      ]
                  ;     [:div.payload_item
                  ;      [:div.detail_title "zencode"]
                  ;      [:div.detail_value (get-in petition [:payload :zencode])]
                  ;      ]
                  ;     ]
                  ;    [:div.payload_item
                  ;     [:div.detail_title "Payload"]
                  ;     [:div.detail_value (:payload petition)]])
                  

                  ;  [:div.details_header
                  ;   [:h6 "🔒 Header"]
                  ;   [:div.detail_item
                  ;    [:div.detail_title "Family version"]
                  ;    [:div.detail_value (get-in petition [:header :family_version] )]
                  ;    ]
                  ;   [:div.detail_item
                  ;    [:div.detail_title "Family name"]
                  ;    [:div.detail_value (get-in petition [:header :family_name] )]
                  ;    ]
                  ;   [:div.detail_item
                  ;    [:div.detail_title "Signer publik key"]
                  ;    [:div.detail_value (get-in petition [:header :signer_public_key] )]
                  ;    ]
                  ;   [:div.detail_item
                  ;    [:div.detail_title "Batcher publik key"]
                  ;    [:div.detail_value (get-in petition [:header :batcher_public_key] )]
                  ;    ]
                  ;   ]
                  ;  ]
                  ; ]
                  (f/when-failed [response]
                    (render-error response)))
  )
