#!/usr/bin/env bash
# Block 2.1 bridge: the app's artist API takes a sequential id and no token. Run in Git Bash:  bash enumerate_talent.sh
# Both hosts belong to the instructor, so anonymous enumeration is fine here.
BASE="https://kazoopromotions.com/api"
echo "GET $BASE/talent (list, no Authorization header)"
curl -s "$BASE/talent" | head -c 400; echo; echo
for id in 1 2 3 4 5 99; do
  printf 'GET /talent/%s -> ' "$id"
  curl -s -o /dev/null -w '%{http_code}\n' "$BASE/talent/$id"
done
echo
echo "Contrast: the orders endpoint has NO id in the path."
printf 'GET https://api.bootcampcentral.com/api/Order/customer -> '
curl -s -o /dev/null -w '%{http_code}\n' "https://api.bootcampcentral.com/api/Order/customer"
