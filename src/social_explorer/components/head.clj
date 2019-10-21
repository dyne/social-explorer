(ns social-explorer.components.head
  (:require [hiccup.page :as page])
  )


(declare render-head)

(defn render-head
  ([] (render-head
       "social-explorer" ;; default title
       "social-explorer"
       "https://social-explorer.dyne.org")) ;; default desc

  ([title desc url]
   [:head [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta
     {:name "viewport"
      :content "width=device-width, initial-scale=1, maximum-scale=1"}]

    [:title title]

    ;; javascript scripts
    ; (page/include-js  "/static/js/jquery-3.2.1.min.js")
    ; (page/include-js  "/static/js/bootstrap.min.js")
    (page/include-js "/static/js/highlight-tomorrow.js")
    (page/include-js "/static/js/clipboard.js")
    ; (page/include-js "https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js")
    ;; cascade style sheets
    (page/include-css "/static/css/spectre.min.css")
    (page/include-css "/static/css/highlight-tomorrow.css")
    (page/include-css "/static/css/spectre.icons.min.css")
    ; (page/include-css "/static/css/json-html.css")
    ; (page/include-css "/static/css/highlight-tomorrow.css")
    (page/include-css "/static/css/formatters-styles/html.css")
    (page/include-css "/static/css/formatters-styles/annotated.css")
    ; (page/include-css "/static/css/fa-regular.min.css")
    ; (page/include-css "/static/css/fontawesome.min.css")
    (page/include-css "/static/css/social-explorer.css")]))
