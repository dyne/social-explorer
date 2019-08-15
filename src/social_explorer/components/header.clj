(ns social-explorer.components.header
  (:require [auxiliary.translation :as t]))


(def header-guest
  [:header.navbar.header
   [:div.navbar-section.container.grid-lg
    [:a.bold.navbar-brand.mr-2 {:href "/"} "social explorer"]]
   ])


(defn header-account [account]
  [:header.navbar.header
   [:div.navbar-section
    [:div.show-md
     [:div.dropdown
      [:a.btn.btn-link.dropdown-toggle {:href "#" :tabIndex "0"}
       [:i.icon.icon-menu]]
      [:ul.menu
       [:li.menu-item
        [:a {:href "/sendto"} (t/locale [:wallet :send])]]
       [:li.menu-item
        [:a {:href "/transactions"}
         (t/locale [:navbar :transactions])]]
       [:li.menu-item
        [:a {:href "/participants"}
         (t/locale [:navbar :participants])]]
       [:li.menu-item
        [:a {:href "/tags"}
         (t/locale [:navbar :tags])]]
       [:li.menu-item
        [:a {:href "/qrcode"}
         (t/locale [:navbar :qrcode])]]
       [:li.menu-item
        [:a {:href "/app-state"} (t/locale [:navbar :conf])]]
       [:li.menu-item
        [:a {:href "/logout"} (t/locale [:navbar :log-out])]]]]]
    [:a.bold.navbar-brand.mr-2 {:href "/" :id "home"} "social wallet"]]
   [:div.navbar-section.hide-md
    [:a.btn.btn-link {:href "/sendto" :id "sendto"}
     (t/locale [:wallet :send])]
    [:a.btn.btn-link {:href "/transactions" :id "transactions"}
     (t/locale [:navbar :transactions])]
    [:a.btn.btn-link {:href "/participants" :id "participants"}
     (t/locale [:navbar :participants])]
    [:a.btn.btn-link {:href "/tags" :id "tags"}
     (t/locale [:navbar :tags])]
    [:div.dropdown
     [:a.btn.btn-link.dropdown-toggle {:href "#" :tabIndex "0" :id "dropdown"}
      [:i.icon.icon-menu]
      ]
     [:ul.menu
      [:li.menu-item
       [:a {:href "/qrcode"}
        (t/locale [:navbar :qrcode])]]
      [:li.menu-item
       [:a {:href "/app-state"} (t/locale [:navbar :conf])]]
      [:li.menu-item
       [:a {:href "/logout" :id "logout"} (t/locale [:navbar :log-out])]]
      ]
     ]]])
