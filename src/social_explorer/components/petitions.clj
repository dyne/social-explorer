(ns social-explorer.components.petitions
  (:require [failjure.core :as f]
            [social-explorer.swapi :as swapi]
            [social-explorer.components.pagination :refer [pagination]]
            [social-explorer.webpage :refer [render-error]]
            [taoensso.timbre :as log]
            ))

(defn petitions [swapi-params query-params uri]
  (f/attempt-all [limit (if (:limit query-params) (:limit query-params) "10")
                  response (swapi/list-transactions swapi-params {:limit limit})
                  petitions (:data response)
                  paging (:paging response)]
                 [:div.navbar-section
                  [:div.hero_petitions
                   [:div.hero_content.container.grid-lg
                    [:div.columns
                     [:div.col-8.p-centered
                      [:h3.hero_title "Zenbridge Explorer"]
                      [:form {:action "/searchpetition"
                              :method "post"}
                       [:div.has-icon-right
                        [:button.btn.btn-link.search  {:type "submit"}
                         [:i.icon.form-icon.icon-search]]]
                        [:input.form-input
                         {:type "text"
                          :id "petitionId"
                          :name "petitionId"
                          :placeholder "Search by transaction Id..."}]]]]]]

                  [:div.list.container.grid-lg
                   [:h4.list_title "Transactions list"]
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
                        [:th [:span (get-in p [:header :family_name])]]])]]
                   (if (nil? (:next_position paging))
                     [:div]
                     (pagination limit uri))]]
                 (f/when-failed [response]
                                (render-error response))))
