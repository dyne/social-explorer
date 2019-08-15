;; social-explorer - TODO:

;; Copyright (C) 2019- Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; TODO:

;; This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

;; Additional permission under GNU AGPL version 3 section 7.

;; If you modify this Program, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.

(ns social-explorer.webpage
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [yaml.core :as yaml]
            [taoensso.timbre :as log]
            [social-explorer.components.header :refer [header-guest header-account]]
            [social-explorer.components.footer :refer [footer]]
            [social-explorer.components.head :refer [render-head]]

            [hiccup.page :as page]
            [hiccup.form :as hf]

            [failjure.core :as f]))

(declare render)
; (declare render-head)
(declare render-yaml)
(declare render-edn)
(declare render-error)
(declare render-error-page)
(declare render-static)

(defn q [req]
  "wrapper to retrieve parameters"
  ;; TODO: sanitise and check for irregular chars
  (get-in req (conj [:params] req)))

(defn button
  ([url text] (button url text [:p]))

  ([url text field] (button url text field "btn-secondary btn-lg"))

  ([url text field type]
   (hf/form-to [:post url]
               field ;; can be an hidden key/value field (project,
               ;; person, etc using hf/hidden-field)
               (hf/submit-button {:class (str "btn " type)} text))))

(defn button-cancel-submit [argmap]
  [:div
   {:class
    (str "row col-md-6 btn-group btn-group-lg "
         (:btn-group-class argmap))
    :role "group"}
   (button
    (:cancel-url argmap) "Cancel"
    (:cancel-params argmap)
    "btn-primary btn-lg btn-danger col-md-3")
   (button
    (:submit-url argmap) "Submit"
    (:submit-params argmap)
    "btn-primary btn-lg btn-success col-md-3")])


(defn reload-session [request]
  ;; TODO: validation of all data loaded via prismatic schema
  #_(conf/load-config "social-explorer" conf/default-settings))

(defn render
  ([body]
   {:headers {"Content-Type"
              "text/html; charset=utf-8"}
    :body (page/html5
           (render-head)
           [:body ;; {:class "static"}
            header-guest
            [:div.container.grid-lg
             [:div body]
             (footer)]])})
  ([account body]
   {:headers {"Content-Type"
              "text/html; charset=utf-8"}
    :body (page/html5
           (render-head)
           [:body [:div (if (empty? account)
                         header-guest
                          (header-account account))
                   [:div.container.grid-lg
                    [:div body]
                    (footer)]]]

           )}))


(defn render-error
  "render an error message without ending the page"
  [err]
  (log/error "Error occured: " err)
  [:div {:class "toast toast-error" :role "alert"}
   [:span {:class "far fa-meh"
           :aria-hidden "true" :style "padding: .5em"}]
   [:span {:class "sr-only"} "Error:"]
   (f/message err)])

(defn render-error-page
  ([]    (render-error-page {} "Unknown"))
  ([err] (render-error-page {} err))
  ([session error]
   (render
    session
    [:div {:class "container-fluid"}
     (render-error error)
    ;  (if-not (empty? session)
    ;    [:div {:class "config"}
    ;     [:h2 "Environment dump:"]
    ;     (render-yaml session)])

     ])))





(defn render-static [body]
  (page/html5 (render-head)
              [:body {:class "fxc static"}

               header-guest

               [:div {:class "container"} body]

               (footer)]))


;; highlight functions do no conversion, take the format they highlight
;; render functions take edn and convert to the highlight format
;; download functions all take an edn and convert it in target format
;; edit functions all take an edn and present an editor in the target format


(defn render-yaml
  "renders an edn into an highlighted yaml"
  [data]
  [:span
   [:pre [:code {:class "yaml"}
          (yaml/generate-string data)]]
   [:script "hljs.initHighlightingOnLoad();"]])


(defn highlight-yaml
  "renders a yaml text in highlighted html"
  [data]
  [:span
   [:pre [:code {:class "yaml"}
          data]]
   [:script "hljs.initHighlightingOnLoad();"]])


(defn highlight-json
  "renders a json text in highlighted html"
  [data]
  [:span
   [:pre [:code {:class "json"}
          data]]
   [:script "hljs.initHighlightingOnLoad();"]])

(defn download-csv
  "takes an edn, returns a csv plain/text for download"
  [data]
  {:headers {"Content-Type"
             "text/plain; charset=utf-8"}
   :body (with-out-str (csv/write-csv *out* data))})

(defn edit-edn
  "renders an editor for the edn in yaml format"
  [data]
  [:div;; {:class "form-group"}
   [:textarea {:class "form-control"
               :rows "20" :data-editor "yaml"
               :id "config" :name "editor"}
    (yaml/generate-string data)]
   [:script {:src "/static/js/ace.js"
             :type "text/javascript" :charset "utf-8"}]
   [:script {:type "text/javascript"}
    (slurp (io/resource "public/static/js/ace-embed.js"))]
   ;; code to embed the ace editor on all elements in page
   ;; that contain the attribute "data-editor" set to the
   ;; mode language of choice
   [:input {:class "btn btn-success btn-lg pull-top"
            :type "submit" :value "submit"}]])

;; (defonce readme
;;   (slurp (io/resource "public/static/README.html")))
