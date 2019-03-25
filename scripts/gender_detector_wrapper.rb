#! /usr/bin/ruby

require 'gender_detector'

d = GenderDetector.new
puts d.get_gender(ARGV[0])

