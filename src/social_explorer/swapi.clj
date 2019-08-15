;; social-explorer - A generic social wallet UI which uses the social-explorer-api for a beckend

;; Copyright (C) 2019- Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; Aspasia Beneti <aspra@dyne.org>

;; This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

;; Additional permission under GNU AGPL version 3 section 7.

;; If you modify this Program, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.

(ns social-explorer.swapi
  (:require [taoensso.timbre :as log]
            [org.httpkit.client :as client]
            [clojure.data.json :as json]
            [failjure.core :as f]
            [yaml.core :as yaml]))


(def headers {"Content-Type" "application/json"})



(defn- wrap-response [response fn]
  (if (or (:error response))
      (f/fail (:error response))
      (if (not= 200 (:status response))
        (-> response :body (json/read-str :key-fn keyword) :error f/fail)
        (fn response))))

(defn- swapi-request [{:keys [endpoint json body-parse-fn]
                       {:keys [base-url apikey-file apikey-name]} :swapi-params}]
  (if apikey-file
    (f/attempt-all [device (keyword apikey-name)
                    apikey (f/try* (-> apikey-file slurp yaml/parse-string device))
                    response (f/try* @(client/request {:url (str base-url "/" endpoint)
                                                      :method :post
                                                      :body json
                                                      :headers (assoc headers "x-api-key" apikey)}))]
                   (wrap-response response body-parse-fn)
                   (f/when-failed [apikey]
                     (f/message apikey)))
    (let [response @(client/request {:url (str base-url "/" endpoint)
                                     :method :post
                                     :body json
                                     :headers headers})]
      (wrap-response response body-parse-fn))))

(defn balance [swapi-params params]
  (swapi-request {:swapi-params swapi-params
                  :endpoint "balance"
                  ;; TODO: should be a parameter if it is db or blockchain
                  :json (json/write-str
                         (cond-> {:connection "mongo"
                                  :type "db-only"}
                           (:email params) (merge {:account-id (:email params)})))
                  :body-parse-fn #(-> % :body (json/read-str :key-fn keyword) :amount)}))

(defn label-request [swapi-params params]
  (swapi-request {:swapi-params swapi-params
                  :endpoint "label"
                  :json (json/write-str
                                 {:connection "mongo"
                                  :type "db-only"})
                  :body-parse-fn #(-> % :body (json/read-str :key-fn keyword) :label)}))

(defn sendto [swapi-params params]
  (swapi-request {:swapi-params swapi-params
                  :endpoint "transactions/new"
                  :json (json/write-str
                         (cond-> {:connection "mongo"
                                  :type "db-only"}
                           (:amount params) (assoc :amount (:amount params))
                           (:from params) (assoc :from-id (:from params))
                           (:to params) (assoc :to-id (:to params))
                           (not (empty? (:description params))) (assoc :description (:description params))
                           (not (empty? (:tags params))) (assoc :tags (:tags params))))
                  :body-parse-fn #(-> % :body)}))

(defn list-transactions [swapi-params params]
  (swapi-request {:swapi-params swapi-params
                  :endpoint "transactions/list"
                  :json (json/write-str
                         (cond-> {:connection "sawtooth"
                                  :type "blockchain-and-db"}
                           (:account params) (assoc :account-id (:account params))
                            (not (empty? (:tags params))) (assoc :tags (:tags params))
                           (:page params) (assoc :page (Long/parseLong (:page params)))
                           (:per-page params) (assoc :page (Long/parseLong (:per-page params)))))
                  :body-parse-fn #(-> % :body (json/read-str :key-fn keyword))}))

(defn list-tags [swapi-params params]
  (swapi-request {:swapi-params swapi-params
                  :endpoint "tags/list"
                  :json (json/write-str
                         {:connection "mongo"
                          :type "db-only"})
                  :body-parse-fn #(-> % :body (json/read-str :key-fn keyword) :tags)}))
