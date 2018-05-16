#! /bin/sh

input_file=$1

cat "$input_file" | while read url; do
    the_person_uuid=${url##*/}
    curl -H "Accept: application/json" "$url" > "persons/${the_person_uuid}.json"
    sleep 5
done
