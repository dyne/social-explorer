;; social-explorer - A generic social wallet UI which uses the social-explorer-api for a beckend

;; Copyright (C) 2019- Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; Aspasia Beneti <aspra@dyne.org>

;; This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

;; Additional permission under GNU AGPL version 3 section 7.

;; If you modify this Program, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.

(ns social-explorer.core
  (:require [taoensso.timbre :as log]
            [mount.core :as mount]
            [clojure.tools.cli :refer [parse-opts]]

            ;; Mount needs to require the ns in order to start them
            [social-explorer.translation]
            [social-explorer.server]))

(defn parse-args [args]
  (let [opts [["-ho" "--host [webapp host]" "Web app host"
               :default "http://localhost"]
              ["-p" "--port [webapp port]" "Web app port"
               :default 3001
               :parse-fn #(Integer/parseInt %)
               :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
              ["-l" "--listen [listening IP address]" "The IP the service is listening to"
               :default "0.0.0.0"]
              ["-u" "--url [The app URL]" "The app URL to be used for redirects etc."
               :default "http://localhost:3001"]
              ["-c" "--config [config file]" "The app config file path"
               :default "config.yaml"]
              ["-s" "--stub-email [stub email]" "Stub email?"
               :default false
               :parse-fn #(Boolean/parseBoolean %)]
              ["-w" "--with-apikey [with apikey]" "Does the swapi need an api key?"
               :default true
               :parse-fn #(Boolean/parseBoolean %)]
              ["a" "--auth-admin [auth admin]" "Auth admin?"]
              ["-h" "--help"]]]
    (-> (parse-opts args opts)
        :options)))

;; example of an app entry point with arguments
(defn -main [& args]
  (log/info "Starting the social wallet GUI with params " args)
  (mount/start-with-args
   (parse-args args)))


(comment
  (mount/start-with-args {:port 3001 :config "config.yaml" :with-apikey false :url "http://localhost:3001"}))
