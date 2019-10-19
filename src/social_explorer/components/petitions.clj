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
                  link (atom {})]
                 [:div.navbar-section
                  [:div.hero_petitions
                   [:div.hero_content
                    [:h3.hero_title "Zenbridge Explorer"]
                    [:form {:action "/searchpetition"
                            :method "post"}
                     [:div.has-icon-right
                      [:i.form-icon.icon.icon-search]
                      [:input.form-input
                       {:type "text"
                        :id "petitionId"
                        :name "petitionId"
                        :placeholder "Search by transaction Id..."}]]]]]

                  [:div.list.container.grid-lg
                   [:h4.list_title "Petitions list"]
                   [:table.table
                    [:thead
                     [:tr
                      [:th "Family version"]
                      [:th "TxId"]
                      [:th "Family name"]]]
                    [:tbody
                     (for [p petitions]
                       [:tr
                        [:th (get-in p [:header :family_version])]
                        [:th.table_item.text-ellipsis [:a {:href (str "/petition/" (:header_signature p))}
                                                       (str (:header_signature p))]]
                        [:th (get-in p [:header :family_name])]])]]]]
                 (f/when-failed [response]
                                (render-error response)))
)
