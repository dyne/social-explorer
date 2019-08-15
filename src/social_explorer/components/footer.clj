(ns social-explorer.components.footer)

(defn footer []
  [:footer
   [:div.divider]
   [:div.navbar
    [:section.navbar-section
     [:img {:src "/static/img/AGPLv3.png" :style "width: 120px"
            :alt "Affero GPLv3 License"
            :title "Affero GPLv3 License"}]
     [:a {:href "https://www.dyne.org"}
      [:img {:src "/static/img/swbydyne.png"
             :alt   "Software by Dyne.org"
             :style "width: 140px"
             :title "Software by Dyne.org"}]]]

    [:section.navbar-section
     [:a {:href "https://github.com/Commonfare-net/social-wallet" :target "blank"} "Github"]]]

    ])
