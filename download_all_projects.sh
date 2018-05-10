#! /bin/sh

input_file=$1

cat "$input_file" | while read url; do
    the_project_uuid=${url##*/}
    curl -H "Accept: application/json" "$url" > "projects/${the_project_uuid}.json"
    sleep 5
done
