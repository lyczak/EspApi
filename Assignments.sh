ESP_URL=$1

curl "$ESP_URL/Content/Student/Assignments.aspx" \
 -H 'Accept-Encoding: gzip, deflate, br' \
 -H 'Accept-Language: en-US,en;q=0.9' \
 -H 'Upgrade-Insecure-Requests: 1' \
 -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36 OPR/51.0.2830.55' \
 -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8' \
 -H 'Cache-Control: max-age=0' \
 -H 'Connection: keep-alive' \
 --compressed \
 --cookie Cookies.txt
