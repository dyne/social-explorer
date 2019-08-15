;; social-explorer - A generic social wallet UI which uses the social-explorer-api for a beckend

;; Copyright (C) 2019- Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; Aspasia Beneti <aspra@dyne.org>

;; This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

;; Additional permission under GNU AGPL version 3 section 7.

;; If you modify this Program, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.

(ns social-explorer.server
  (:require [taoensso.timbre :as log]

            [social-explorer.handler :as h]
            [social-explorer.util :refer [deep-merge]]
            [social-explorer.config :refer [config]]

            [ring.middleware.defaults :refer
             [wrap-defaults site-defaults]]
            [org.httpkit.server :refer [run-server]]
            [mount.core :as mount :refer [defstate]]))

(declare stop-server)

(defn my-wrap-accept [handler {:keys [mime language]}]
  (fn [request]
    (-> request
        (assoc-in [:accept :mime] mime)
        (assoc-in [:accept :language] language)
        handler)))

(defn wrap-with-middleware [handler]
  (-> handler
      (my-wrap-accept {:mime ["text/html"
                              "text/plain"
                              "text/css"]
                       ;; preference in language, fallback to english
                       :language ["en" :qs 0.5
                                  "it" :qs 1
                                  "nl" :qs 1
                                  "hr" :qs 1]})
      (wrap-defaults (deep-merge site-defaults
                                 (-> config :webserver)))))

(defn start-server [{:keys [port]}]
  (log/info "Starting server at port " port " ...")
  (let [handler (wrap-with-middleware h/app-routes)]
    (run-server handler {:port port})))

(defstate server :start (start-server (mount/args))
                 :stop (stop-server))

(defn stop-server []
  (log/info "Stopping server... ")
  (server :timeout 100))
