(ns gtr-scraper.util
  (:require [clojure.tools.logging :refer [debugf tracef warnf errorf infof]]
            [fipp.edn :as fipp]))

; This has to be a macro otherwise the source namespace will be obscured in
; the log message.
(defmacro debugp [preface value]
  `(debugf "%s: %s" ~preface (with-out-str (fipp/pprint ~value))))

(defmacro tracep [preface value]
  `(tracef "%s: %s" ~preface (with-out-str (fipp/pprint ~value))))

(defmacro warnp [preface value]
  `(warnf "%s: %s" ~preface (with-out-str (fipp/pprint ~value))))

(defmacro errorp [preface value]
  `(errorf "%s: %s" ~preface (with-out-str (fipp/pprint ~value))))

(defmacro infop [preface value]
  `(infof "%s: %s" ~preface (with-out-str (fipp/pprint ~value))))
