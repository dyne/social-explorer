(ns social-explorer.components.pagination)

(defn pagination
  [limit uri]
    [:nav.pagination
     [:a {:href (str uri "?limit="  (+ 10  (read-string limit)))} "Continue"]])