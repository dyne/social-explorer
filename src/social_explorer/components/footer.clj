(ns social-explorer.components.footer)

(defn footer []
  [:footer.footer
   [:div.footer_container.container.grid-lg
    [:a.logo {:href "https://www.dyne.org"}
     [:img {:src "/static/img/dyne.png"
            :alt   "Software by Dyne.org"
            :title "Software by Dyne.org"}]]
    [:div.links
     [:a {:href "https://twitter.com/DyneOrg"
          :target "blank"} [:img {:src "/static/img/twitter.png"}]]
     [:a {:href "https://github.com/Commonfare-net/social-wallet"
          :target "blank"} [:img {:src "/static/img/github.png"}]]
     
     ]]
   ])
