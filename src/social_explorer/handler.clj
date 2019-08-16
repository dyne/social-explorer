;; social-explorer - A generic social wallet UI which uses the social-explorer-api for a beckend

;; Copyright (C) 2019- Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; TODO:

;; This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

;; This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

;; Additional permission under GNU AGPL version 3 section 7.

;; If you modify this Program, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.

(ns social-explorer.handler
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.route :as route]
            [ring.util.response :refer [redirect]]
            [failjure.core :as f]
            [mount.core :as mount]
            [social-explorer.components.petitions :refer [petitions]]
            [social-explorer.components.petition :refer [petition]]
            [social-explorer.webpage :as web]
            [social-explorer.config :refer [config] :as c]
            [social-explorer.swapi :as swapi]
            [social-explorer.util :as u]
            [taoensso.timbre :as log]))

(defn get-host [port] (if port
                        (str (:host (mount/args)) ":" port)
                        (:host (mount/args))))

(defroutes app-routes

(GET "/" request
     (let [{{:keys [page per-page]} :params} request]
       (web/render (petitions (c/get-swapi-params) (cond-> {}
                                                         page (assoc :page page)
                                                         per-page (assoc :per-page per-page))))))

(GET "/petition/:id" request
 (let [{{:keys [id]} :route-params} request]
   (web/render (petition (c/get-swapi-params) id)))
 )

(POST "/searchpetition" request
  (let [{{:keys [petitionId]} :params} request]
    (redirect (str "/petition/" petitionId))
    )
  )

  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
